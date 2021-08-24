package com.hello.basicmvc.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.basicmvc.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            if(ex instanceof UserException){
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                if("application/json".equals(acceptHeader)){
                    Map<String, Object> errorRequest = new HashMap<>();
                    errorRequest.put("ex", ex.getClass());
                    errorRequest.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorRequest);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);

                    return new ModelAndView();
                }else{
                    // text / html
                    return  new ModelAndView("error/500"); // 뷰 템플릿 호출
                }
            }

        }catch (IOException e){
            log.info("resolver ex", e);
        }

        return null;
    }
}
