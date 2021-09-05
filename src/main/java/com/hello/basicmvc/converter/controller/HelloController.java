package com.hello.basicmvc.converter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request){
        String data = request.getParameter("data"); // 문자타입
        Integer intData = Integer.valueOf(data); // 숫자타입으로 변경
        System.out.println("intData = " + intData);
        return "ok";
    }


    /**
     * 스프링 타입 변환 적용 예
     * 1. @RequestParam
     * 2. @ModelAttribute
     * 3. @PathVariable
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data){
        // 쿼리스트링으로 전달하는 data=10의 10은 숫자가 아닌 문자이다
        // 하지만 @RequestParam을 사용하면 문자 10을 Integer타입의 숫자 10으로 스프링에서 타입 변환을 해주므로 편리하게 받을 수 있다.
        System.out.println("data = " + data);
        return "ok";
    }


}
