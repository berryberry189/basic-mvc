package com.hello.basicmvc;

import com.hello.basicmvc.exception.filter.LogFilter;
import com.hello.basicmvc.exception.interceptor.LogInterceptor;
import com.hello.basicmvc.exception.resolver.MyHandlerExceptionResolver;
import com.hello.basicmvc.exception.resolver.UserHandlerExceptionResolver;
import com.hello.basicmvc.typeconverter.converter.IntegerToStringConverter;
import com.hello.basicmvc.typeconverter.converter.IpPortToStringConverter;
import com.hello.basicmvc.typeconverter.converter.StringToIntegerConverter;
import com.hello.basicmvc.typeconverter.converter.StringToIpPortConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ===================  Exception 설정 시작  ===================== //
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
     */

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

    // ================= Exception 설정 끝 =================== //

    // ================= Converter 설정 시작 =================== //
    /**
     * 스프링은 기본적으로 수많은 컨버터를 제공하며, 추가한 컨버터는 기본 제공 컨버터 보다 더 높은 우선순위를 가지게 된다.
     */

    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }

    // ================= Converter 설정 끝  =================== //




}
