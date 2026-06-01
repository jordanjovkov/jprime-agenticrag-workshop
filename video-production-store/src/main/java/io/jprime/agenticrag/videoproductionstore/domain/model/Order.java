package io.jprime.agenticrag.videoproductionstore.domain.model;

import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @EqualsAndHashCode.Include
    private final Integer id;
    private Customer customer;
    private VideoEditingCard videoEditingCard;
    private final LocalDate orderDate;
    private String orderNote;

    private Order(Integer id, Customer customer, VideoEditingCard videoEditingCard,
                  LocalDate orderDate, String orderNote) {
        this.id = id;
        this.customer = customer;
        this.videoEditingCard = videoEditingCard;
        this.orderDate = orderDate;
        this.orderNote = orderNote;
    }

    public static Order place(Integer id, Customer customer, VideoEditingCard videoEditingCard,
                              LocalDate orderDate, String orderNote) {
        return new Order(id, customer, videoEditingCard, orderDate, orderNote);
    }

    /* Domain mutation methods — prepared for future use when service layer
     is refactored to mutate domain objects instead of JPA entities directly.
     */
    void reassignToCustomer(Customer newCustomer) {
        this.customer = newCustomer;
    }

    void replaceVideoEditingCard(VideoEditingCard newVideoEditingCard) {
        this.videoEditingCard = newVideoEditingCard;
    }

    void updateNote(String newNote) {
        this.orderNote = newNote;
    }

    public Integer getId() { return id; }
    public Customer getCustomer() { return customer; }
    public VideoEditingCard getVideoEditingCard() { return videoEditingCard; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getOrderNote() { return orderNote; }
}
