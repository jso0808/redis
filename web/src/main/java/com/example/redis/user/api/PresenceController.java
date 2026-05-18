package com.example.redis.user.api;

import com.example.redis.user.application.PresenceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regions/{regionCode}/presence")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @org.springframework.web.bind.annotation.PostMapping("/join")
    public ResponseEntity<Void> join(@PathVariable String regionCode, @Valid @RequestBody PresenceRequest request) {
        presenceService.join(regionCode, request.userId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leave(@PathVariable String regionCode, @Valid @RequestBody PresenceRequest request) {
        presenceService.leave(regionCode, request.userId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public PresenceCountResponse count(@PathVariable String regionCode) {
        return new PresenceCountResponse(presenceService.onlineCount(regionCode));
    }

    public record PresenceRequest(@NotBlank String userId) {
    }

    public record PresenceCountResponse(long count) {
    }
}
