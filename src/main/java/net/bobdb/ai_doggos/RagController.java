package net.bobdb.ai_doggos;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
class RagController {

    private final RagService ragService;
    private final ChatService chatService;

    @Value("classpath:/prompts/RagPromptTemplate.st")
    private Resource ragPromptTemplate;

    RagController(RagService ragService, ChatService chatService) {
        this.ragService = ragService;
        this.chatService = chatService;
    }

    @GetMapping("/recommendations")
    String recommendations(
            @RequestParam(value = "message", defaultValue = "Can you recommend the cutest dog?")
            String message) {
        List<Document> similar = ragService.getVectorStore()
                .similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        List<String> content = similar.stream().map(Document::getText).toList();
        var template = new PromptTemplate(ragPromptTemplate);
        var params = new HashMap<String, Object>();
        params.put("input", message);
        params.put("documents", String.join("\n", content));
        Prompt prompt = template.create(params);
        return chatService.prompt(prompt);
    }

    @PostMapping("/rag/upload")
    ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("No file provided");
        try {
            String text = new String(file.getBytes(), StandardCharsets.UTF_8);
            ragService.replaceContent(text);
            return ResponseEntity.ok("Vector store replaced with: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
