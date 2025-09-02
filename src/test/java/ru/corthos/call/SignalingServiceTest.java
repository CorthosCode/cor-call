package ru.corthos.call;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

class SignalingServiceTest {

    public static final String CALL_ID = "test-call-race";
    public static final SignalingService SIGNALING_SERVICE = new SignalingService(new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

    @Test
    public void testIceCandidateRaceCondition() throws InterruptedException {
        final int threadCount = 10;
        final List<Map<String, Object>> allReceivedCandidates = Collections.synchronizedList(new ArrayList<>());

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            List<Future<?>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                final int candidateId = i;
                Future<?> future = executor.submit(() -> {
                    SIGNALING_SERVICE.saveIceCandidate(CALL_ID, createCandidate(candidateId));

                    if (ThreadLocalRandom.current().nextBoolean()) {
                        // Сохраняем полученные кандидаты, а не просто читаем
                        List<Map<String, Object>> received = SIGNALING_SERVICE.getIceCandidates(CALL_ID);
                        allReceivedCandidates.addAll(received);
                    }
                });
                futures.add(future);
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    // Игнорируем
                }
            }
        }

        // Получаем оставшиеся кандидаты
        List<Map<String, Object>> remainingCandidates = SIGNALING_SERVICE.getIceCandidates(CALL_ID);
        allReceivedCandidates.addAll(remainingCandidates);

        System.out.println("Total received candidates: " + allReceivedCandidates.size());

        // Должны получить все 10 кандидатов в сумме
        Assertions.assertThat(allReceivedCandidates.size()).isEqualTo(threadCount);
    }

    private Map<String, Object> createCandidate(int id) {
        return Map.of(
                "candidate", "candidate:" + id + " 1 udp 2122260223 192.168.1." + id + " 58831 typ host",
                "sdpMid", "0",
                "sdpMLineIndex", 0
        );
    }

}