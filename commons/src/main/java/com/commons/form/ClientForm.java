package com.commons.form;

import com.commons.enums.ClientType;

public class ClientForm {

    private String name;
    private ClientType type;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ClientType getType() {
        return type;
    }
    public void setType(ClientType type) {
        this.type = type;
    }

}
