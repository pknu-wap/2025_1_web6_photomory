package com.example.photomory.controller;

import com.example.photomory.dto.FriendResponse;
import com.example.photomory.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @DeleteMapping("/{fromUserId}/{toUserId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
        friendService.deleteFriend(fromUserId, toUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FriendResponse>> searchUsers(@RequestParam String name) {
        return ResponseEntity.ok(friendService.searchUsers(name));
    }
}

