package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(FriendId.class) // ★ 복합키를 사용하겠다고 명시
public class Friend {

    @Id
    @Column(name = "from_user_id")
    private Long fromUserId;

    @Id
    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "are_we_friend")
    private Boolean areWeFriend;
}
