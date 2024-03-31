package cz.muni.fi.obs.enums;

import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import lombok.Getter;

@Getter
public enum Nationality {
    CZ,
    SK;

    public static Nationality fromString(String code) {
        for (Nationality nationality : Nationality.values()) {
            if (nationality.toString().equalsIgnoreCase(code.trim())) {
                return nationality;
            }
        }
        throw new ResourceNotFoundException("No nationality with code " + code + " found");
    }
}
