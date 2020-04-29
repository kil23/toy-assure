package com.commons.form;

import com.commons.enums.InvoiceType;

public class ChannelForm {

    private String name;
    private InvoiceType type;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public InvoiceType getType() {
        return type;
    }
    public void setType(InvoiceType type) {
        this.type = type;
    }
}
