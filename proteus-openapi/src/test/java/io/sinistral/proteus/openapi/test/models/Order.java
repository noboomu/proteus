package io.sinistral.proteus.openapi.test.models;

public class Order {
    public Long id;
    public String description;
    public String orderNumber;
    
    public Order() {}
    public Order(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
