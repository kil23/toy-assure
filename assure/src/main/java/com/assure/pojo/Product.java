package com.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"clientSkuId", "clientId"}))
public class Product extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="s")
    @SequenceGenerator(name="s", initialValue=1, allocationSize = 1)
    private Long globalSkuId;

    @NotNull
    @Column(nullable = false)
    private String clientSkuId;

    @NotNull
    @Column(nullable = false)
    private Long clientId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String brandId;

    @NotNull
    @Column(nullable = false)
    private Double mrp = 0.0;

    @Column(columnDefinition = "TEXT")
    private String description;
}
