package nl.brianvermeer.springmcpjavaconf.javaconfereces;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Event (
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String URL,
        String location,
        Format format,
        String CfpUrl,
        LocalDate CfpEndDate
) {}

