package net.bobdb.fun_with_chatbots;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
class ChatController {

    private final ChatService chatService;

    private final DogRepository dogRepository;

    private final VectorStore vectorStore;

    @Value("classpath:/prompts/dogs.st")
    private Resource doggos;

    @Value("classpath:/docs/names.txt")
    private Resource names;

    @Value("classpath:/prompts/dogs-rag.st")
    private Resource ragPromptTemplate;

    public ChatController(ChatService chatService, DogRepository dogRepository, VectorStore vectorStore) {
       this.chatService = chatService;
       this.dogRepository = dogRepository;
       this.vectorStore = vectorStore;
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

    @GetMapping("/doggiesAI")
    String dogsAI(@RequestParam(value="message", defaultValue = "Can you recommend the cutest dog of all my dogs?") String message) {

        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join("\n", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);

        return chatService.stuffedPrompt(prompt);  // works just as well, terrible name
    }



}
