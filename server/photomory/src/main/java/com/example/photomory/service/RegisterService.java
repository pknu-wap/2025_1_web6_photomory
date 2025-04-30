package com.example.photomory.service;

import com.example.photomory.dto.RegisterRequestDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ✅ 인터페이스로 변경

    public RegisterService(AuthUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final List<String> specialCharacters = Arrays.asList(
            "!", "@", "#", "$", "%", "^", "&", "*", ".", ",", "₩", "~", "?", "/"
    );
    private final List<String> nums = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    );

    @Transactional
    public String register(RegisterRequestDto userRequestDto) {
        // ✅ 이메일 중복 체크
        Optional<UserEntity> existingUser = userRepository.findByUserEmail(userRequestDto.getUser_email());
        if (existingUser.isPresent()) {
            return "회원가입 실패(이미 존재하는 이메일입니다.)";
        }

        // ✅ 비밀번호 유효성 검사
        int specialCharacterCount = 0;
        int numCount = 0;

        for (String specialCharacter : specialCharacters) {
            if (userRequestDto.getUser_password().contains(specialCharacter)) {
                specialCharacterCount++;
            }
        }

        for (String num : nums) {
            if (userRequestDto.getUser_password().contains(num)) {
                numCount++;
            }
        }

        if (
                numCount >= 4 &&
                        specialCharacterCount >= 2 &&
                        userRequestDto.getUser_password().length() >= 12 &&
                        userRequestDto.getUser_email().contains("@")
        ) {
            try {
                // ✅ 비밀번호 암호화
                String encodedPassword = passwordEncoder.encode(userRequestDto.getUser_password());

                // ✅ 엔티티 생성 (암호화된 비밀번호 포함)
                UserEntity userEntity = new UserEntity(
                        userRequestDto.getUser_email(),
                        userRequestDto.getUser_name(),
                        encodedPassword,
                        userRequestDto.getUser_photourl(),
                        userRequestDto.getUser_equipment(),
                        userRequestDto.getUser_introduction(),
                        userRequestDto.getUser_job(),
                        userRequestDto.getUser_field()
                );

                // ✅ 저장
                userRepository.save(userEntity);
                return "회원가입 완료";

            } catch (Exception e) {
                e.printStackTrace(); // 개발 중에는 OK, 이후 로거로 변경 권장
                return "회원가입 실패(데이터베이스 오류 발생)";
            }

        } else {
            return "회원가입 실패(비밀번호 형식 불일치)";
        }
    }
}
