package com.oceantaker.aicodehelper.ai;

import com.oceantaker.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import reactor.core.publisher.Flux;

import java.util.List;

// @InputGuardrails 注解用于指定输入防护规则，这里使用了 SafeInputGuardrail 类来确保输入安全
@InputGuardrails({SafeInputGuardrail.class})
public interface AiCodeHelperService {

    // 使用 @SystemMessage 注解指定系统提示信息，从 resources 目录下的 system-prompt.txt 文件中读取
    // 该注解支持文件
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    // 大模型返回结构化数据，LangChain4j 自动映射到 Report对象：
    // 可以理解为用方法的方式创建一个类
    record Report(String name, List<String> suggestionList){};


    // 流式对话
    @SystemMessage(fromResource = "system-prompt.txt")
    // 定义流式对话方法，返回 Flux<String> 用于异步流式响应
    // @MemoryId 注解用于标识对话记忆体，int 类型的 memoryId 用于区分不同的对话上下文
    // @UserMessage 注解用于标识用户输入的消息内容
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}