package net.bobdb.fun_with_chatbots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("classpath:/docs/doggiedetails.txt")
    private Resource names;

    @Value("vectorStore.json")
    private String vectorStoreName;

    @Bean
    SimpleVectorStore simpleVectorStore(OpenAiEmbeddingModel model) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(model);
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {
            log.info("vector store file exists.   loading...");
            simpleVectorStore.load(vectorStoreFile);
        } else {
            log.info("vector store DNE.  generating...");
            var textReader = new TextReader(names);
            textReader.getCustomMetadata().put("filename","doggiedetails.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);

            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);

        }

        return simpleVectorStore;

    }

    private File getVectorStoreFile() {
        var path = Paths.get("src","main","resources","data");
        var absPath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absPath);
    }


}
