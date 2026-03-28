package net.bobdb.ai_doggos;

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

    @Value("classpath:/prompts/basicPromptTemplate.st")
    private Resource basicPromptTemplate;

    @Value("${my.app.localfile}")
    private Resource names;

    public ChatController(ChatService chatService, DogRepository dogRepository) {
       this.chatService = chatService;
       this.dogRepository = dogRepository;
    }

    @PostMapping("/chat-blocking")
    String chat(@RequestParam String message) {
        return chatService.prompt(message);
    }

    @GetMapping("/chat-streaming")
    Flux<String> chatUsingStream(@RequestParam String message) {
        return chatService.promptWithStream(message);
    }

    @PostMapping("/chat/reset")
    String resetChat(@RequestParam(defaultValue = "false") boolean stuffit,
                     @RequestParam(required = false, defaultValue = "") String stuffContent) throws Exception {
        chatService.reset();
        if (stuffit) {
            var content = stuffContent.isBlank()
                    ? new String(names.getInputStream().readAllBytes())
                    : stuffContent;
            chatService.seedMemory(content);
            return content;
        }
        return "";
    }

    @GetMapping("/ai/dogs")
    String dogs(@RequestParam(value="message", defaultValue = "What are the names of my dogs?") String message,
                @RequestParam(value="stuffit", defaultValue = "false") boolean stuffit,
                @RequestParam(value="stuffContent", required = false, defaultValue = "") String stuffContent) {

        var promptTemplate = new PromptTemplate(basicPromptTemplate);
        var map = new HashMap<String, Object>();
        map.put("question", message);
        if (stuffit) {
            map.put("context", stuffContent.isBlank() ? names : stuffContent);
        } else {
            map.put("context","");
        }
        Prompt prompt = promptTemplate.create(map);

        return chatService.prompt(prompt);
    }

}
