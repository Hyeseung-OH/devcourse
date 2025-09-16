package com.jumptospringboot.sbb.user;

import com.jumptospringboot.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    // 빈으로 등록한 PasswordEncoder 객체를 주입받아 사용
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        // BCryptPasswordEncoder: 비크립트(BCrypt) 해시 함수 사용
        // 해시 함수의 하나로 주로 비밀번호와 같은 보안 정보를 안전하게 저장하고 검증할 때 사용
        // 그러나 아래처럼 BCryptPasswordEncoder 객체를 직접 new로 생성하는 방식은 좋지 않음
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    // SiteUser 조회
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found"); // 사용자명에 해당하는 데이터가 없을 경우
        }
    }
}
