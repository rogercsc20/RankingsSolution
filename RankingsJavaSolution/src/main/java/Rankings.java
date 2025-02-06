import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rankings {
    private static final Logger logger = Logger.getLogger(Rankings.class.getName());

    public static Map<String, Integer> calculateRankings(List<String> matches) {
        Map<String, Integer> points = new HashMap<>();

        for (String match : matches) {
            String[] parsed = parseMatchResult(match);
            if (parsed == null) {
                continue;
            }

            String team1 = parsed[0];
            int score1 = Integer.parseInt(parsed[1]);
            String team2 = parsed[2];
            int score2 = Integer.parseInt(parsed[3]);

            points.putIfAbsent(team1, 0);
            points.putIfAbsent(team2, 0);

            if (score1 > score2) {
                points.put(team1, points.get(team1) + 3);
            } else if (score2 > score1) {
                points.put(team2, points.get(team2) + 3);
            } else {
                points.put(team1, points.get(team1) + 1);
                points.put(team2, points.get(team2) + 1);
            }
        }

        return points;
    }

    public static String[] parseMatchResult(String match) {
        try {
            String[] parts = match.split(", ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Line must contain exactly two teams separated by ', '");
            }
    
            // Validate team1 and score1
            int lastSpaceIndex1 = parts[0].lastIndexOf(" ");
            if (lastSpaceIndex1 == -1) {
                throw new IllegalArgumentException("Invalid format for the first team and score: " + parts[0]);
            }
            String team1 = parts[0].substring(0, lastSpaceIndex1).trim();
            String score1Str = parts[0].substring(lastSpaceIndex1 + 1).trim();
            if (team1.isEmpty() || score1Str.isEmpty()) {
                throw new IllegalArgumentException("Team name or score cannot be empty for the first part: " + parts[0]) ;
            }
            int score1 = Integer.parseInt(score1Str);
    
            // Validate team2 and score2
            int lastSpaceIndex2 = parts[1].lastIndexOf(" ");
            if (lastSpaceIndex2 == -1) {
                throw new IllegalArgumentException("Invalid format for the second team and score: " + parts[1]);
            }
            String team2 = parts[1].substring(0, lastSpaceIndex2).trim();
            String score2Str = parts[1].substring(lastSpaceIndex2 + 1).trim();
            if (team2.isEmpty() || score2Str.isEmpty()) {
                throw new IllegalArgumentException("Team name or score cannot be empty for the second part: " + parts[1]);
            }
            int score2 = Integer.parseInt(score2Str);
    
            // Check for negative scores
            if (score1 < 0 || score2 < 0) {
                throw new IllegalArgumentException("Scores cannot be negative: " + match);
            }
    
            return new String[]{team1, String.valueOf(score1), team2, String.valueOf(score2)};
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Skipping invalid input: {0}. Error: Invalid number format", new Object[]{match});
            return null;
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Skipping invalid input: {0}. Error: {1}", new Object[]{match, e.getMessage()});
            return null;
        }
    }
    

    public static List<Map.Entry<String, Integer>> sortRankings(Map<String, Integer> points) {
        List<Map.Entry<String, Integer>> sortedTeams = new ArrayList<>(points.entrySet());
        sortedTeams.sort(Comparator
            .comparing((Map.Entry<String, Integer> entry) -> entry.getValue()).reversed()
            .thenComparing(Map.Entry::getKey));
        return sortedTeams;
    }

    public static String formatRankings(List<Map.Entry<String, Integer>> sortedTeams) {
        if (sortedTeams.isEmpty()) {
            return "No valid match results to display.";
        }

        StringBuilder output = new StringBuilder();
        int rank = 0;
        int prevPoints = -1;

        for (int i = 0; i < sortedTeams.size(); i++) {
            Map.Entry<String, Integer> team = sortedTeams.get(i);
            if (!team.getValue().equals(prevPoints)) {
                rank = i + 1;
            }
            prevPoints = team.getValue();
            String pointStr = (team.getValue() == 1) ? "pt" : "pts";
            output.append(rank).append(". ").append(team.getKey()).append(", ").append(team.getValue()).append(" ").append(pointStr).append("\n");
        }

        return output.toString();
    }

    public static void main(String[] args) {
        try {
            List<String> matches = readMatchesFromInput(args);

            if (matches.isEmpty()) {
                System.out.println("No match results provided.");
                return;
            }

            Map<String, Integer> rankings = calculateRankings(matches);
            if (rankings.isEmpty()) {
                System.out.println("No valid match results to display.");
                return;
            }

            List<Map.Entry<String, Integer>> sortedRankings = sortRankings(rankings);
            System.out.println(formatRankings(sortedRankings));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error processing input: {0}", e.getMessage());
        }
    }

    private static List<String> readMatchesFromInput(String[] args) throws IOException {
        List<String> matches = new ArrayList<>();

        if (args.length > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    matches.add(line);
                }
            }
        } else {
            System.out.println("Enter match results (one per line). Press Ctrl+D (Linux/macOS) or Ctrl+Z (Windows) when done:");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    matches.add(line);
                }
            }
        }

        return matches;
    }
}
