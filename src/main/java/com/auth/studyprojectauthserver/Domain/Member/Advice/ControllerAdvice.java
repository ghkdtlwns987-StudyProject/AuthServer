package com.auth.studyprojectauthserver.Domain.Member.Advice;

import com.auth.studyprojectauthserver.Domain.Member.Dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 예외 처리를 위한 Controller Advice입니다.
 * @author : 황시준
 * @since : 1.0
 */
@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDto.builder()
                        .success(false)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .errorMessages(List.of(e.getMessage()))
                        .build()
        );
    }
}
