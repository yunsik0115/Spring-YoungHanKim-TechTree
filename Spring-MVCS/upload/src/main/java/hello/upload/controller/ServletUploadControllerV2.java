package hello.upload.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {


    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}" , request);
        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> parts = request.getParts(); // 여기가 바로 분할점을 기준으로 나뉘는 부분부분들을 모아두는것.
        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name = {}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            // 파츠도 헤더와 바디로 구분, 각각의 그것들을 출력해보자
            for (String headerName : headerNames) {
                log.info("header {} : {}", headerName, part.getHeader(headerName));
            }
            // 헤더와 편의 메서드도 제공됨.
            // 임헤더에서 파일 네임 꺼내기 복잡.... 따라서 편의 메서드 제공!

            log.info("submittedFileName = {}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());
            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);// 항상 뭔가 바뀔때는 형식을 정해야 함.
            log.info("body = {}", body);

            // 파일에 저장하기
            if(StringUtils.hasText(part.getSubmittedFileName())){
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath  : {}", fullPath);
                part.write(fullPath);
            }
        }



        return "upload-form";
    }
}