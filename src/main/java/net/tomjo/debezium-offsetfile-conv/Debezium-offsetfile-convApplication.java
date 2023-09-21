package net.tomjo.debezium-offsetfile-conv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class Debezium-offsetfile-convApplication {

    public static void main(String[] args) {
        SpringApplication.run(Debezium-offsetfile-convApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
