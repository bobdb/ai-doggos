package net.bobdb.fun_with_chatbots;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
       this.chatService = chatService;
    }

    @GetMapping("/hello")
    String hello() {
        return "hello";
    }

    @PostMapping("/chat")
    String chat(@RequestParam String message) {
        return chatService.prompt(message);
    }

    @GetMapping("/stream")
    Flux<String> chatUsingStream(@RequestParam String message) {
        return chatService.promptWithStream(message);
    }

}
