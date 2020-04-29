package com.assure.pojo;

import com.commons.enums.ClientType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_CLIENT", uniqueConstraints= @UniqueConstraint(columnNames = {"name"}))
public class Client extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ClientType type;

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

    public ClientType getType() {
        return type;
    }
    public void setType(ClientType type) {
        this.type = type;
    }
}
