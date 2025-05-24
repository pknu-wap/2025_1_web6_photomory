package com.example.photomory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RemindNotificationDto {
    private String type;      // "REMIND"
    private String title;     // 사진 제목
    private String photoUrl;  // 사진 URL
    private String date;      // yyyy-MM-dd
    private String message;   // 알림 메시지
}
