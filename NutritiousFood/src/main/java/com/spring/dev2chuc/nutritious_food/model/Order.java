package com.spring.dev2chuc.nutritious_food.model;

import com.spring.dev2chuc.nutritious_food.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String note;
    private float totalPrice;
    private int type;
    private int status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    public Order(Address address, float totalPrice, String note, Integer type) {
        this.address = address;
        this.totalPrice = totalPrice;
        this.note = note;
        this.type = type;
        this.status = Status.ACTIVE.getValue();
    }

    public Order() {
    }
}
