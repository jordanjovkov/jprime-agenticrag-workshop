package io.jprime.agenticrag.videoproductionstore.domain.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StockAvailability {

    @EqualsAndHashCode.Include
    private final Integer id;
    private final VideoEditingCard videoEditingCard;
    private Integer availability;

    private StockAvailability(Integer id, VideoEditingCard videoEditingCard, Integer availability) {
        this.id = id;
        this.videoEditingCard = videoEditingCard;
        this.availability = availability;
    }

    public static StockAvailability initialize(Integer id, VideoEditingCard videoEditingCard,
                                               Integer availability) {
        return new StockAvailability(id, videoEditingCard, availability);
    }

    /* Domain mutation methods — prepared for future use when service layer
     is refactored to mutate domain objects instead of JPA entities directly.
     dispatch() enforces the stock availability invariant.
     */
    void restock(int quantity) {
        this.availability += quantity;
    }

    void dispatch(int quantity) {
        if (quantity > this.availability) {
            throw new IllegalStateException(
                    "Insufficient stock for dispatch. Requested: " + quantity + ", available: " + this.availability);
        }
        this.availability -= quantity;
    }

    public Integer getId() { return id; }
    public VideoEditingCard getVideoEditingCard() { return videoEditingCard; }
    public Integer getAvailability() { return availability; }
}
