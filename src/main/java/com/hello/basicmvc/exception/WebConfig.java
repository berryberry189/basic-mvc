package com.hello.basicmvc.exception;

import com.hello.basicmvc.exception.filter.LogFilter;
import com.hello.basicmvc.exception.interceptor.LogInterceptor;
import com.hello.basicmvc.exception.resolver.MyHandlerExceptionResolver;
import com.hello.basicmvc.exception.resolver.UserHandlerExceptionResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/*", "*.ico", "/error", "/error-page/**"); // 오류페이지 경로
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers){
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    @Bean
    public FilterRegistrationBean logFilter(){

        // DispatcherType.REQUEST, DispatcherType.ERROR 두가지경우에 호출 됨
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

        return filterRegistrationBean;
    }

    /**
     * [ /hello 정상 요청 ]
     * WAS(/hello, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> View
     * -------------------------------------------------------------------------------
     *
     * [ /error-ex 오류 요청 ]
     * 필터는 DispatchType 으로 중복 호출 제거 ( dispatchType=REQUEST )
     * 인터셉터는 경로 정보로 중복 호출 제거( excludePathPatterns("/error-page/**") )
     * 1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
     * 2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
     * 3. WAS 오류 페이지 확인
     * 4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View
     *
     */

}
