package ru.corthos.call;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/offer/{callId}")
    public String getOffer(@PathVariable String callId) {
        System.out.println("GET OFFER for: " + callId + " -> " + offers.get(callId));
        return offers.get(callId);
    }

    @PostMapping("/offer/{callId}")
    public void setOffer(@PathVariable String callId, @RequestBody String offer) {
        offers.put(callId, offer);
        System.out.println("ADD OFFER: " + callId + " -> " + offer);
    }

    @GetMapping("/answer/{callId}")
    public String getAnswer(@PathVariable String callId) {
        System.out.println("GET ANSWER for: " + callId + " -> " + answers.get(callId));
        return answers.get(callId);
    }

    @PostMapping("/answer/{callId}")
    public void setAnswer(@PathVariable String callId, @RequestBody String answer) {
        answers.put(callId, answer);
        System.out.println("ADD ANSWER: " + callId + " -> " + answer);
    }

    @GetMapping("/ice-candidates/{callId}")
    public List<Map<String, Object>> getIceCandidates(@PathVariable String callId) {
        List<Map<String, Object>> candidates = iceCandidates.getOrDefault(callId, new ArrayList<>());
        System.out.println("GET ICE CANDIDATES for: " + callId + " -> " + candidates.size() + " candidates");

        // Очищаем кандидаты после получения (важно!)
        if (iceCandidates.containsKey(callId)) {
            iceCandidates.put(callId, new CopyOnWriteArrayList<>());
        }

        return candidates;
    }

    @PostMapping("/ice-candidate/{callId}")
    public void addIceCandidate(@PathVariable String callId, @RequestBody Map<String, Object> candidate) {
        // Добавляем кандидат в список, а не перезаписываем!
        iceCandidates.computeIfAbsent(callId, k -> new CopyOnWriteArrayList<>())
                .add(candidate);

        System.out.println("ADD ICE CANDIDATE for: " + callId + " -> " + candidate);
        System.out.println("Total candidates for " + callId + ": " +
                iceCandidates.get(callId).size());
    }

    // Добавьте метод для очистки
    @DeleteMapping("/cleanup/{callId}")
    public void cleanup(@PathVariable String callId) {
        offers.remove(callId);
        answers.remove(callId);
        iceCandidates.remove(callId);
        System.out.println("CLEANUP for: " + callId);
    }
}