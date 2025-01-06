package net.bobdb.fun_with_chatbots;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DogNotFoundException extends RuntimeException {

    public DogNotFoundException(String message) {
            super(message);
        }

    public DogNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
