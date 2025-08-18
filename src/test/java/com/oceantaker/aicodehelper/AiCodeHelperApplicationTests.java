package com.oceantaker.aicodehelper;

import com.oceantaker.aicodehelper.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperApplicationTests {

    /**
     * 注入 AiCodeHelper 实例，用于测试 AI 对话功能
     */
    @Resource
    private AiCodeHelper aiCodeHelper;

    /**
     * 测试与 AI 的基本对话功能
     * 向 AI 发送一条欢迎信息并接收回复
     */
    @Test
    void chat() {
        // 调用 aiCodeHelper 的 chat 方法，传入用户消息
        aiCodeHelper.chat("你好，我是程序员OceanTaker");
    }
    @Test
    void chatWithMassage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://www.codefather.cn/logo.png")
        );
        aiCodeHelper.chatWithMessage(userMessage);

    }
}
