package hello.typeconverter.converter;

import hello.typeconverter.controller.converter.IntegerToStringConverter;
import hello.typeconverter.controller.converter.IpPortToStringConverter;
import hello.typeconverter.controller.converter.StringToIntegerConverter;
import hello.typeconverter.controller.converter.StringToIpPortConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

public class ConversionServiceTest {

    @Test
    void conversionService(){
        //등록
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new StringToIntegerConverter());
        service.addConverter(new IntegerToStringConverter());
        service.addConverter(new StringToIpPortConverter());
        service.addConverter(new IpPortToStringConverter());

        Integer result = service.convert("10", Integer.class);
        Assertions.assertThat(result).isEqualTo(10);
    }
}
