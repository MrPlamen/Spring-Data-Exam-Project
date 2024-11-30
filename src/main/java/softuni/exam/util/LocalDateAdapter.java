package softuni.exam.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public LocalDate unmarshal(String v) {
        return LocalDate.parse(v, formatter);  // Convert String to LocalDate
    }

    @Override
    public String marshal(LocalDate v) {
        return v.format(formatter);  // Convert LocalDate to String
    }
}
