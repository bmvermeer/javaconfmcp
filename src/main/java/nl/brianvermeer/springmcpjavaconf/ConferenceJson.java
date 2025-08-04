package nl.brianvermeer.springmcpjavaconf;

public record ConferenceJson(
        String name,
        String link,
        String locationName,
        Coordinates coordinates,
        boolean hybrid,
        String date,
        String cfpLink,
        String cfpEndDate
) {
    public static record Coordinates(
            double lat,
            double lon,
            String countryName
    ) {}
}
