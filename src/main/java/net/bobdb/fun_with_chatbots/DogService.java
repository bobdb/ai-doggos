package net.bobdb.fun_with_chatbots;

import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
@Transactional(readOnly = true)
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
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

    //custom match paramteters
    public List<Dog> findDogsWithCustomSearch(String name, String breed, String description) {
        Dog d = new Dog();
        d.setName(name);
        d.setBreed(breed);
        d.setDescription(description);

        ExampleMatcher matcher = matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();

        Example<Dog> example = Example.of(d, matcher);
        return dogRepository.findAll(example);
    }

}
