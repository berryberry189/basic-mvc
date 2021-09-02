package com.hello.basicmvc.exception.exhandler.advice;

import com.hello.basicmvc.exception.exception.UserException;
import com.hello.basicmvc.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    /** 여러 컨트롤러에서 발생하는 에러를 모아
     * ( 대상을 적용하지 않으면 모든 컨트롤러에 적용 )
     * => 대상 컨트롤러 지정 방법
     *  1. @ControllerAdvice(annotations = RestController.class)  : @RestController 붙은 클래스
     *  2. @ControllerAdvice("org.example.controllers") : 패키지 하위 모두 적용
     *  3. @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class}) : 특정 클래스 혹은 부모클래스
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 이걸 안넣으면 200이 떨어지는게 맞음(예외처리를 했기 때문)
    @ExceptionHandler(IllegalArgumentException.class) // Exception.class 여러개 받을 수 있음
    public ErrorResult illegalExHandler(IllegalArgumentException e){ // IllegalArgumentException 와 그 자식까지 잡음
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler // @ExceptionHandler 에 예외를 생략할 수 있다. 생략하면 메서드 파라미터의 예외가 지정된다.
    public ResponseEntity<ErrorResult> userExHandler(UserException e){ // UserException 와 그 자식까지 잡음
        log.error("[exceptionHandler] ex", e);
        return new ResponseEntity<>(new ErrorResult("USER-EX", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){ // 위에서 처리하지 못하는 나머지 예외가 다 넘어옴
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
