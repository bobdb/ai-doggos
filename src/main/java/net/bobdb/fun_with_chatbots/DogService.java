package net.bobdb.fun_with_chatbots;

import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DogService {

    private final DogRepository dogRepository;

   public  DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public List<Dog> findAll() {
        return dogRepository.findAll();
    }

    //exact
    public List<Dog> findDogsByExample(Dog dog) {
        Example<Dog> example = Example.of(dog);
        return dogRepository.findAll(example);
    }

    //limit one
    Optional<Dog> findOneDogByExample(Dog dog) {
       Example<Dog> example = Example.of(dog);
       return dogRepository.findOne(example);
    }

    //count
    public long countByExample(@Valid Dog dog) {
        Example<Dog> example = Example.of(dog);
        return dogRepository.count(example);
    }

    //exists
    public boolean existsByExample(@Valid Dog dog) {
        Example<Dog> example = Example.of(dog);
        return dogRepository.exists(example);
    }


    //some flexible searching...cool
//            var reply = chatClient.prompt()
//                    .advisors(new QuestionAnswerAdvisor(vectorStore))
//                    .user("do you have any neurotic dogs?")
//                    .call()
//                    .content();

//            System.out.println(reply);

}
