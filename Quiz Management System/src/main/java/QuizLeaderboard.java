import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class QuizLeaderboard {

    private static final String REG_NO = "RA2311033010186";
    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";

    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();

            Set<String> processedEvents = new HashSet<>();
            Map<String, Integer> participantScores = new HashMap<>();

            System.out.println("Starting 10 API polls...");

            for (int i = 0; i < 10; i++) {
                String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + i;
                System.out.println("Polling Index " + i + "...");

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                DataModels.QuizResponse quizData = mapper.readValue(response.body(), DataModels.QuizResponse.class);

                if (quizData.events != null) {
                    for (DataModels.Event event : quizData.events) {
                        String uniqueKey = event.roundId + "-" + event.participant;

                        if (!processedEvents.contains(uniqueKey)) {
                            processedEvents.add(uniqueKey);

                            participantScores.put(
                                    event.participant,
                                    participantScores.getOrDefault(event.participant, 0) + event.score
                            );
                        }
                    }
                }

                if (i < 9) {
                    Thread.sleep(5000);
                }
            }

            List<DataModels.LeaderboardEntry> finalLeaderboard = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : participantScores.entrySet()) {
                finalLeaderboard.add(new DataModels.LeaderboardEntry(entry.getKey(), entry.getValue()));
            }

            finalLeaderboard.sort((a, b) -> Integer.compare(b.totalScore, a.totalScore));

            System.out.println("\nFinal Leaderboard Calculated. Submitting...");

            DataModels.SubmitRequest submitRequest = new DataModels.SubmitRequest(REG_NO, finalLeaderboard);
            String jsonPayload = mapper.writeValueAsString(submitRequest);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/quiz/submit"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println("Submission Response:");
            System.out.println(postResponse.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}