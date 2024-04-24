package cz.muni.fi.obs.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Nationality {
    CZ,
    SK
}
