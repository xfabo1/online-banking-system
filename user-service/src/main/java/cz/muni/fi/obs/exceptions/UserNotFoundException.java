package cz.muni.fi.obs.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User with id " + userId + " not found");
    }
}
