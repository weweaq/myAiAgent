package com.example.myaiagent.controller;

import com.example.myaiagent.advisor.MySimpleLoggerAdvisor;
import com.example.myaiagent.chatmemory.MysqlMemory;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RequestMapping("/chat")
@RestController
public class ChatController {

    @Resource
    @Qualifier("openAiChatClient")
    ChatClient openAiChatClient;

    @Autowired
    MysqlMemory mysqlMemory;

    /**
     * 这里简单实现一个接口，让用户输入一个prompt，然后返回一个结果
     *
     * @param message 请求内容
     * @return 结果
     */
    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        String call = openAiChatClient.prompt().user(message).call().content();
        return Map.of("generation", call);
    }

    @GetMapping("/ai/generateStream")
    public Flux<String>
        generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return openAiChatClient.prompt(prompt).stream().content();
    }

    /**
     * 返回实体类
     *
     * @param message 请求内容
     * @return 结果
     */
    @GetMapping("/ai/film/generate")
    public Map<String, List<ActorFilms>>
        generateFilm(@RequestParam(value = "message", defaultValue = "成龙") String message) {
        ActorFilms actorFilm = openAiChatClient.prompt().advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
            .user(message).call().entity(ActorFilms.class);

        List<ActorFilms> actorFilms = openAiChatClient.prompt().system(s -> s.text("You are a helpful assistant."))
            .user(u -> u.text("Tell me the names of 5 movies whose soundtrack was composed by {composer}")
                .param("composer", message))
            .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
            // 自定义的日志log，其实不好用，还得每个chatClient里用
            // 或许加到默认的advisor里也是个好选择, 使用around切面打日志才是正常手段
            .advisors(new MySimpleLoggerAdvisor()).call().entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
        return Map.of("generation", actorFilms);
    }

    record ActorFilms(String actor, List<String> movies) {
    }

    // TODO 希望能做到 1.指定使用哪段记忆 2. 对于过长的记忆能够抽取摘要
    @GetMapping("/ai/memory/chat")
    public Map<String, String> chatWithMemory(@RequestParam(value = "message", defaultValue = "介绍自己") String message) {
        String content = openAiChatClient.prompt().system(s -> s.text("你是一个乐于助人的，热心，活泼，思想活跃的古风小生.")).user(message)
            .advisors(new MySimpleLoggerAdvisor(),
                // 记忆，使用的默认memoryId
                MessageChatMemoryAdvisor.builder(mysqlMemory.getChatMemory()).build())
            .call().content();
        return Map.of("generation", content);
    }
}
