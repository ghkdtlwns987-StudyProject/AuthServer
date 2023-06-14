package com.auth.studyprojectauthserver;

import com.auth.studyprojectauthserver.Domain.Member.Entity.MemberEntity;
import com.auth.studyprojectauthserver.Domain.Member.Repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 회원가입과 로그인 요청을 수행하는 코드입니다.
 * 회원가입의 경우 @BeforeEach로 회원가입을 먼저 진행시킨 후 로그인을 수행합니다.
 * 로그인 실패 시 200OK메시지가 반환되지만, 인증 시 발급되는 헤더가 생성되지 않습니다.
 * @author : 황시준
 * @since  : 1.0
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입 테스트")
    @BeforeEach
    public void signupRequestTest() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("email", "test@naver.com");
        input.put("pwd", "testPassword1234");
        input.put("name", "test");
        input.put("gender", "M");
        input.put("phone", "010-4828-2771");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andDo(print());

        System.out.println("Signup Request Successful");
        System.out.println("[+] Find Member For userEmail...");

        MemberEntity memberEntity = memberRepository.findByEmail("test@naver.com");
        System.out.println("[+] memberEntity.getEmail : " + memberEntity.getEmail());
        System.out.println("[+] memberEntity.getName  : " + memberEntity.getName());
        System.out.println("[+] memberEntity.getPwd   : " + memberEntity.getPwd());
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginRequestTest() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("loginId", "test@naver.com");
        input.put("pwd", "testPassword1234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("UUID_HEADER"))
                .andExpect(header().exists("X-Expire"))
                .andDo(print());

        System.out.println("[+] Login Request Successful");
    }

    @AfterEach
    @DisplayName("로그인 실패 테스트(잘못된 로그인 요청)")
    void loginRequestFailTest() throws Exception{
        Map<String, String> input = new HashMap<>();
        input.put("loginId", "testfail@fail.com");
        input.put("pwd", "testPassword1234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Authorization"))
                .andExpect(header().doesNotExist("UUID_HEADER"))
                .andExpect(header().doesNotExist("X-Expire"))
                .andDo(print());

        System.out.println("[+] 로그인 실패 테스트 FIN");

    }

}
