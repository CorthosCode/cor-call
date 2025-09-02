package ru.corthos.call;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/signal")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SignalingController {

    private final SignalingService signalingService;

    @GetMapping("/offer/{callId}")
    public String getOffer(@PathVariable String callId) {
        return signalingService.getOffer(callId);
    }

    @PostMapping("/offer/{callId}")
    public void setOffer(@PathVariable String callId,
                         @RequestBody String offer) {
        signalingService.saveOffer(callId, offer);
    }

    @GetMapping("/answer/{callId}")
    public String getAnswer(@PathVariable String callId) {
        return signalingService.getAnswer(callId);
    }

    @PostMapping("/answer/{callId}")
    public void setAnswer(@PathVariable String callId,
                          @RequestBody String answer) {
        signalingService.saveAnswer(callId, answer);
    }

    @GetMapping("/ice-candidates/{callId}")
    public List<Map<String, Object>> getIceCandidates(@PathVariable String callId) {
        return signalingService.getIceCandidates(callId);
    }

    @PostMapping("/ice-candidate/{callId}")
    public void addIceCandidate(@PathVariable String callId,
                                @RequestBody Map<String, Object> candidate) {
        signalingService.saveIceCandidate(callId, candidate);
    }

    @DeleteMapping("/cleanup/{callId}")
    public void cleanup(@PathVariable String callId) {
        signalingService.clearCallData(callId);
    }
}