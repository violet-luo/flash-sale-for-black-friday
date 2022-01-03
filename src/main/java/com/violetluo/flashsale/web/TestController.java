package com.violetluo.flashsale.web;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("hello")
    public String hello(){
        String result;
        try (Entry entry = SphU.entry("HelloResource")){
            // protected business logic
            result  = "Hello Sentinel";
            return result;
        }catch (BlockException ex) {
            // Resource access blocked, restricted, or downgraded
            log.error(ex.toString());
            result = "System is busy. Try later.";
            return  result;
        }
    }

    /**
     *  Define load limit rules
     *  @PostConstruct
     */
    @PostConstruct
    public void flashsalesFlow(){
        // 1. Create a collection of load limit rules
        List<FlowRule> rules = new ArrayList<>();
        // 2. create load limit rules
        FlowRule rule = new FlowRule();
        // define the resources on which Sentinel will be effective
        rule.setResource("flashsales");
        // define the types of load limit rules and QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // define the number of requests passed by QPS
        rule.setCount(1);

        FlowRule rule2 = new FlowRule();
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setCount(2);
        rule2.setResource("HelloResource");
        // 3. put the load limit rules into the collection
        rules.add(rule);
        rules.add(rule2);
        // 4. add load limit rules
        FlowRuleManager.loadRules(rules);
    }
}