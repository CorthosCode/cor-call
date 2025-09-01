package ru.corthos.call;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/signal")
@CrossOrigin(origins = "*")
public class SignalingController {

    private final ConcurrentHashMap<String, String> offers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> answers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> iceCandidates = new ConcurrentHashMap<>();


    @GetMapping("/offer/{callId}")
    public String getOffer(@PathVariable String callId) {
        return offers.get(callId);
    }

    @PostMapping("/offer/{callId}")
    public void setOffer(@PathVariable String callId, @RequestBody String offer) {
        offers.put(callId, offer);
        System.out.println("AUDIO Offer received for call: " + callId);
    }

    @GetMapping("/answer/{callId}")
    public String getAnswer(@PathVariable String callId) {
        return answers.get(callId);
    }

    @PostMapping("/answer/{callId}")
    public void setAnswer(@PathVariable String callId, @RequestBody String answer) {
        answers.put(callId, answer);
        System.out.println("AUDIO Answer received for call: " + callId);
    }

    @GetMapping("/ice-candidates/{callId}")
    public Map<String, Object> getIceCandidates(@PathVariable String callId) {
        return iceCandidates.getOrDefault(callId, Map.of());
    }

    @PostMapping("/ice-candidate/{callId}")
    public void addIceCandidate(@PathVariable String callId, @RequestBody Map<String, Object> candidate) {
        iceCandidates.put(callId, candidate);
        System.out.println("ICE candidate received for call: " + callId);
    }

}