package net.bobdb.fun_with_chatbots;

import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
       this.dogService = dogService;
    }

    @GetMapping("")
    List<Dog> findAllDogs() {
        return dogService.findAll();
    }

    @PostMapping("/search/example")
    List<Dog> findDogsByExample(@RequestBody @Valid Dog dog) {
        return dogService.findDogsByExample(dog);
    }

    @PostMapping("/search/example/one")
    Dog findOneDogByExample(@RequestBody @Valid Dog dog) {
        return dogService.findOneDogByExample(dog)
                .orElseThrow(()-> new NoResultException("no doggies found"));
    }

}
