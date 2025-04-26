package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long from_user_id;
    private Long to_user_id;
    private Boolean are_we_friend;
}
