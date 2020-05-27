package com.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"globalSkuId"}))
public class Inventory extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long globalSkuId;

    @NotNull
    @Column(nullable = false)
    private Long availableQuantity = 0L;

    @NotNull
    @Column(nullable = false)
    private Long allocatedQuantity = 0L;

    @NotNull
    @Column(nullable = false)
    private Long fulfilledQuantity = 0L;
}
