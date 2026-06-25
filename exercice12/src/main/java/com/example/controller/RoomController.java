package com.example.controller;

import com.example.dto.CreateRoomRequest;
import com.example.dto.RoomResponse;
import com.example.model.Room;
import com.example.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody CreateRoomRequest request) {
        Room room = service.create(request.getName(), request.getCapacity());
        return ResponseEntity
                .created(URI.create("/api/rooms/" + room.getId()))
                .body(RoomResponse.from(room));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll() {
        List<RoomResponse> responses = service.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
