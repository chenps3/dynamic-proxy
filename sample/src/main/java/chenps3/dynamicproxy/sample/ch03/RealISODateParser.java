package chenps3.dynamicproxy.sample.ch03;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @Author chenguanhong
 * @Date 2021/8/19
 */
public class RealISODateParser implements ISODateParser {

    private static final DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate parse(String dateStr) throws ParseException {
        try {
            return LocalDate.parse(dateStr, df);
        } catch (DateTimeParseException e) {
            throw new ParseException(e.toString(), e.getErrorIndex());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
