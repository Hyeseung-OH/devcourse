package com.jumptospringboot.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 스프링 시큐리티가 로그인 시 사용할 서비스
@RequiredArgsConstructor
@Service
// UserSecurityService는 스프링 시큐리티가 제공하는 UserDetailsService라는 인터페이스를 implements 해야 함
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    // loadUserByUsername: 사용자명으로 스프링 시큐리티의 사용자 객체를 조회하여 리턴하는 메서드
    // 스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가 사용자로부터 입력받은 비밀번호와 일치하는지 검사하는 기능이 내부에 있음
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}