package net.bobdb.fun_with_chatbots;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
class ChatController {

    private final ChatService chatService;

    private final DogRepository dogRepository;

    public ChatController(ChatService chatService, DogRepository dogRepository) {
       this.chatService = chatService;
       this.dogRepository = dogRepository;
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
