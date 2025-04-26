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
    private Long user_id;

    private String user_name;
    private String user_email;
    private String user_field;
    private String user_equipment;
    private String user_introduction;
    private String user_job;
    private String photourl;
}
