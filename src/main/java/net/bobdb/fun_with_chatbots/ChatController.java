package net.bobdb.fun_with_chatbots;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@RestController
@CrossOrigin
class ChatController {

    private final ChatService chatService;

    private final DogRepository dogRepository;

    @Value("classpath:/prompts/dogs.st")
    private Resource doggos;

    @Value("classpath:/docs/names.txt")
    private Resource names;

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

    @GetMapping("/doggies")
    String dogs(@RequestParam(value="message", defaultValue = "What are the names of my dogs?") String message,
                @RequestParam(value="stuffit", defaultValue = "false") boolean stuffit) {

        var promptTemplate = new PromptTemplate(doggos);
        var map = new HashMap<String, Object>();
        map.put("question", message);
        if (stuffit) {
            map.put("context",names);
        } else {
            map.put("context","");
        }
        Prompt prompt = promptTemplate.create(map);

        return chatService.stuffedPrompt(prompt);
    }


}
