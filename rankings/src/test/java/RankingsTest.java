import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RankingsTest {

    @Test
    void testCalculateRankingsValidInput() {
        List<String> matches = Arrays.asList(
                "Team A 3, Team B 1",
                "Team C 2, Team D 2",
                "Team A 1, Team C 1"
        );

        Map<String, Integer> expected = new HashMap<>();
        expected.put("Team A", 4);
        expected.put("Team B", 0);
        expected.put("Team C", 2);
        expected.put("Team D", 1);

        Map<String, Integer> result = Rankings.calculateRankings(matches);
        assertEquals(expected, result);
    }

    @Test
    void testCalculateRankingsEmptyInput() {
        List<String> matches = Collections.emptyList();

        Map<String, Integer> result = Rankings.calculateRankings(matches);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateRankingsInvalidInput() {
        List<String> matches = Arrays.asList(
                "Team A 3, Team B",
                "Team C, Team D 2",
                "Team E 2, Team F 1"
        );

        Map<String, Integer> result = Rankings.calculateRankings(matches);

        assertEquals(2, result.size()); // Only the valid match is processed
        assertEquals(3, result.get("Team E"));
        assertEquals(0, result.get("Team F"));
    }

    @Test
    void testSortRankingsByPoints() {
        Map<String, Integer> points = new HashMap<>();
        points.put("Team A", 4);
        points.put("Team B", 0);
        points.put("Team C", 2);
        points.put("Team D", 1);

        List<Map.Entry<String, Integer>> sorted = Rankings.sortRankings(points);

        assertEquals("Team A", sorted.get(0).getKey());
        assertEquals("Team C", sorted.get(1).getKey());
        assertEquals("Team D", sorted.get(2).getKey());
        assertEquals("Team B", sorted.get(3).getKey());
    }

    @Test
    void testSortRankingsAlphabeticalTies() {
        Map<String, Integer> points = new HashMap<>();
        points.put("Team A", 4);
        points.put("Team B", 4);
        points.put("Team C", 4);

        List<Map.Entry<String, Integer>> sorted = Rankings.sortRankings(points);

        assertEquals("Team A", sorted.get(0).getKey());
        assertEquals("Team B", sorted.get(1).getKey());
        assertEquals("Team C", sorted.get(2).getKey());
    }

    @Test
    void testFormatRankings() {
        Map<String, Integer> points = new HashMap<>();
        points.put("Team A", 4);
        points.put("Team B", 3);
        points.put("Team C", 2);

        List<Map.Entry<String, Integer>> sorted = Rankings.sortRankings(points);

        String expectedOutput = "1. Team A, 4 pts\n2. Team B, 3 pts\n3. Team C, 2 pts\n";
        assertEquals(expectedOutput, Rankings.formatRankings(sorted));
    }

    @Test
    void testFormatRankingsEmpty() {
        List<Map.Entry<String, Integer>> sorted = Collections.emptyList();

        String result = Rankings.formatRankings(sorted);

        assertEquals("No valid match results to display.", result);
    }

    @Test
    void testParseMatchResultValidInput() {
        String input = "Team A 3, Team B 2";
        String[] expected = {"Team A", "3", "Team B", "2"};

        String[] result = Rankings.parseMatchResult(input);

        assertArrayEquals(expected, result);
    }

    @Test
    void testParseMatchResultInvalidInput() {
        String input = "Team A 3 Team B 2";

        String[] result = Rankings.parseMatchResult(input);

        assertNull(result); // Invalid input should return null
    }

    @Test
    void testParseMatchResultNegativeScores() {
        String input = "Team A -1, Team B -2";

        String[] result = Rankings.parseMatchResult(input);

        assertNull(result); // Negative scores should return null
    }
}
