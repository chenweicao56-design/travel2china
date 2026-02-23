package com.gklx.travel.controller;

import com.gklx.travel.service.AgentService;
import com.gklx.travel.util.FTUtil;
import io.github.imfangs.dify.client.DifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseController {

    @Autowired
    private DifyClient difyClient;

    @Autowired
    private FTUtil ftUtil;

    @Autowired
    private AgentService agentService;


    @GetMapping
    public String index() throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "FreeMarker 测试");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        user.put("age", 25);
        user.put("hobbies", new String[]{"看书", "跑步", "编程"});
        dataModel.put("user", user);

        // 生成字符串
        String result = ftUtil.get("demo.ftl", dataModel);
        return result;
    }

    @PostMapping(value = "/run", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> run() {
        // 1. 基础示例：每秒推送一条数据，共推送10条
        return Flux.interval(Duration.ofSeconds(1)) // 每秒生成一个递增的数字（0,1,2...）
                .take(10) // 只取前10条，避免无限流
                .map(count -> "流式输出内容: " + (count + 1)) // 转换为业务数据
                .doOnNext(data -> System.out.println("准备推送: " + data)) // 日志监控
                .doOnComplete(() -> System.out.println("流式输出完成")) // 完成回调
                .onErrorReturn("推送异常: 数据发送失败"); // 异常兜底

        // 2. 如果需要先执行前置逻辑（如初始化），再返回流式数据，可以用 concatWith
        // return Mono.fromRunnable(() -> {
        //     // 前置同步/异步逻辑（如参数校验、资源初始化）
        //     System.out.println("开始执行流式任务...");
        // }).thenMany(
        //     Flux.interval(Duration.ofSeconds(1))
        //         .take(10)
        //         .map(count -> "流式输出内容: " + (count + 1))
        // );
    }

}
