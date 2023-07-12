package com.auth.studyprojectauthserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@TestMethodOrder(MethodOrderer.MethodName.class)
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("회원가입 테스트")
    @Test
    public void signUpTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("email", "amd@naver.com");
        requestBody.put("name", "amd");
        requestBody.put("nickname", "amd-nickname");
        requestBody.put("pwd", "amd-Password");
        requestBody.put("phone", "010-1234-1234");

        mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("member-signup",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("status").description("상태 코드"),
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        subsectionWithPath("data").description("응답 데이터"),
                                        subsectionWithPath("_links").description("하이퍼미디어 링크 정보")
                                ),
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("name").description("이름"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("pwd").description("비밀번호"),
                                        fieldWithPath("phone").description("전화번호")
                                )
                        )
                );
        }


    @DisplayName("회원가입 중복 테스트")
    @Test
    public void duplicationSignupTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("email", "amd@naver.com");
        requestBody.put("name", "amd");
        requestBody.put("nickname", "amd-nickname");
        requestBody.put("pwd", "amd-Password");
        requestBody.put("phone", "010-1234-1234");

        mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                )
                .andExpect(status().isInternalServerError())
                .andDo(
                        document("member-duplicate-signup",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("success").description("성공 여부"),
                                        fieldWithPath("status").description("상태 코드"),
                                        fieldWithPath("data").description("데이터"),
                                        fieldWithPath("errorMessage").description("에러 메시지")
                                )
                        )
                );
    }

    @DisplayName("유저 검색 테스트")
    @Test
    public void getMemberInfoTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        mockMvc.perform(
                        get("/auth/members/amd@naver.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("member-getMemberInfo",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("status").description("상태 코드"),
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.userId").description("사용자 ID"),
                                        fieldWithPath("data.email").description("이메일"),
                                        fieldWithPath("data.nickname").description("닉네임"),
                                        fieldWithPath("data.name").description("이름"),
                                        fieldWithPath("data.phone").description("전화번호"),
                                        fieldWithPath("data.roles").description("역할"),
                                        fieldWithPath("data.createAt").description("생성 시간"),
                                        fieldWithPath("_links.self.href").description("요청 URL")
                                )
                        )
                );
    }


    @DisplayName("회원 수정 테스트")
    @Test
    public void updateTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("nickname", "amd-nickname-update");
        requestBody.put("pwd", "amd-Password");
        requestBody.put("phone", "010-1234-1234");

        mockMvc.perform(
                        put("/auth/members/amd@naver.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("member-update",
                                Preprocessors.preprocessRequest(prettyPrint()),
                                Preprocessors.preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("status").description("상태 코드"),
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.userId").description("사용자 ID"),
                                        fieldWithPath("data.email").description("이메일"),
                                        fieldWithPath("data.nickname").description("닉네임"),
                                        fieldWithPath("data.name").description("이름"),
                                        fieldWithPath("data.phone").description("전화번호"),
                                        fieldWithPath("data.roles").description("역할"),
                                        fieldWithPath("data.createAt").description("생성 시간"),
                                        fieldWithPath("_links.self.href").description("요청 URL")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").description("업데이트할 nickname"),
                                        fieldWithPath("pwd").description("업데이트할 비밀번호"),
                                        fieldWithPath("phone").description("업데이트할 전화번호")
                                )
                        )
                );
    }
}