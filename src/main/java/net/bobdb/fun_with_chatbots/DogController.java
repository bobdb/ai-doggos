package net.bobdb.fun_with_chatbots;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
       this.dogService = dogService;
    }

    @GetMapping("/")
    List<Dog> getAllDogs() {
        return dogService.findAll();
    }

}
