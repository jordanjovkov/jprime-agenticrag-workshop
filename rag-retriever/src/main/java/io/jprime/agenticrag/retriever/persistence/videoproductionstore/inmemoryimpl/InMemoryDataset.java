package io.jprime.agenticrag.retriever.persistence.videoproductionstore.inmemoryimpl;

import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Customer;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.Order;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.StockAvailability;
import io.jprime.agenticrag.retriever.domain.model.videoproductionstore.VideoEditingCard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/* TODO: Replace this in-memory dataset with a database-backed implementation on the next project stage.
 All fields must remain immutable (List.of()) — this class is used as shared static state.
 Replacing List.of() with a mutable list would introduce shared mutable state across threads.
 */
public class InMemoryDataset {

    public static final List<VideoEditingCard> VIDEO_EDITING_CARDS = List.of(
            new VideoEditingCard(1, "Movie Machine Pro", "Fast Multimedia AG", "For enthusiasts and professional Productions", new BigDecimal("350.00")),
            new VideoEditingCard(2, "DPS Velocity",      "DPS",               "Professional for TV and Productions", new BigDecimal("1200.00")),
            new VideoEditingCard(3, "Media 100",         "Data Translation",  "MAC oriented Professional for TV and Productions", new BigDecimal("450.00")),
            new VideoEditingCard(4, "MiroMotion DC30",   "Pinacle",           "For enthusiasts and professionals", new BigDecimal("280.00"))
    );

    public static final List<Customer> CUSTOMERS = List.of(
            new Customer(1, "Ivan Ivanov",    "ivan.ivanov@gmail.com",   "0888111222", "Sofia, Vitosha 1",        "VIP client"),
            new Customer(2, "Petar Petrov",  "petar.petrov@gmail.com",  "0888333444", "Plovdiv, Maritsa 5",      "Unable to pick up the phone by 2 pm"),
            new Customer(3, "Georgi Georgiev","georgi.georgiev@gmail.com", "0888555666", "Varna, Cherno more 12",   "He prefers quick delivery"),
            new Customer(4, "Nikolay Nikolaev",  "nikolay.nikolaev@gmail.com",  "0888777888", "Sofia, Tech Park",      "The building lift is not working")
    );

    public static final List<StockAvailability> STOCK_AVAILABILITIES = List.of(
            new StockAvailability(1, VIDEO_EDITING_CARDS.get(0), 5),
            new StockAvailability(2, VIDEO_EDITING_CARDS.get(1), 23),
            new StockAvailability(3, VIDEO_EDITING_CARDS.get(2), 47),
            new StockAvailability(4, VIDEO_EDITING_CARDS.get(3), 12)
    );

    public static final List<Order> ORDERS = List.of(
            new Order(1, CUSTOMERS.get(0), VIDEO_EDITING_CARDS.get(0), LocalDate.of(2025, 1, 15), "Urgent delivery"),
            new Order(2, CUSTOMERS.get(0), VIDEO_EDITING_CARDS.get(2), LocalDate.of(2025, 2, 20), "Only credit card payment"),
            new Order(3, CUSTOMERS.get(1), VIDEO_EDITING_CARDS.get(1), LocalDate.of(2025, 3, 10), "Customer gift"),
            new Order(4, CUSTOMERS.get(2), VIDEO_EDITING_CARDS.get(0), LocalDate.of(2025, 3, 25), "The same delivery for the next order"),
            new Order(5, CUSTOMERS.get(2), VIDEO_EDITING_CARDS.get(3), LocalDate.of(2025, 4,  5), "Use light weight packages"),
            new Order(6, CUSTOMERS.get(3), VIDEO_EDITING_CARDS.get(1), LocalDate.of(2025, 4, 18), "See the invoice details")
    );
}