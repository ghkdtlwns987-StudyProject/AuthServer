package com.auth.studyprojectauthserver.Domain.Member.Controller;

import com.auth.studyprojectauthserver.Domain.Member.Dto.*;
import com.auth.studyprojectauthserver.Domain.Member.Service.impl.MemberAuthenticationServiceImpl;
import com.auth.studyprojectauthserver.Global.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    public EntityModel<ResultResponse> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) throws Exception{
        SignupResponseDto signupResponseDto = memberAuthenticationServiceImpl.signup(signupRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.REGISTER_SUCCESS, signupResponseDto);
        // return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));

        EntityModel<ResultResponse> entityModel = EntityModel.of(resultResponse);
        entityModel.add(linkTo(MemberRestController.class).withSelfRel());

        //entityModel.add(linkTo(MemberRestController.class).slash(signupResponseDto.getEmail()).withRel("email"));
        return entityModel;
    }

    @PutMapping("/members/{loginId}")
    public EntityModel<ResultResponse> update(@PathVariable String loginId, @RequestBody UpdateMemberDto updateMemberDto) throws Exception{
        MemberResponseDto memberResponseDto = memberAuthenticationServiceImpl.update(loginId, updateMemberDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.USER_UPDATE_SUCCESS, memberResponseDto);

        EntityModel<ResultResponse> entityModel = EntityModel.of(resultResponse);
        entityModel.add(linkTo(MemberRestController.class).withSelfRel());
        //entityModel.add(linkTo(MemberRestController.class).slash(memberResponseDto.getNickname()).withRel("nickname"));
        //entityModel.add(linkTo(MemberRestController.class).slash(memberResponseDto.getPhone()).withRel("phone"));

        return entityModel;
        //return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }

    @GetMapping("/members/{loginId}")
    public EntityModel<ResultResponse> getMemberInfo(@PathVariable String loginId) throws Exception{
        MemberResponseDto memberResponseDto = memberAuthenticationServiceImpl.getMemberInfo(loginId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.GET_MY_INFO_SUCCESS, memberResponseDto);

        EntityModel<ResultResponse> entityModel = EntityModel.of(resultResponse);
        entityModel.add(linkTo(MemberRestController.class).withSelfRel());

        return entityModel;
        //return new ResponseEntity<>(resultResponse, HttpStatus.valueOf(resultResponse.getStatus()));
    }

}
