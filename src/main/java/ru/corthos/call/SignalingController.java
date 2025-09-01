package ru.corthos.call;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/signal")
@CrossOrigin(origins = "*")
public class SignalingController {

    private final ConcurrentHashMap<String, String> offers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> answers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Map<String, Object>>> iceCandidates = new ConcurrentHashMap<>();


    // Offer от Caller'а
    @PostMapping("/offer/{callId}")
    public void setOffer(@PathVariable String callId, @RequestBody String offer) {
        offers.put(callId, offer);
        System.out.println("AUDIO Offer received for call: " + callId);
    }

    // Caller запрашивает answer
    @GetMapping("/answer/{callId}")
    public String getAnswer(@PathVariable String callId) {
        return answers.get(callId);
    }

    // Answer от Callee
    @PostMapping("/answer/{callId}")
    public void setAnswer(@PathVariable String callId, @RequestBody String answer) {
        answers.put(callId, answer);
        System.out.println("AUDIO Answer received for call: " + callId);
    }

    // Callee запрашивает offer
    @GetMapping("/offer/{callId}")
    public String getOffer(@PathVariable String callId) {
        return offers.get(callId);
    }

    @PostMapping("/ice-candidate/{callId}")
    public void addIceCandidate(@PathVariable String callId, @RequestBody Map<String, Object> candidate) {
        iceCandidates.computeIfAbsent(callId, k -> new CopyOnWriteArrayList<>()).add(candidate);
        System.out.println("ICE candidate received for call: " + callId);
    }

    @GetMapping("/ice-candidates/{callId}")
    public List<Map<String, Object>> getIceCandidates(@PathVariable String callId) {
        return iceCandidates.getOrDefault(callId, List.of());
    }

}