package com.resumeanalyzer.resumeanalyzerbackend.service;

import com.resumeanalyzer.resumeanalyzerbackend.entity.ScanHistory;
import com.resumeanalyzer.resumeanalyzerbackend.entity.User;
import com.resumeanalyzer.resumeanalyzerbackend.repository.ScanHistoryRepository;
import com.resumeanalyzer.resumeanalyzerbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private final ScanHistoryRepository scanHistoryRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public String analyzeResume(String resumeText, String email) {
        String prompt = "Analyze this resume and provide detailed feedback on: " +
            "1) Strengths 2) Weaknesses 3) ATS Score out of 100 " +
            "4) Improvements needed 5) Missing keywords\n\nResume:\n" + resumeText;

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("messages", List.of(message));
        requestBody.put("max_tokens", 1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> entity =
            new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://api.groq.com/openai/v1/chat/completions",
            entity, Map.class);

        Map choices = (Map) ((List) response.getBody()
            .get("choices")).get(0);
        String result = (String) ((Map) choices.get("message"))
            .get("content");

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setUser(user);
        scanHistory.setResumeText(resumeText);
        scanHistory.setAnalysisResult(result);
        scanHistoryRepository.save(scanHistory);

        return result;
    }

    public List<ScanHistory> getScanHistory(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return scanHistoryRepository
            .findByUserOrderByScannedAtDesc(user);
    }
}