package com.resumeanalyzer.resumeanalyzerbackend.repository;

import com.resumeanalyzer.resumeanalyzerbackend.entity.ScanHistory;
import com.resumeanalyzer.resumeanalyzerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {
    List<ScanHistory> findByUserOrderByScannedAtDesc(User user);
}