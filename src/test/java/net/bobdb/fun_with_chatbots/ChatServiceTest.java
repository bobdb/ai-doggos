package net.bobdb.fun_with_chatbots;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatServiceTest {


    @Autowired
    ChatService chatService;

    @Test
    void testPrompt() {

        var prompt = """
                List five random Spring Annotations with brief explanations.
                """;

        var reply = chatService.prompt(prompt);

        System.out.println("reply: [" + reply + "]");
    }
}
