package com.hello.basicmvc.exception.api;

import com.hello.basicmvc.exception.exception.UserException;
import com.hello.basicmvc.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

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


    @GetMapping("/api/v2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
