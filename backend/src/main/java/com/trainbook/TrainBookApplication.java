package com.trainbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrainBookApplication {
    public static void main(String[] args) { SpringApplication.run(TrainBookApplication.class, args); }

    @Bean
    CommandLineRunner seed(TrainRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(new Train("Rajdhani Express", "Jaipur", "Delhi", "06:00", "10:30", 650, 60));
            repo.save(new Train("Shatabdi Express", "Jaipur", "Delhi", "17:45", "22:10", 780, 45));
            repo.save(new Train("Ajmer Shatabdi", "Delhi", "Jaipur", "06:05", "10:40", 700, 50));
            repo.save(new Train("Mumbai Superfast", "Jaipur", "Mumbai", "15:20", "08:40", 1450, 80));
            repo.save(new Train("Pink City Express", "Delhi", "Jaipur", "19:30", "00:15", 480, 70));
            repo.save(new Train("Deccan Queen", "Mumbai", "Pune", "07:10", "10:25", 390, 90));
        };
    }
}
