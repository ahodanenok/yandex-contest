package ahodanenok.yandex.contest.cup2020.backend.qualification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * YandexCup2020.Backend.Qualification.A
 */
public class A {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        Period period = Period.valueOf(reader.readLine());
        String[] datesStr = reader.readLine().split(" ");
        LocalDate fromDate = LocalDate.parse(datesStr[0]);
        LocalDate toDate = LocalDate.parse(datesStr[1]);

        List<String> periods = new ArrayList<>();
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            LocalDate periodEndDate = period.endDate(currentDate);
            if (periodEndDate.isAfter(toDate)) {
                periodEndDate = toDate;
            }

            periods.add(currentDate + " " + periodEndDate);
            currentDate = periodEndDate.plusDays(1);
        }

        System.out.println(periods.size());
        periods.forEach(System.out::println);
    }

    public enum Period {

        WEEK {
            @Override
            LocalDate endDate(LocalDate date) {
                return date.plusDays(7 - date.getDayOfWeek().getValue());
            }
        },
        MONTH {
            @Override
            LocalDate endDate(LocalDate date) {
                return date.withDayOfMonth(date.lengthOfMonth());
            }
        },
        QUARTER {
            @Override
            LocalDate endDate(LocalDate date) {
                int quarter = (date.getMonthValue() - 1) / 3;
                int endMonth = (quarter + 1) * 3;
                LocalDate endDate = date.plusMonths(endMonth - date.getMonthValue());
                return endDate.withDayOfMonth(endDate.lengthOfMonth());
            }
        },
        YEAR {
            @Override
            LocalDate endDate(LocalDate date) {
                return date.withDayOfYear(date.lengthOfYear());
            }
        },
        REVIEW {
            @Override
            LocalDate endDate(LocalDate date) {
                if (date.getMonthValue() >= Month.APRIL.getValue() && date.getMonthValue() <= Month.SEPTEMBER.getValue()) {
                    return LocalDate.of(date.getYear(), Month.SEPTEMBER, 30);
                } else {
                    int year = date.getYear();
                    if (date.getMonth().getValue() >= Month.OCTOBER.getValue()) {
                        year++;
                    }

                    return LocalDate.of(year, Month.MARCH, 31);
                }
            }
        };

        abstract LocalDate endDate(LocalDate date);
    }
}
