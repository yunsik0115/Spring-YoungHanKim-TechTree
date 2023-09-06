package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;

    // 사용자 두명이 같은 파일이름으로 저장한다고 가정할때 storeFileName은 uuid와 같은걸로 안겹치게 만들어낼것임.


    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
