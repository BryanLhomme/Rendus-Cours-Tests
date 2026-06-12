package com.example.model;

public class Room {

    private Long id;
    private String name;
    private int capacity;

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
}
