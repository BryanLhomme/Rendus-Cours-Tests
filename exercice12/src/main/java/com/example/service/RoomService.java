package com.example.service;

import com.example.model.Room;
import com.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(String name, int capacity) {
        Room room = new Room(name, capacity);
        return repository.save(room);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }
}
