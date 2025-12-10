package com.example.myaiagent.controller.vo;

import lombok.NonNull;
import org.springframework.ai.chat.memory.ChatMemory;

import lombok.Data;

@Data
public class MemoryChatReq {

    // 不传使用默认值
    private String conversationId = ChatMemory.DEFAULT_CONVERSATION_ID;

    @NonNull
    private String message;
}
