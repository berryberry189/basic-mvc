package com.hello.basicmvc.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    /**
     * ExceptionResolver
     * - 상태 코드를 변환할 수 있음
     * - ModelAndView 에 값을 채워서 예외에따른 새로운 오류 화면 뷰를 랜더링해서 고객에게 제공할 수 있음
     * - API 응답 처리 : http 응답 바디에 직접 데이터를 넣어주는 것도 가능
     */

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try{
            if(ex instanceof IllegalArgumentException){ // 예외를 꿀꺽 삼킴
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();// 빈값으로 넘기면 정상적인 흐름으로 was 까지 리턴됨, 빈 값이 아니면 뷰를 랜더링 한다.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;// 예외가 삼켜지 않으며 원래대로 날아감
    }
}
