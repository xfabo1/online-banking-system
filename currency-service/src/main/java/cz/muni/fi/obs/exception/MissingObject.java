package cz.muni.fi.obs.exception;

import cz.muni.fi.obs.domain.DomainObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;


/**
 * Exception for missing database objects
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MissingObject extends RuntimeException {

    private final Class<? extends DomainObject> objectClass;

    private final String identifier;


    public MissingObject(Class<? extends DomainObject> objectClass, String identifier) {
        this.objectClass = objectClass;
        this.identifier = identifier;
    }

    @Override
    public String getMessage() {
        return String.format("Missing object with identifier: %s, of class: %s ", identifier, objectClass.getSimpleName());
    }
}
