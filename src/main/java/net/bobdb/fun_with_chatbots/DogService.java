package net.bobdb.fun_with_chatbots;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }


    public List<Dog> findAll() {
        return dogRepository.findAll();
    }
}
