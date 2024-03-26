package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.service.CallHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/callhistory")
public class CallHistoryController {
    @Autowired
    private CallHistoryService callHistoryService;

    @PostMapping("/add")
    public String add(@RequestBody CallHistory callHistory)
    {
        callHistoryService.saveCallHistory(callHistory);
        return "New CallHistory added";
    }

    @GetMapping("/today")
    public ResponseEntity<List<CallHistory>> getCallHistoryForToday() {
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForToday();
        return new ResponseEntity<>(callHistoryList, HttpStatus.OK);
    }

    @GetMapping("/today/search")
    public ResponseEntity<List<CallHistory>> searchCallHistory(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        // Get today's date
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<CallHistory> callHistoryList = callHistoryService.getCallHistoryForTodayWithinTimeRange(start, end);
        return ResponseEntity.ok(callHistoryList);
    }
}
