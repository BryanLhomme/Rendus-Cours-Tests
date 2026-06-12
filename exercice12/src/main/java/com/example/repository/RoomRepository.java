package com.example.repository;

import com.example.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    void deleteAll();
}
