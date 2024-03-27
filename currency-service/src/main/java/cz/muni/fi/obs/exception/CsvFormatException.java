package cz.muni.fi.obs.exception;

import com.opencsv.exceptions.CsvValidationException;

public class CsvFormatException extends RuntimeException {

    private final CsvValidationException csvValidationException;

    public CsvFormatException(CsvValidationException csvValidationException) {
        super(csvValidationException.getCause());
        this.csvValidationException = csvValidationException;
    }

    @Override
    public String getMessage() {
        return csvValidationException.getMessage();
    }
}
