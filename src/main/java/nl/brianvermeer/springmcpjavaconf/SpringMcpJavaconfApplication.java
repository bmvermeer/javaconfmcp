package nl.brianvermeer.springmcpjavaconf;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringMcpJavaconfApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMcpJavaconfApplication.class, args);
    }

    @Bean
    public List<ToolCallback> javaConfTools(ConferenceService conferenceService) {
        return List.of(ToolCallbacks.from(conferenceService));
    }

}
