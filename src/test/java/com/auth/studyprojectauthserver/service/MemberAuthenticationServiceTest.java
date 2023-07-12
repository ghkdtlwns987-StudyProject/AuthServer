package com.auth.studyprojectauthserver.service;

import com.auth.studyprojectauthserver.Domain.Member.Dto.MemberResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupRequestDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.auth.studyprojectauthserver.Domain.Member.Repository.MemberRepository;
import com.auth.studyprojectauthserver.Domain.Member.Service.impl.MemberAuthenticationServiceImpl;
import com.auth.studyprojectauthserver.Global.Error.Exception.MemberEmailAlreadyExistsException;
import com.auth.studyprojectauthserver.Global.Error.Exception.MemberNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

/**
 * 회원가입 로직을 테스트하는 클래스입니다.
 * 회원가입 로직 뿐 아니라 이메일 중복 가입시 발생하는 예외처리를 테스트 했습니다.
 * @author : 황시준
 * @since  : 1.0
 */
@SpringBootTest
public class MemberAuthenticationServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAuthenticationServiceImpl memberAuthenticationService;


    @Transactional
    @DisplayName("회원 가입 테스트")
    @BeforeEach
    void SignupTest() throws Exception{
        SignupRequestDto signupRequestDto = new SignupRequestDto("test@naver.com", "testName", "testNickName", "testPassword", "010-4828-2771");
        SignupResponseDto signupResponseDto = memberAuthenticationService.signup(signupRequestDto);
        MemberEntity memberEntities = memberRepository.findByEmail(signupResponseDto.getEmail()).orElseThrow(()-> new MemberNotFoundException());

        Assertions.assertEquals(signupRequestDto.getEmail(), memberEntities.getEmail());
        Assertions.assertEquals(signupRequestDto.getName(), memberEntities.getName());
        Assertions.assertEquals(signupRequestDto.getPwd(), memberEntities.getPwd());
        Assertions.assertEquals(signupRequestDto.getPhone(), memberEntities.getPhone());
    }

    @Transactional
    @DisplayName("회원 중복 가입 테스트")
    @Test
    void DuplicationSignupTest() throws Exception {
        SignupRequestDto signupRequestDto1 = new SignupRequestDto("test2@naver.com", "test2Name", "test2NickName", "testPassword", "010-4828-2771");
        SignupRequestDto signupRequestDto2 = new SignupRequestDto("test2@naver.com", "test2Name", "test2NickName", "testPassword", "010-4828-2771");

        memberAuthenticationService.signup(signupRequestDto1);

        MemberEmailAlreadyExistsException exception = Assertions.assertThrows(MemberEmailAlreadyExistsException.class, () -> {
            memberAuthenticationService.signup(signupRequestDto2);
        }, "MemberEmailAlreadyExistsException Called");
        Assertions.assertEquals("user email already exists", exception.getMessage());
    }

    @DisplayName("회원 정보 검색 테스트")
    @AfterEach
    @Test
    void getMemberInfoTest() throws Exception{
        MemberResponseDto memberResponseDto = memberAuthenticationService.getMemberInfo("test@naver.com");
        MemberEntity memberEntities = memberRepository.findByEmail("test@naver.com").orElseThrow(() -> new MemberNotFoundException());

        Assertions.assertEquals(memberResponseDto.getEmail(), memberEntities.getEmail());
        Assertions.assertEquals(memberResponseDto.getName(), memberEntities.getName());
        Assertions.assertEquals(memberResponseDto.getPhone(), memberEntities.getPhone());
    }
}
