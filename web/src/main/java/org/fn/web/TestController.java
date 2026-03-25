package org.fn.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.fn.persistence.result.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chenshoufeng
 * @since 2026/3/6 下午4:32
 **/
@RestController
@RequestMapping("/api")
public class TestController {
        @GetMapping("/test")
    public R<TestModel> test() {
        return R.success(TestModel.builder()
                .id(100L)
                .createdAt(LocalDateTime.now())
                .updatedAt(new Date())
                .build());
    }

    @GetMapping("/test/{id}")
    public String test(@RequestParam Integer id) {
        return "ok";
    }

    @PostMapping("/user")
    public String createUser(@RequestBody @Valid UserDTO user) {
        return "ok";
    }

    @GetMapping("/search")
    public String search(UserDTO query) {
        return "ok";
    }

    @Data
    public static class UserDTO {

        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotNull(message = "{common.r.success}")
        private Integer age;
    }

    @Data
    @Builder
    public static class TestModel {
        private Long id;
        private LocalDateTime createdAt;
        private Date updatedAt;
    }
}
