package io.jprime.agenticrag.videoproductionstore.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ORDERS")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VIDEO_EDITING_CARD_ID", nullable = false)
    private VideoEditingCardEntity videoEditingCard;

    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDate orderDate;

    @Column(name = "ORDER_NOTE", length = 500)
    private String orderNote;

    public OrderEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public CustomerEntity getCustomer() { return customer; }
    public void setCustomer(CustomerEntity customer) { this.customer = customer; }

    public VideoEditingCardEntity getVideoEditingCard() { return videoEditingCard; }
    public void setVideoEditingCard(VideoEditingCardEntity videoEditingCard) { this.videoEditingCard = videoEditingCard; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public String getOrderNote() { return orderNote; }
    public void setOrderNote(String orderNote) { this.orderNote = orderNote; }
}
