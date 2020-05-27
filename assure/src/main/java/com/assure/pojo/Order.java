package com.assure.pojo;

import com.commons.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"channelId", "channelOrderId"}))
public class Order extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long clientId;

    @NotNull
    @Column(nullable = false)
    private Long customerId;

    @NotNull
    @Column(nullable = false)
    private Long channelId;

    @NotNull
    @Column(nullable = false)
    private String channelOrderId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
}
