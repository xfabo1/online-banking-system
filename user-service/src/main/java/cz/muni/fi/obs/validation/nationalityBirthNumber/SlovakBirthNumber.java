package cz.muni.fi.obs.validation.nationalityBirthNumber;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlovakBirthNumber {
    private final String year;
    private final String month;
    private final String day;
    private final String extension;
    private final String controlDigit;

    @Getter
    private Date birthDate;

    public SlovakBirthNumber(String birthNumber) {
        if (birthNumber == null) {
            throw new IllegalArgumentException("Birth number cannot be null");
        }

        Pattern pattern = Pattern.compile("^(\\d\\d)(\\d\\d)(\\d\\d)/(\\d\\d\\d)(\\d?)$");
        Matcher matcher = pattern.matcher(birthNumber);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid birth number format");
        }

        year = matcher.group(1);
        month = matcher.group(2);
        day = matcher.group(3);
        extension = matcher.group(4);
        controlDigit = matcher.group(5);

        if (!isModuloValid()) {
            throw new IllegalArgumentException("Invalid birth number");
        }

        extractBirthDate();
    }

    public boolean has9digits() {
        return controlDigit.isEmpty();
    }

    private boolean isModuloValid() {
        if (has9digits()) {
            return !extension.equals("000");
        }

        int mod9digits = (Integer.parseInt(year + month + day + extension)) % 11;
        if (mod9digits == 10) {
            return controlDigit.equals("0");
        }

        int mod10digits = (Integer.parseInt(year + month + day + extension + controlDigit)) % 11;
        return mod10digits == 0;
    }

    private void extractBirthDate() {
        int yearNum = Integer.parseInt(year);
        int monthNum = Integer.parseInt(month);
        int dayNum = Integer.parseInt(day);

        if (has9digits()) {
            yearNum += 1900;
        } else {
            yearNum += yearNum >= 54 ? 1900 : 2000;
        }

        // female
        if (monthNum > 50) {
            monthNum -= 50;
        }

        // all numbers after slash were already used
        if (yearNum >= 2004 && monthNum > 20) {
            monthNum -= 20;
        }

        try {
            LocalDate date = LocalDate.of(yearNum, monthNum, dayNum);
            if (!date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Birth date is in the future");
            }
            birthDate = java.sql.Date.valueOf(date);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid birth date");
        }
    }


}