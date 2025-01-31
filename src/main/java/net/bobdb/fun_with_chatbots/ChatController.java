package net.bobdb.fun_with_chatbots;

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

    @Value("classpath:/prompts/basicPromptTemplate.st")
    private Resource basicPromptTemplate;

    @Value("classpath:/prompts/RagPromptTemplate.st")
    private Resource ragPromptTemplate;

    @Value("${my.app.localfile}")
    private Resource names;

    public ChatController(ChatService chatService, DogRepository dogRepository, VectorStore vectorStore) {
       this.chatService = chatService;
       this.dogRepository = dogRepository;
       this.vectorStore = vectorStore;
    }

    @PostMapping("/chat")
    String chat(@RequestParam String message) {
        return chatService.prompt(message);
    }

    @GetMapping("/stream")
    Flux<String> chatUsingStream(@RequestParam String message) {
        return chatService.promptWithStream(message);
    }

    @GetMapping("/dogs")
    String dogs(@RequestParam(value="message", defaultValue = "What are the names of my dogs?") String message,
                @RequestParam(value="stuffit", defaultValue = "false") boolean stuffit) {

        var promptTemplate = new PromptTemplate(basicPromptTemplate);
        var map = new HashMap<String, Object>();
        map.put("question", message);
        if (stuffit) {
            map.put("context",names);
        } else {
            map.put("context","");
        }
        Prompt prompt = promptTemplate.create(map);

        return chatService.prompt(prompt);
    }

    @GetMapping("/recommendations")
    String recommendations(@RequestParam(value="message", defaultValue = "Can you recommend the cutest dog of all my dogs?") String message) {

        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join("\n", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);

        return chatService.prompt(prompt);
    }



}
