package com.example.myaiagent;

import com.example.myaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（编程导航）更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮，我想让另一半（编程导航）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithReportCheck() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮";
        LoveApp.LoveReport answer = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（编程导航）更爱我";
        answer = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(answer);
    }

    /**
     * 测试继续聊天 "fc906b2c-e3aa-4067-bfbd-06e06518f2b0"
     */
    @Test
    void doChatWithReportWithContinueChat() {
        String message =  "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        LoveApp.LoveReport answer = loveApp.doChatWithReport(message, "fc906b2c-e3aa-4067-bfbd-06e06518f2b0");
        Assertions.assertNotNull(answer);
    }

}
