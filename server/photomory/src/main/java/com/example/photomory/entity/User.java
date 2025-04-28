package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String userEmail;
    private String userField;
    private String userEquipment;
    private String userIntroduction;
    private String userJob;
    private String photourl;
}
