package com.demo.rampup.common.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zhouyw
 * @date 2022-05-11
 * @describe com.demo.rampup.common.minio
 */
@Data
@Component
public class MinIoClientConfig {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 注入minio 客户端
     * @return
     */
    @Bean
    public MinioClient minioClient(){

        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

}
