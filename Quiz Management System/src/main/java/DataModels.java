import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

public class DataModels {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuizResponse {
        public String regNo;
        public String setId;
        public int pollIndex;
        public List<Event> events;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        public String roundId;
        public String participant;
        public int score;
    }

    public static class SubmitRequest {
        public String regNo;
        public List<LeaderboardEntry> leaderboard;

        public SubmitRequest(String regNo, List<LeaderboardEntry> leaderboard) {
            this.regNo = regNo;
            this.leaderboard = leaderboard;
        }
    }

    public static class LeaderboardEntry {
        public String participant;
        public int totalScore;

        public LeaderboardEntry(String participant, int totalScore) {
            this.participant = participant;
            this.totalScore = totalScore;
        }
    }
}