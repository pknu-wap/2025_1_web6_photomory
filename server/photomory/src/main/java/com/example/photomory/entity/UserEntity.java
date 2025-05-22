    package com.example.photomory.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "USERS")
    public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "user_name", nullable = false)
        private String userName;

        @Column(name = "user_email", nullable = false, unique = true)
        private String userEmail;

        @Column(name = "user_password", nullable = false)
        private String userPassword;

        @Column(name = "user_photourl")
        private String userPhotourl;

        @Column(name = "user_equipment", nullable = false)
        private String userEquipment;

        @Column(name = "user_introduction", columnDefinition = "TEXT")
        private String userIntroduction;

        @Column(name = "user_job", nullable = false)
        private String userJob;

        @Column(name = "user_field", nullable = false)
        private String userField;

        public UserEntity() {}

        public UserEntity(
                String userEmail,
                String userName,
                String userPassword,
                String userPhotourl,
                String userEquipment,
                String userIntroduction,
                String userJob,
                String userField
        ) {
            this.userEmail = userEmail;
            this.userName = userName;
            this.userPassword = userPassword;
            this.userPhotourl = userPhotourl;
            this.userEquipment = userEquipment;
            this.userIntroduction = userIntroduction;
            this.userJob = userJob;
            this.userField = userField;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public String getUserPhotourl() {
            return userPhotourl;
        }

        public String getUserEquipment() {
            return userEquipment;
        }

        public String getUserIntroduction() {
            return userIntroduction;
        }

        public String getUserJob() {
            return userJob;
        }

        public String getUserField() {
            return userField;
        }
    }
