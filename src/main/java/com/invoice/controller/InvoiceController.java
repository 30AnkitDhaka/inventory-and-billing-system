package com.invoice.controller;
import com.invoice.entity.Invoice; import com.invoice.repository.InvoiceRepository; import com.invoice.service.InvoiceService;
import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/invoices") @CrossOrigin
public class InvoiceController {
 private final InvoiceService service;
 private final InvoiceRepository repo;
 public InvoiceController(InvoiceService s,InvoiceRepository r){
  service=s;
  repo=r;
 }
 @GetMapping public
 List<Invoice> all(){
  return service.all();
 }
 @GetMapping("/{id}")
 public Invoice get(@PathVariable Long id){
  return service.get(id);
 }
 @PostMapping public Invoice create(@RequestBody Map<String,Object> body){
  return service.create(body);
 }
 @PatchMapping("/{id}/status")
 public Invoice status(@PathVariable Long id,@RequestBody Map<String,String> body){
  return service.updateStatus(id,body.get("status"));
 }
 @DeleteMapping("/{id}")
 public void delete(@PathVariable Long id){
  service.delete(id);
 }
 @GetMapping("/dashboard")
 public Map<String,Object> dashboard()
 {return service.dashboard();
 }
}
