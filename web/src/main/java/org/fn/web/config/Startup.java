package org.fn.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenshoufeng
 * @since 2026/3/4 下午7:03
 **/
@Configuration
@ComponentScans({
        @ComponentScan("org.fn.application"),
        @ComponentScan("org.fn.core"),
        @ComponentScan("org.fn.persistence"),
        @ComponentScan("org.fn.identity")
})
public class Startup {
}
