package com.example.photomory.controller;

import com.example.photomory.dto.FriendListDto;
import com.example.photomory.service.AuthService;
import com.example.photomory.service.FriendListService;
import com.example.photomory.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-list")
public class FriendListController {

    private final FriendListService friendListService;
    private final AuthService authService; // 토큰 → 사용자 ID 추출

    @GetMapping
    public ResponseEntity<List<FriendListDto>> getMyFriends(@RequestHeader("Authorization") String token) {
        Long loginUserId = authService.extractUserId(token);
        List<FriendListDto> friendList = friendListService.getFriendList(loginUserId);
        return ResponseEntity.ok(friendList);
    }

    @DeleteMapping("/{friendUserId}")
    public ResponseEntity<Void> deleteMyFriend(@RequestHeader("Authorization") String token,
                                             @PathVariable Long friendUserId) {
        Long loginUserId = authService.extractUserId(token);
        friendListService.deleteMyFriend(loginUserId, friendUserId);
        return ResponseEntity.ok().build();
    }


}
