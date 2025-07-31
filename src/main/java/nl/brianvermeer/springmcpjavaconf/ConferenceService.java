package nl.brianvermeer.springmcpjavaconf;

import nl.brianvermeer.springmcpjavaconf.javaconfereces.Event;
import nl.brianvermeer.springmcpjavaconf.javaconfereces.JavaConferencesService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConferenceService {

    @Autowired
    JavaConferencesService javaConferencesService;

    @Tool(name = "javaConf_get_allEvents", description = "Get all Java conferences from the Java Conferences API both in the past and upcoming.")
    public List<Event> getAllEvents() throws IOException {
        return javaConferencesService.getAllEvents();
    }

    @Tool(name = "javaConf_get_upcommingEvents", description = "Get all upcoming Java conferences from the Java Conferences API.")
    public List<Event> getUpcomingEvents() throws IOException {
        return javaConferencesService.getUpcomingConferences();
    }

    @Tool(name = "javaConf_get_openCfps", description = "Get all Java conferences that have an open CFP (Call for Papers) from the Java Conferences API.")
    public List<Event> getOpenCfps() throws IOException {
        return javaConferencesService.getAllEvents().stream()
                .filter(event -> event.CfpEndDate() != null && event.CfpEndDate().isAfter(java.time.LocalDate.now()))
                .toList();
    }

    @Tool(name = "javaConf_get_eventsForPeriod", description = "Get all Java conferences for a specific period from the Java Conferences API. Provide start and end dates in the format 'YYYY-MM-DD'.")

    public List<Event> getEventsForPeriod(String startDate, String endDate) throws IOException {
        List<Event> allEvents = javaConferencesService.getAllEvents();
        return allEvents.stream()
                .filter(event -> event.startDate().toLocalDate().isAfter(java.time.LocalDate.parse(startDate)) &&
                                 event.endDate().toLocalDate().isBefore(java.time.LocalDate.parse(endDate)))
                .toList();
    }





}
