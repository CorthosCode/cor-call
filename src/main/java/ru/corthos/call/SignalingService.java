package ru.corthos.call;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SignalingService {

    private final Map<String, String> offers;
    private final Map<String, String> answers;
    private final Map<String, BlockingQueue<Map<String, Object>>> iceCandidates;

    public SignalingService(Map<String, String> offers,
                            Map<String, String> answers,
                            Map<String, BlockingQueue<Map<String, Object>>> iceCandidates) {
        this.offers = offers;
        this.answers = answers;
        this.iceCandidates = iceCandidates;
    }

    public String getOffer(String callId) {
        System.out.println("GET OFFER for: " + callId + " -> " + offers.get(callId));
        return offers.get(callId);
    }

    public void saveOffer(String callId, String offer) {
        offers.put(callId, offer);
        System.out.println("ADD OFFER: " + callId + " -> " + offer);
    }

    public String getAnswer(String callId) {
        System.out.println("GET ANSWER for: " + callId + " -> " + answers.get(callId));
        return answers.get(callId);
    }

    public void saveAnswer(String callId, String answer) {
        answers.put(callId, answer);
        System.out.println("ADD ANSWER: " + callId + " -> " + answer);
    }

    public List<Map<String, Object>> getIceCandidates(String callId) {
        List<Map<String, Object>> candidates = new ArrayList<>();
        BlockingQueue<Map<String, Object>> queue = iceCandidates.get(callId);

        if (queue != null) {
            int drainedCount = queue.drainTo(candidates);
            System.out.println("GET ICE CANDIDATES for: " + callId + " -> " + drainedCount + " candidates");
        }

        return candidates;
    }

    public void saveIceCandidate(String callId, Map<String, Object> candidate) {
        iceCandidates.computeIfAbsent(callId, k -> new LinkedBlockingQueue<>()).offer(candidate);
        System.out.println("ADD ICE CANDIDATE for: " + callId + " -> " + candidate);
        System.out.println("Total candidates for " + callId + ": " + iceCandidates.get(callId).size());
    }

    public void clearCallData(String callId) {
        offers.remove(callId);
        answers.remove(callId);
        iceCandidates.remove(callId);
        System.out.println("CLEANUP for: " + callId);
    }
}