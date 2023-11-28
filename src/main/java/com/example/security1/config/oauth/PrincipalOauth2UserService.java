package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken요청
        // 여기까지가 userRequest정보 -> 회원프로필을 받아야함 그때 사용되는 메소드가(loadUser메소드) -> 구글로부터 회원프로필을 받았다.

        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; // google_107157108223565402072
        String password = bCryptPasswordEncoder.encode("1234");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User user = userRepository.findByUsername(username);

        // OAuth2로 로그인시 그 정보들로 우리 DB에 회원가입
        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(provider)
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
