package com.example.photomory.controller;

import com.example.photomory.dto.FriendListDto;
import com.example.photomory.service.AlbumInviteService;
import com.example.photomory.service.AuthService;
import com.example.photomory.service.FriendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class AlbumInviteController {

    private final AlbumInviteService albumInviteService;
    private final AuthService authService;

    // 특정 그룹에 초대할 수 있는 친구 목록 조회
    @GetMapping("/{groupId}/invite-friends")
    public ResponseEntity<List<FriendListDto>> searchInviteFriends(
            @PathVariable Long groupId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestHeader("Authorization") String token) {

        Long loginUserId = authService.extractUserId(token);

        List<FriendListDto> inviteFriends = albumInviteService.findNonMemberFriends(groupId, loginUserId, keyword);

        return ResponseEntity.ok(inviteFriends);
    }
}