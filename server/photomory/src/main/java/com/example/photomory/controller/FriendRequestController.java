package com.example.photomory.controller;

import com.example.photomory.dto.FriendRequestDto;
import com.example.photomory.dto.FriendResponse;
import com.example.photomory.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestParam Long senderId, @RequestParam Long receiverId) {
        service.sendRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) {
        service.acceptRequest(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/received")
    public ResponseEntity<List<FriendRequestDto>> received(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getReceivedRequests(userId));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequestDto>> sent(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getSentRequests(userId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponse>> getFriends(@RequestParam Long userId) {
        return ResponseEntity.ok(service.getFriendsOf(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFriend(@RequestParam Long userId, @RequestParam Long targetId) {
        service.deleteFriend(userId, targetId);
        return ResponseEntity.ok().build();
    }
}
