package nl.brianvermeer.springmcpjavaconf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
public class ConferenceService {

    private static final String FEED_URL = "https://javaconferences.org/conferences.json";

    ObjectMapper mapper = new ObjectMapper();

    @Tool(name = "javaConf_get_allEvents", description = "Get all Java conferences from the Java Conferences API both in the past and upcoming.")
    public List<ConferenceJson> getAllConfs() throws IOException {
        return fetchJavaConferences();
    }

    @Tool(name = "javaConf_search_confsByName", description = "Search Java conferences by name.")
    public List<ConferenceJson> getConfsByName(String name) throws IOException {
        return fetchJavaConferences()
                .stream()
                .filter(conf -> conf.name().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Tool(name = "javaConf_search_confsByCountry", description = "Search Java conferences by cpuntry. Country name should be probided in English")
    public List<ConferenceJson> getConfsByCountry(String countryName) throws IOException {
        return fetchJavaConferences()
                .stream()
                .filter(conf -> conf.coordinates().countryName().toLowerCase().contains(countryName.toLowerCase()))
                .toList();
    }

    private List<ConferenceJson> fetchJavaConferences() throws IOException {
        mapper.registerModule(new JavaTimeModule());
        URL url = URI.create(FEED_URL).toURL();
        return mapper.readValue(url, new TypeReference<List<ConferenceJson>>() {});
    }






}
