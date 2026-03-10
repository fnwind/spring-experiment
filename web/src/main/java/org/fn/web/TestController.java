package org.fn.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chenshoufeng
 * @since 2026/3/6 下午4:32
 **/
@RestController
public class TestController {
    @GetMapping("/api/test")
    public ResponseEntity<TestModel> test() {
        return ResponseEntity.ok(TestModel.builder()
                .id(100L)
                .createdAt(LocalDateTime.now())
                .updatedAt(new Date())
                .build());
    }

    @Data
    @Builder
    public static class TestModel {
        private Long id;
        private LocalDateTime createdAt;
        private Date updatedAt;
    }
}
