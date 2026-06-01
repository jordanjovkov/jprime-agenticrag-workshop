package io.jprime.agenticrag.videoproductionstore.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "STOCK_AVAILABILITY")
public class StockAvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VIDEO_EDITING_CARD_ID", nullable = false)
    private VideoEditingCardEntity videoEditingCard;

    @Column(name = "AVAILABILITY", nullable = false)
    private Integer availability;

    public StockAvailabilityEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public VideoEditingCardEntity getVideoEditingCard() { return videoEditingCard; }
    public void setVideoEditingCard(VideoEditingCardEntity videoEditingCard) { this.videoEditingCard = videoEditingCard; }

    public Integer getAvailability() { return availability; }
    public void setAvailability(Integer availability) { this.availability = availability; }
}
