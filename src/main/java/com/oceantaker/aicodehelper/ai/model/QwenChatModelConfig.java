package com.oceantaker.aicodehelper.ai.model;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration // 声明这是一个配置类，用于定义Spring Bean
@ConfigurationProperties(prefix = "langchain4j.community.dashscope.chat-model") // 绑定配置文件中指定前缀的属性
@Data // Lombok注解，自动生成getter、setter、toString等方法
public class QwenChatModelConfig {

    // 这两个字段由配置文件注入
    private String modelName; // 模型名称配置项

    private String apiKey; // API密钥配置项

    @Resource // 注入ChatModelListener监听器
    private ChatModelListener chatModelListener;

    @Bean // 声明这是一个Bean，返回值将被注册到Spring容器中
    public ChatModel myQwenChatModel() { // 防止与默认的qwen模型冲突另起名
        return QwenChatModel.builder() // 构建QwenChatModel实例
                .apiKey(apiKey) // 设置API密钥
                .modelName(modelName) // 设置模型名称
                .listeners(List.of(chatModelListener)) // 添加监听器
                .build(); // 构建完成并返回实例
    }
}