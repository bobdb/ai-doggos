package net.bobdb.ai_doggos;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final MessageWindowChatMemory memory;

    public ChatService(ChatClient.Builder builder) {
        this.memory = MessageWindowChatMemory.builder().build();
        this.chatClient = builder
                .defaultSystem("""
                        You are a dog expert assistant for the AI Doggos pet shop.
                        You only answer questions about dogs: breeds, behavior, care, health, and training.
                        You can also answer questions about the specific dogs available in this shop's database.
                        For any question unrelated to dogs, politely decline and invite the user to ask a dog-related question instead.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }

    public void reset() {
        memory.clear("default");
    }

    public void seedMemory(String content) {
        memory.add("default", new UserMessage(content));
    }

    public String prompt(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String prompt(Prompt prompt) {
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    public Flux<String> promptWithStream(String message) {
            return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
