package com.resumeanalyzer.resumeanalyzerbackend.controller;

import com.resumeanalyzer.resumeanalyzerbackend.entity.ScanHistory;
import com.resumeanalyzer.resumeanalyzerbackend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeResume(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String result = resumeService.analyzeResume(
            request.get("resumeText"), userDetails.getUsername());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ScanHistory>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            resumeService.getScanHistory(userDetails.getUsername()));
    }
}