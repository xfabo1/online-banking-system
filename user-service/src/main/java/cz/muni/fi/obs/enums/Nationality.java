package cz.muni.fi.obs.enums;

import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import lombok.Getter;

@Getter
public enum Nationality {
    CZECH("CZ"),
    SLOVAK("SK");

    private final String code;

    Nationality(String code) {
        this.code = code;
    }

    public static Nationality fromCode(String code) {
        for (Nationality nationality : Nationality.values()) {
            if (nationality.getCode().equalsIgnoreCase(code.trim())) {
                return nationality;
            }
        }
        throw new ResourceNotFoundException("No nationality with code " + code + " found");
    }
}
