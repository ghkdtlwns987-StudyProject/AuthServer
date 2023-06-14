package com.auth.studyprojectauthserver.Domain.Member.Controller;

import com.auth.studyprojectauthserver.Domain.Member.Dto.AuthorizationResopnseDto;
import com.auth.studyprojectauthserver.Domain.Member.Dto.ResponseDto;
import com.auth.studyprojectauthserver.Domain.Member.Service.inter.AuthorizationService;
import com.auth.studyprojectauthserver.Global.Error.Exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인가 처리를 담당하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/authorization")
public class AuthorizationController {
    private static final String BEARER_PREFIX = "Bearer ";
    private final AuthorizationService authorizationService;

    @GetMapping(headers = "Authorization")
    public ResponseDto<AuthorizationResopnseDto> authorization(@RequestHeader(name = "Authorization") String authorization){
        AuthorizationResopnseDto authorizationResopnseDto = authorizationService.authorization(
                removeTokenPrefix(authorization));

        return ResponseDto.<AuthorizationResopnseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(authorizationResopnseDto)
                .build();
    }

    private String removeTokenPrefix(String tokenWithPrefix){
        if(!tokenWithPrefix.startsWith(BEARER_PREFIX)){
            throw new InvalidTokenException(tokenWithPrefix);
        }

        return tokenWithPrefix.substring(BEARER_PREFIX.length());
    }
}
