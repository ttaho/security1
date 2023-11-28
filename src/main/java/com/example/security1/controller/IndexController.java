package com.example.security1.controller;


import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/test/login")
    @ResponseBody
    public String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){ //@AuthenticationPrincipal 어노테이션을 이용해서 session의 security 세션 중 userDetails에 접근할 수 있다.
        System.out.println("/test/login ==============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //구글 로그인은 ClassCastException 발생!
        System.out.println("authentication : " + principalDetails.getUser());
        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth){
        System.out.println("/test/oauth/login ==============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oAuth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }



    @GetMapping({"","/"})
    public String index(){
        return "index";
    }


    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails : " + principalDetails);

        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    @ResponseBody
    public String manager(){
        return "manager";
    }

    // 스프링시큐리티가 주소를 낚아챈다 - SecurityConfig 파일 생성 후 작동안함
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        userService.saveUser(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정메소드에 권한 있는 유저만 접속가능하게 걸어줄때사용
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인정보";
    }

    // 메소드 호출전에 여러개의 권한을 걸고싶으면 이렇게.
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//    @PostAuthorize() // 얘는 메소드가 끝난후에 하고싶으면 하기
    @GetMapping("/data")
    @ResponseBody
    public String data(){
        return "데이터";
    }
}
