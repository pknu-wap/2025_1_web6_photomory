package com.example.photomory.controller;

import com.example.photomory.dto.FriendRequestDto;
import com.example.photomory.dto.FriendResponse;
import com.example.photomory.dto.NonFriendUserDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.security.JwtTokenProvider;
import com.example.photomory.service.AuthService;
import com.example.photomory.service.FriendRequestService;
import com.example.photomory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final FriendRequestService friendRequestService;
    private final AuthService authService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Long>> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestParam Long receiverId) {
        Long senderId = authService.extractUserId(token);
        Long requestId = friendRequestService.sendRequest(senderId, receiverId);

        Map<String, Long> response = new HashMap<>();
        response.put("requestId", requestId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        friendRequestService.acceptRequest(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        friendRequestService.rejectRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/non-friends/search")
    public ResponseEntity<List<NonFriendUserDto>> searchNonFriendUsers(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "keyword", required = false) String keyword) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다.");
        }

        String pureToken = token.substring(7); // "Bearer " 제거
        Long loginUserId = authService.extractUserId(pureToken);

        List<NonFriendUserDto> nonFriends = friendRequestService.searchNonFriendUsers(loginUserId, keyword);
        return ResponseEntity.ok(nonFriends);
    }

}
