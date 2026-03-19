package org.fn.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chenshoufeng
 * @since 2026/3/19 上午10:14
 **/
@Component
public class NacosTest {
    @Value("${foo:foo}")
    private String foo;
    @Value("${bar:bar}")
    private String bar;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        System.out.println("NacosTest onReady");
        System.out.println("foo = " + foo);
        System.out.println("bar = " + bar);
    }
}
