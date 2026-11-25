package com.invoice.service;
import com.invoice.entity.*;
import com.invoice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.*;
@Service
public class InvoiceService {
 private final InvoiceRepository invoices;
 private final CustomerRepository customers;

 public InvoiceService(InvoiceRepository i, CustomerRepository c){
  invoices=i;
  customers=c;
 }
 public List<Invoice> all(){
  return invoices.findAll();
 }
 public Invoice get(Long id){
  return invoices.findById(id).orElseThrow(()->new NoSuchElementException("Invoice not found: "+id));
 }
 @Transactional public Invoice create(Map<String,Object> body){

  Long customerId=Long.valueOf(body.get("customerId").toString());
  Customer customer=customers.findById(customerId).orElseThrow(()->new NoSuchElementException("Customer not found"));
  Invoice inv=new Invoice();
  inv.setInvoiceNumber("INV-"+System.currentTimeMillis());
  inv.setCustomer(customer); inv.setIssueDate(LocalDate.parse(body.get("issueDate").toString()));
  inv.setDueDate(LocalDate.parse(body.get("dueDate").toString()));
  BigDecimal subtotal=BigDecimal.ZERO;
  Object raw=body.get("items");
  if(raw instanceof List<?> list) for(Object obj:list){
   Map<?,?> m=(Map<?,?>)obj; InvoiceItem item=new InvoiceItem();
   item.setDescription(m.get("description").toString());
   item.setQuantity(Integer.parseInt(m.get("quantity").toString()));
   item.setUnitPrice(new BigDecimal(m.get("unitPrice").toString()));
   item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
   item.setInvoice(inv);
   inv.getItems().add(item);
   subtotal=subtotal.add(item.getLineTotal());
  }
  BigDecimal taxRate=new BigDecimal(body.getOrDefault("taxRate","0").toString());
  BigDecimal tax=subtotal.multiply(taxRate).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
  inv.setSubtotal(subtotal);
  inv.setTax(tax);
  inv.setTotal(subtotal.add(tax));
  return invoices.save(inv);
 }
 public Invoice updateStatus(Long id, String status){Invoice i=get(id); i.setStatus(Invoice.Status.valueOf(status.toUpperCase())); return invoices.save(i);}
 public void delete(Long id){invoices.delete(get(id));}
 public Map<String,Object> dashboard(){
  List<Invoice> all=all(); BigDecimal revenue=all.stream().filter(i->i.getStatus()==Invoice.Status.PAID).map(Invoice::getTotal).reduce(BigDecimal.ZERO,BigDecimal::add);
  long paid=all.stream().filter(i->i.getStatus()==Invoice.Status.PAID).count();
  long overdue=all.stream().filter(i->i.getStatus()==Invoice.Status.OVERDUE || (i.getStatus()==Invoice.Status.PENDING && i.getDueDate()!=null && i.getDueDate().isBefore(LocalDate.now()))).count();
  return Map.of("totalInvoices",all.size(),"paidInvoices",paid,"pendingInvoices",all.size()-paid-overdue,"overdueInvoices",overdue,"revenue",revenue);
 }
}
