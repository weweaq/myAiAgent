package com.example.myaiagent.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RequestMapping("/chat")
@RestController
public class ChatController {

    @Resource
    @Qualifier("openAiChatClient")
    ChatClient openAiChatClient;

    /**
     * 这里简单实现一个接口，让用户输入一个prompt，然后返回一个结果
     *
     * @param message 请求内容
     * @return 结果
     */
    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        String call = openAiChatClient.prompt()
                .user(message)
                .call().content();
        return Map.of("generation", call);
    }

    @GetMapping("/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return openAiChatClient
                .prompt(prompt)
                .stream().content();
    }
}
