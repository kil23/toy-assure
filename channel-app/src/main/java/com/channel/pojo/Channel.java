package com.channel.pojo;

import com.commons.enums.InvoiceType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_CHANNEL", uniqueConstraints= @UniqueConstraint(columnNames = {"name"}))
public class Channel extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }
}