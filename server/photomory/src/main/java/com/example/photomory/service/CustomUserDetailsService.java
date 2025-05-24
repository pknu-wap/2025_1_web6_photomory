package com.example.photomory.service;

import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository userRepository;

    public CustomUserDetailsService(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println("🔍 [CustomUserDetailsService] 로그인 시도 이메일: " + userEmail);

        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    System.out.println("❌ [CustomUserDetailsService] 해당 이메일의 사용자 없음: " + userEmail);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail);
                });
    }
}
