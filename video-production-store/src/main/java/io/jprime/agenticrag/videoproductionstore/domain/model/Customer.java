package io.jprime.agenticrag.videoproductionstore.domain.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @EqualsAndHashCode.Include
    private final Integer id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String notes;

    private Customer(Integer id, String name, String email,
                     String phone, String address, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.notes = notes;
    }

    public static Customer enroll(Integer id, String name, String email,
                                  String phone, String address, String notes) {
        return new Customer(id, name, email, phone, address, notes);
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
}
