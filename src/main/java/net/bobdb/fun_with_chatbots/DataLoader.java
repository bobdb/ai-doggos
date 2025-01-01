package net.bobdb.fun_with_chatbots;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    ApplicationRunner loadData() {
            return args-> {
//            dogRepository.findAll().forEach(
//                    dog -> {
//                        var doc = new Document("id: %s, name: %s, description: %s"
//                                .formatted(dog.id(), dog.name(), dog.decription()));
//                        vectorStore.add(List.of(doc));
//                    });
//            var reply = chatClient.prompt()
//                    .advisors(new QuestionAnswerAdvisor(vectorStore))
//                    .user("do you have any neurotic dogs?")
//                    .call()
//                    .content();

//            System.out.println(reply);
        };
    }




}
