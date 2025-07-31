package nl.brianvermeer.springmcpjavaconf.javaconfereces;

public enum Format {
    IN_PERSON("In-person"),
    VIRTUAL("Virtual"),
    HYBRID("Hybrid");

    public final String label;

    Format(String label) {
        this.label = label;
    }
}
