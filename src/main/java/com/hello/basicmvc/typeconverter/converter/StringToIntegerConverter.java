package com.hello.basicmvc.typeconverter.converter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j                                            // < 파라미터 , 반환값 >
public class StringToIntegerConverter implements Converter<String, Integer> {


    @Override
    public Integer convert(String source) {
        log.info("convert source={}", source);
        return Integer.valueOf(source);
    }
}
