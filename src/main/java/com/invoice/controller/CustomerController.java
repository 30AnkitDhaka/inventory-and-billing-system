package com.invoice.controller;
import com.invoice.entity.Customer; import com.invoice.repository.CustomerRepository;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {
 private final CustomerRepository repo; public CustomerController(CustomerRepository r){repo=r;}
 @GetMapping public List<Customer> all(){return repo.findAll();}
 @PostMapping public Customer create(@Valid @RequestBody Customer c){c.setId(null);return repo.save(c);}
 @PutMapping("/{id}") public Customer update(@PathVariable Long id,@Valid @RequestBody Customer c){Customer old=repo.findById(id).orElseThrow();old.setName(c.getName());old.setEmail(c.getEmail());old.setPhone(c.getPhone());old.setAddress(c.getAddress());return repo.save(old);}
 @DeleteMapping("/{id}") public void delete(@PathVariable Long id){repo.deleteById(id);}
}
