package com.gklx.travel.controller;

import cn.hutool.core.map.MapUtil;
import com.gklx.travel.service.AgentService;
import com.gklx.travel.util.FTUtil;
import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import io.github.imfangs.dify.client.DifyClient;
import io.github.imfangs.dify.client.callback.ChatStreamCallback;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.event.*;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
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
    @Autowired
    private OpenAIClient openAIClient;


    @GetMapping
    public String index() throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "FreeMarker 测试");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        user.put("age", 25);
        user.put("hobbies", new String[]{"看书", "跑步", "编程"});
        dataModel.put("user", user);

        String result = ftUtil.get("demo.ftl", dataModel);
        ChatMessage message = ChatMessage.builder()
                .query("请给我讲一个简短的故事")
                .user("user-123")
                .inputs(MapUtil.<String,Object>builder().build())
                .responseMode(ResponseMode.STREAMING)
                .build();

        difyClient.sendChatMessageStream(message, new ChatStreamCallback() {
            @Override
            public void onMessage(MessageEvent event) {
                System.out.println("收到消息片段: " + event.getAnswer());
            }

            @Override
            public void onAgentMessage(AgentMessageEvent event) {
                System.out.println("收到消息片段: " + event.getAnswer());
            }

            @Override
            public void onMessageEnd(MessageEndEvent event) {
                System.out.println("消息结束，完整消息ID: " + event.getMessageId());
            }

            @Override
            public void onError(ErrorEvent event) {
                System.err.println("错误: " + event.getMessage());
            }



            @Override
            public void onException(Throwable throwable) {
                System.err.println("异常: " + throwable.getMessage());
            }
        });
        return result;
    }

    @GetMapping(value = "/openAi", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> openAi() throws Exception {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("你是谁")
                .model("deepseek-chat")
                .build();
        return Flux.using(
                () -> openAIClient.chat().completions().createStreaming(params),
                stream -> Flux.fromStream(stream.stream())
                        .flatMap(chunk -> Flux.fromIterable(chunk.choices()))
                        .map(choice -> choice.delta().content().orElse(""))
                        .doOnNext(System.out::println),
                StreamResponse::close
        );
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

    }

}
