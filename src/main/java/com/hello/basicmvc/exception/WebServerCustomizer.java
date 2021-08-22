package com.hello.basicmvc.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    /**
     * 예외 발생과 오류 페이지 요청 흐름
     * 1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
     * 2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error- page/500) -> View
     *                              ㄴ 오류 페이지 경로로 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출
     *  중요한 점은 웹 브라우저(클라이언트)는 서버 내부에서 이런 일이 일어나는지 전혀 모른다는 점이다.
     *  오직 서버 내부에서 오류 페이지를 찾기 위해 추가적인 호출을 한다.
     *
     */

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

       ErrorPage errorPage404 =  new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
       ErrorPage errorPage500 =  new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

                                            // RuntimeException 또는 그 자식타입의 예외
       ErrorPage errorPageEx =  new ErrorPage(RuntimeException.class, "/error-page/500");

       factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
