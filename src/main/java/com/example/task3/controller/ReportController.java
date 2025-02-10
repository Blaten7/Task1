package com.example.task3.controller;

import com.example.task3.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/daily")
    public ResponseEntity<String> getDailyReport(@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("Access Denied");
        }
        String reportPath = reportService.generateReport();
        return ResponseEntity.ok("Report generated at: " + reportPath);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats(@AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        Map<String, Long> stats = Map.of(
                "newMembers", reportService.getNewMembersCount(),
                "chatCount", reportService.getChatCount()
        );
        return ResponseEntity.ok(stats);
    }
}
