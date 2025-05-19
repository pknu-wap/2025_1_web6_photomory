package com.example.photomory.dto;

public class FriendRequestDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String status;

    public FriendRequestDto(Long id, Long senderId, Long receiverId, String status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

}
