package com.invoice.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@Entity @Table(name="invoices")
public class Invoice {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true,nullable=false) private String invoiceNumber;
 @ManyToOne(optional=false) private Customer customer;
 private LocalDate issueDate;
 private LocalDate dueDate;
 @Column(precision=12,scale=2) private BigDecimal subtotal=BigDecimal.ZERO;
 @Column(precision=12,scale=2) private BigDecimal tax=BigDecimal.ZERO;
 @Column(precision=12,scale=2) private BigDecimal total=BigDecimal.ZERO;
 @Enumerated(EnumType.STRING) private Status status=Status.PENDING;
 @OneToMany(mappedBy="invoice",cascade=CascadeType.ALL,orphanRemoval=true)
 private List<InvoiceItem> items=new ArrayList<>();
 public enum Status { PENDING, PAID, OVERDUE }
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getInvoiceNumber(){return invoiceNumber;} public void setInvoiceNumber(String v){invoiceNumber=v;}
 public Customer getCustomer(){return customer;} public void setCustomer(Customer v){customer=v;}
 public LocalDate getIssueDate(){return issueDate;} public void setIssueDate(LocalDate v){issueDate=v;}
 public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
 public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;}
 public BigDecimal getTax(){return tax;} public void setTax(BigDecimal v){tax=v;}
 public BigDecimal getTotal(){return total;} public void setTotal(BigDecimal v){total=v;}
 public Status getStatus(){return status;} public void setStatus(Status v){status=v;}
 public List<InvoiceItem> getItems(){return items;} public void setItems(List<InvoiceItem> v){items=v;}
}
