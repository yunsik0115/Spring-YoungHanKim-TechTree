package hello.typeconverter.controller;

import hello.typeconverter.controller.converter.IntegerToStringConverter;
import hello.typeconverter.controller.converter.IpPortToStringConverter;
import hello.typeconverter.controller.converter.StringToIntegerConverter;
import hello.typeconverter.controller.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }
}
