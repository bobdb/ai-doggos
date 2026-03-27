package net.bobdb.ai_doggos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ChatServiceTest {

    @MockBean
    SimpleVectorStore simpleVectorStore;

    @Autowired
    ChatService chatService;

    @Test
    @EnabledIfEnvironmentVariable(named = "SPRING_AI_OPENAI_API_KEY", matches = ".+")
    void testPrompt() {

        var prompt = """
                List five random Spring Annotations with brief explanations.
                """;

        var reply = chatService.prompt(prompt);

        System.out.println("reply: [" + reply + "]");
    }
}
