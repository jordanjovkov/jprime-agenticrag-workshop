package io.jprime.agenticrag.videoproductionstore.domain.model;

import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VideoEditingCard {

    @EqualsAndHashCode.Include
    private final Integer id;
    private final String name;
    private final String manufacturer;
    private final String description;
    private final BigDecimal price;

    private VideoEditingCard(Integer id, String name, String manufacturer,
                             String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.price = price;
    }

    public static VideoEditingCard register(Integer id, String name, String manufacturer,
                                            String description, BigDecimal price) {
        return new VideoEditingCard(id, name, manufacturer, description, price);
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
}
