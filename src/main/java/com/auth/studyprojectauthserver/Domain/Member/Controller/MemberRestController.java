package com.auth.studyprojectauthserver.Domain.Member.Controller;

import com.auth.studyprojectauthserver.Domain.Member.Dto.MemberResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupRequestDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.ResultResponse;
import com.auth.studyprojectauthserver.Domain.Member.Dto.SignupResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Service.impl.MemberAuthenticationServiceImpl;
import com.auth.studyprojectauthserver.Global.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Member를 인증하는 Controller입니다.
 * @author : 황시준
 * @since : 1.0
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberAuthenticationServiceImpl memberAuthenticationServiceImpl;
    /**
     * 현재는 인증 서버에 회원가입 API가 존재합니다.
     * 이는 추후 다른 서비스를 제작해 API를 옮길 예정입니다.
     * @param signupRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequestDto signupRequestDto) throws Exception{
        SignupResponseDto signupResponseDto = memberAuthenticationServiceImpl.signup(signupRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.REGISTER_SUCCESS, signupResponseDto);
        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }

    @GetMapping("/members/{loginId}")
    public ResponseEntity getMemberInfo(@PathVariable String loginId) throws Exception{
        MemberResponseDto memberResponseDto = memberAuthenticationServiceImpl.getMemberInfo(loginId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.GET_MY_INFO_SUCCESS, memberResponseDto);

        return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }

}
