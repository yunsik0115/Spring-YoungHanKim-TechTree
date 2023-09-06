package hello.typeconverter.controller.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

//127.0.0.1:8080-> Converter를 이용해서 IP랑 포트 분리
@Getter
@EqualsAndHashCode
public class IpPort {
    private String ip;
    private int port;

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
