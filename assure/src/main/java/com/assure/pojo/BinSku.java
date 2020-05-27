package com.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"binId", "globalSkuId"}))
public class BinSku extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long binId;

    @NotNull
    @Column(nullable = false)
    private Long globalSkuId;

    @Min(0)
    @Max(10000)
    @NotNull
    @Column(nullable = false)
    private Long quantity = 0L;

}
