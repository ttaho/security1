package com.example.security1.config.auth;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailService 타입으로 IoC되어 있는 loadUserByUsername 메소드가 실행
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 이것의 return은 시큐리티 session 의 Authentication 안의 UserDetails이다.
    // 시큐리티 session(Authentication(UserDetails -> PrincipalDetails))
    // 즉 UserDetails가 들어갈 자리에 PrincipalDetails가 들어간다.

    // 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("principalDetailsService의 loadUserByUsername");
        User user = userRepository.findByUsername(username);
        if(user != null) return new PrincipalDetails(user);
        return null;
    }
}
