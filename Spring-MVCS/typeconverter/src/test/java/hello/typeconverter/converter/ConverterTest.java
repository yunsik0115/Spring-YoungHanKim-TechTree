package hello.typeconverter.converter;

import hello.typeconverter.controller.converter.IntegerToStringConverter;
import hello.typeconverter.controller.converter.IpPortToStringConverter;
import hello.typeconverter.controller.converter.StringToIntegerConverter;
import hello.typeconverter.controller.converter.StringToIpPortConverter;
import hello.typeconverter.controller.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Port;

public class ConverterTest {

    @Test
    void stringToInteger(){
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");
        Assertions.assertThat(result).isEqualTo(10);
    }

    @Test
    void integerToString(){
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);
        Assertions.assertThat(result).isEqualTo("10");
    }

    @Test
    void stringToIpPort(){
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);
        Assertions.assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void IpPortToString(){
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";
        IpPort result = converter.convert(source);
        Assertions.assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
        // EqualsAndHashCode -> 내부 필드 다 비교하도록 구현된다. (참조값이 달라도 됨)
    }
}
