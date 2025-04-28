package com.example.photomory.service; // CustomUserDetailsService가 있는 패키지

import com.example.photomory.repository.AuthUserRepository; // 올바른 import 경로
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository userRepository;

    public CustomUserDetailsService(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException { // 파라미터 이름 일치
        return userRepository.findByUserEmail(userEmail) // 메서드 이름 일치
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUserEmail()) // 올바른 Getter 호출
                        .password(user.getUserPassword()) // 올바른 Getter 호출
                        .roles(user.getUserJob()) // 역할 필드명 확인 후 Getter 호출
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail));
    }
}