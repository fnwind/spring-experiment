# spring-experiment

对近年来基于 Spring Boot 开发的技术总结，同时也是对 Spring Boot 4.x 版本的一次探索。

会先尝试以单体项目的结构写出第一版，主要是回忆和总结，然后用 spring-boot-starter 重构成可用的开发框架。

## v1 - 单体应用

| 层级 | 功能 |
| -- | -- |
| web | 面向用户层 |
| application | 业务代码层，大概会做一个基础的后台管理 |
| identity | 应该会基于 OAuth2.0 编写认证模块 |
| persistence | 持久化层，包括数据库、MinIO 和 Redis 缓存 |
| core | 基础层，实体、注解、工具类 |
