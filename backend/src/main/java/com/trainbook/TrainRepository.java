package com.trainbook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findByFromStationContainingIgnoreCaseAndToStationContainingIgnoreCase(String from, String to);
}
