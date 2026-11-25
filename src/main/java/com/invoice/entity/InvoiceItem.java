package com.invoice.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="invoice_items")
public class InvoiceItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="invoice_id") private Invoice invoice;
 @Column(nullable=false) private String description;
 private Integer quantity;
 @Column(precision=12,scale=2) private BigDecimal unitPrice;
 @Column(precision=12,scale=2) private BigDecimal lineTotal;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Invoice getInvoice(){return invoice;} public void setInvoice(Invoice v){invoice=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
 public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
 public BigDecimal getLineTotal(){return lineTotal;} public void setLineTotal(BigDecimal v){lineTotal=v;}
}
