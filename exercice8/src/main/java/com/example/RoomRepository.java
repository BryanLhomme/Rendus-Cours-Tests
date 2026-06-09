package com.example;

public interface RoomRepository {
    boolean exists(String code);
    Room findByCode(String code);
}
