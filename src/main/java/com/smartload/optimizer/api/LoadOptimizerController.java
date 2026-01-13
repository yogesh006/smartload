package com.smartload.optimizer.api;

import com.smartload.optimizer.model.OptimizeRequest;
import com.smartload.optimizer.model.OptimizeResponse;
import com.smartload.optimizer.service.LoadOptimizerService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/load-optimizer")
public class LoadOptimizerController {

    private final LoadOptimizerService service;

    public LoadOptimizerController(LoadOptimizerService service) {
        this.service = service;
    }

    @PostMapping(value = "/optimize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptimizeResponse> optimize(@Valid @RequestBody OptimizeRequest request,
                                                     @RequestHeader(value = "Content-Length", required = false) String len) {
        // Optional payload guard (simple)
        if (len != null) {
            try {
                long contentLength = Long.parseLong(len);
                if (contentLength > 1024 * 1024) { // 1MB
                    return ResponseEntity.status(413).build();
                }
            } catch (NumberFormatException ignored) {}
        }
        OptimizeResponse resp = service.optimize(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/healthz")
    public String health() {
        return "OK";
    }
}
