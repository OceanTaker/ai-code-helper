package com.oceantaker.aicodehelper.ai;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel myQwenChatModel;

    @Resource
    private ContentRetriever contentRetriever; // retriever 检索器

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private StreamingChatModel qwenStreamingChatModel;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        // 创建一个基于消息数量的会话记忆，最多保留10条消息
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // 使用AiServices.builder构建AiCodeHelperService实例
        // 配置聊天模型和聊天记忆
        // 1. 使用AiServices.builder创建一个构建器，指定服务接口为AiCodeHelperService
        // 2. 设置聊天模型为已注入的qwenChatModel
        // 3. 设置聊天记忆为上面创建的MessageWindowChatMemory实例
        // 4. 调用build()方法完成构建，得到AiCodeHelperService实例
        AiCodeHelperService aiCodeHelperService = AiServices.builder(AiCodeHelperService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel) // 流式输出
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(10)) // 每个会话独立存储
//                .chatMemory(chatMemory) //会话记忆
                .contentRetriever(contentRetriever) // RAG增强检索生成
                .toolProvider(mcpToolProvider) // MCP 工具调用
                .build();

        return aiCodeHelperService;
    }
}
