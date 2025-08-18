package com.oceantaker.aicodehelper.ai;


import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiCodeHelper {

    @Resource // 注入 Spring 容器中的 qwenChatModel Bean
    private ChatModel qwenChatModel;

    //和ai简单对话
    public String chat(String message) {
        // 1. 封装用户消息
        UserMessage userMessage = UserMessage.from(message);

        // 2. 调用 AI 模型的chat 方法向模型传入userMessage来向ai发送问题
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);

        // 3. 提取 AI 回复，aiMessage封装了ai的回复内容
        AiMessage aiMessage = chatResponse.aiMessage();

        // 4. 打印日志（Lombok 生成 log 对象）
        log.info("AI 输出：{}", aiMessage.toString());

        // 5. 返回纯文本结果
        return aiMessage.text();
    }

    //和ai简单对话-自定义用户消息
    public String chatWithMessage(UserMessage userMessage) {//userMessage直接自定义
        // 2. 调用 AI 模型的chat 方法向模型传入userMessage来向ai发送问题
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);

        // 3. 提取 AI 回复，aiMessage封装了ai的回复内容
        AiMessage aiMessage = chatResponse.aiMessage();

        // 4. 打印日志（Lombok 生成 log 对象）
        log.info("AI 输出：{}", aiMessage.toString());

        // 5. 返回纯文本结果
        return aiMessage.text();
    }
}
