package com.inu.go_cagong.admin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드
     * @param file 업로드할 파일
     * @param folder S3 내 폴더명 (예: "cafe")
     * @return 업로드된 파일의 URL
     */
    public String uploadFile(MultipartFile file, String folder) {
        // 원본 파일명
        String originalFilename = file.getOriginalFilename();
        
        // 고유한 파일명 생성 (UUID + 원본 확장자)
        String fileName = folder + "/" + UUID.randomUUID() + "_" + originalFilename;

        try {
            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // S3에 업로드
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
            
            // 업로드된 파일의 URL 반환
            String fileUrl = amazonS3.getUrl(bucket, fileName).toString();
            log.info("파일 업로드 성공: {}", fileUrl);
            
            return fileUrl;
            
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    /**
     * S3에서 파일 삭제
     * @param fileUrl 삭제할 파일의 URL
     */
    public void deleteFile(String fileUrl) {
        try {
            // URL에서 파일명 추출
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            amazonS3.deleteObject(bucket, fileName);
            log.info("파일 삭제 성공: {}", fileName);
            
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }
}
