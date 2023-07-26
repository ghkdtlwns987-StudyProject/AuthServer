package com.auth.studyprojectauthserver.controller;

import com.auth.studyprojectauthserver.Domain.Member.Controller.AuthorizationController;
import com.auth.studyprojectauthserver.Domain.Member.Repository.MemberRepository;
import com.auth.studyprojectauthserver.Domain.Member.Service.impl.MemberAuthenticationServiceImpl;
import com.auth.studyprojectauthserver.Global.Jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
@SpringJUnitConfig
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthorizationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberAuthenticationServiceImpl memberAuthenticationService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("유저 정보 등록")
    @BeforeEach
    void setUp() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("email", "test@naver.com");
        requestBody.put("name", "test");
        requestBody.put("nickname", "test-nickname");
        requestBody.put("pwd", "testPassword1234");
        requestBody.put("phone", "010-1234-1234");

        mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                )
                .andExpect(status().isOk());

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @DisplayName("Authorization 헤더 테스트")
    @Test
    void authorizationHeaderTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("loginId", "test@naver.com");
        requestBody.put("pwd", "testPassword1234");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String authorizationHeader = response.getHeader("Authorization");
        String uuid = response.getHeader("UUID_HEADER");
        String tokenWithPrefix = "Bearer "; // Bearer 토큰의 접두사
        String accessToken = authorizationHeader.substring(tokenWithPrefix.length());

        // Embedded Redis 서버 시작

        System.out.println("accessToken : " + accessToken);
        System.out.println("UUID_HEADER : " + uuid);
        String extractLoginId = jwtTokenProvider.extractLoginId(accessToken);

        mockMvc.perform(MockMvcRequestBuilders.get("/authorization")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertEquals(extractLoginId, uuid);

    }

}