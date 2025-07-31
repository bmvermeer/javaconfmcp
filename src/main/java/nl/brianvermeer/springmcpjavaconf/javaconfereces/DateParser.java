package nl.brianvermeer.springmcpjavaconf.javaconfereces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateParser {

    private static final Logger Log = LoggerFactory.getLogger(DateParser.class);

    private DateParser() {}

    // Define various date formats
    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("d MMMM yyyy"),
            DateTimeFormatter.ofPattern("d MMM yyyy"),
            DateTimeFormatter.ofPattern("MMM d yyyy"),
            DateTimeFormatter.ofPattern("MMMM d yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public static LocalDateTime[] parseDateRange(String dateString) {
        var cleanDateString = removeOrdinals(removePrefix(dateString));  // Handle ordinals

        for (var formatter : DATE_FORMATS) {
            try {
                if (cleanDateString.matches("\\d{1,2} \\w+[-–]\\d{1,2} \\w+ \\d{4}")) {
                    return parseRangeWithDifferentMonths(cleanDateString, formatter);
                } else if (cleanDateString.matches("\\d{1,2}[-–]\\d{1,2} .* \\d{4}")) {
                    return parseRangeWithSameMonth(cleanDateString, formatter);
                } else if (cleanDateString.matches(".* \\d{1,2}[-–]\\d{1,2},? \\d{4}")) {
                    return parseRangeWithMonthAndYear(cleanDateString, formatter);
                } else if (cleanDateString.contains("-")) {
                    return parseSimpleRange(cleanDateString, formatter);
                } else {  // Single date
                    return parseSingleDate(cleanDateString, formatter);
                }
            } catch (DateTimeParseException ignored) {
                // Continue to the next format
            }
        }
        Log.error("Unknown date format: {}", dateString);
        return new LocalDateTime[]{
                null,
                null
        };
    }

    private static LocalDateTime[] parseRangeWithDifferentMonths(String dateString, DateTimeFormatter formatter) {
        var parts = dateString.split("[-–]", 2);
        var startPart = parts[0].trim();
        var endPart = parts[1].trim();

        var startParts = startPart.split(" ", 2);
        var startDay = Integer.parseInt(startParts[0].trim());
        var startMonth = startParts[1].trim();

        var endParts = endPart.split(" ", 2);
        var endDay = Integer.parseInt(endParts[0].trim());
        var endMonthAndYear = endParts[1].trim();

        var startDate = LocalDate.parse(startDay + " " + startMonth + " " + endMonthAndYear.split(" ")[1], formatter);
        var endDate = LocalDate.parse(endDay + " " + endMonthAndYear, formatter);

        return new LocalDateTime[]{
                startDate.atStartOfDay(),
                endDate.atStartOfDay()
        };
    }

    private static LocalDateTime[] parseRangeWithSameMonth(String dateString, DateTimeFormatter formatter) {
        var parts = dateString.split("[-–]", 2);
        var startDay = Integer.parseInt(parts[0].trim());

        var remainingParts = parts[1].trim().split(" ", 2);
        var endDay = Integer.parseInt(remainingParts[0].trim());
        var monthAndYear = remainingParts[1].trim();

        var startDate = LocalDate.parse(startDay + " " + monthAndYear, formatter);
        var endDate = LocalDate.parse(endDay + " " + monthAndYear, formatter);

        return new LocalDateTime[]{
                startDate.atStartOfDay(),
                endDate.atStartOfDay()
        };
    }

    private static LocalDateTime[] parseRangeWithMonthAndYear(String dateString, DateTimeFormatter formatter) {
        var parts = dateString.split("[-–]", 2);
        var month = parts[0].trim().split(" ")[0];
        var startDay = Integer.parseInt(parts[0].trim().split(" ")[1]);

        var remainingParts = parts[1].trim().split(" ", 2);
        var endDay = Integer.parseInt(remainingParts[0].trim().replace(",", ""));
        var year = remainingParts[1].trim();

        var startDate = LocalDate.parse(month + " " + startDay + " " + year, formatter);
        var endDate = LocalDate.parse(month + " " + endDay + " " + year, formatter);

        return new LocalDateTime[]{
                startDate.atStartOfDay(),
                endDate.atStartOfDay()
        };
    }

    private static LocalDateTime[] parseSimpleRange(String dateString, DateTimeFormatter formatter) {
        var parts = dateString.split("-", 2);
        var startDateString = parts[0].trim();
        var endDateString = parts[1].trim();

        var startDate = LocalDate.parse(startDateString, formatter);
        var endDate = LocalDate.parse(endDateString, formatter);

        return new LocalDateTime[]{
                startDate.atStartOfDay(),
                endDate.atStartOfDay()
        };
    }

    private static LocalDateTime[] parseSingleDate(String dateString, DateTimeFormatter formatter) {
        var singleDate = LocalDate.parse(dateString.trim(), formatter);
        return new LocalDateTime[]{
                singleDate.atStartOfDay(),
                singleDate.atStartOfDay()
        };
    }

    // Helper method to remove ordinal suffixes
    private static String removeOrdinals(String dateStr) {
        return dateStr.replaceAll("(?<=\\d)(st|nd|rd|th)", "")
                .replace(",", "") // Remove commas
                .replace("–", "-") // Replace en dash with hyphen
                .replace(" - ", "-"); // Remove spaces around hyphens
    }

    private static String removePrefix(String dateStr) {
        // Remove prefix "around " if it exists
        return dateStr.replace("around ", "").trim();
    }
}