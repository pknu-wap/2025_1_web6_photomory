package com.example.photomory.controller;

import com.example.photomory.dto.UserProfileResponse;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("user_name") String userName,
            @RequestPart("user_introduction") String userIntroduction,
            @RequestPart("user_job") String userJob,
            @RequestPart("user_equipment") String userEquipment,
            @RequestPart("user_field") String userField,
            @RequestPart("user_area") String userArea,
            @RequestPart(value = "user_photourl", required = false) MultipartFile userPhotourl
    ) {
        userService.updateUserProfile(
                userDetails.getUser(),
                userName, userIntroduction, userJob,
                userEquipment, userField, userArea, userPhotourl
        );
        return ResponseEntity.ok("프로필이 성공적으로 수정되었습니다.");
    }
}
