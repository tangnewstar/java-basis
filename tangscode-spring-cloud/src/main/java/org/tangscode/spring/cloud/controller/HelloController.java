package org.tangscode.spring.cloud.controller;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.AbstractSentinelInterceptor;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/3/27
 */
@RestController
public class HelloController {

    @SentinelResource
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
