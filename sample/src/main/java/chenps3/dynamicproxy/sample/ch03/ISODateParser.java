package chenps3.dynamicproxy.sample.ch03;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author chenguanhong
 * @Date 2021/8/18
 */
public interface ISODateParser {

    LocalDate parse(String dateStr) throws ParseException;
}
