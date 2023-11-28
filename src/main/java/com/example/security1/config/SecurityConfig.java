package com.example.security1.config;

import com.example.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록되게 한다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured라는 어노테이션 활성화, preAuthorize,postAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable(); // csrf 비활성화. csrf가 켜져있으면 form 태그로 요청시 csrf 토큰이 추가된다.
        // 그래서 서버쪽에서 만들어준 form 태그로만 요청이 가능하게 된다.
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // "/user"로 가면 인증해야된다.
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 위세가지 주소가 아니면 누구나 들어갈 수 있다.
                .and()
                .formLogin()
                .loginPage("/loginForm") // 사용자가 인증되지 않은 경우 로그인 페이지로 리다이렉트 하는데 그게 /loginForm 요청이다.
//                .usernameParameter("email") // 넘어오는 parameter가 username이 아닌 email로 변경할땐 이걸로.
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. controller를 만들어주지 않아도 된다.
				.defaultSuccessUrl("/") // 로그인이 성공하면 / 주소로 간다.
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);// 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip 코드X, (액세서토큰 + 사용자프로필정보 O)


//        http.logout() // 로그아웃 기능 작동함
//                .logoutUrl("/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
//                .logoutSuccessUrl("/logoutSuccess"); // 로그아웃 성공 후 이동페이지



        return http.build();
    }


}
