package net.bobdb.ai_doggos;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class RagService {

    private final OpenAiEmbeddingModel embeddingModel;
    private final AtomicReference<SimpleVectorStore> storeRef;

    RagService(OpenAiEmbeddingModel embeddingModel, SimpleVectorStore initial) {
        this.embeddingModel = embeddingModel;
        this.storeRef = new AtomicReference<>(initial);
    }

    VectorStore getVectorStore() {
        return storeRef.get();
    }

    void replaceContent(String text) {
        var resource = new ByteArrayResource(text.getBytes(StandardCharsets.UTF_8));
        var reader = new TextReader(resource);
        List<Document> docs = new TokenTextSplitter().apply(reader.get());
        var newStore = SimpleVectorStore.builder(embeddingModel).build();
        newStore.add(docs);
        storeRef.set(newStore);
    }
}
