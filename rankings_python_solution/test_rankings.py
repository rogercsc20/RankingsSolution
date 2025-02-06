import unittest
from rankings import parse_match_result, calculate_rankings, format_rankings

class TestRankings(unittest.TestCase):
    
    def test_parse_match_result_valid(self):
        self.assertEqual(parse_match_result("Lions 3, Snakes 3"), ("Lions", 3, "Snakes", 3))
        self.assertEqual(parse_match_result("Real Madrid 2, Barcelona 1"), ("Real Madrid", 2, "Barcelona", 1))
    
    def test_parse_match_result_invalid_format(self):
        self.assertIsNone(parse_match_result("Invalid Input"))
        self.assertIsNone(parse_match_result("Lions 3 Snakes 3"))
        self.assertIsNone(parse_match_result("Lions, Snakes 3"))
    
    def test_parse_match_result_missing_score(self):
        self.assertIsNone(parse_match_result("Lions , Snakes 3"))
        self.assertIsNone(parse_match_result("Lions 3, "))
    
    def test_calculate_rankings_basic(self):
        matches = ["Lions 3, Snakes 3", "Tarantulas 1, FC Awesome 0"]
        expected = [("Tarantulas", 3), ("Lions", 1), ("Snakes", 1), ("FC Awesome", 0)]
        self.assertEqual(calculate_rankings(matches), expected)
    
    def test_calculate_rankings_tiebreaker(self):
        matches = ["TeamA 2, TeamB 1", "TeamC 3, TeamD 3", "TeamE 1, TeamF 1"]
        expected = [("TeamA", 3), ("TeamC", 1), ("TeamD", 1), ("TeamE", 1), ("TeamF", 1), ("TeamB", 0)]
        self.assertEqual(calculate_rankings(matches), expected)
    
    def test_calculate_rankings_no_matches(self):
        self.assertEqual(calculate_rankings([]), [])
    
    def test_format_rankings(self):
        rankings = [("Tarantulas", 6), ("Lions", 5), ("FC Awesome", 1), ("Snakes", 1), ("Grouches", 0)]
        expected_output = """1. Tarantulas, 6 pts\n2. Lions, 5 pts\n3. FC Awesome, 1 pt\n3. Snakes, 1 pt\n5. Grouches, 0 pts"""
        self.assertEqual(format_rankings(rankings), expected_output)
    
    def test_format_rankings_empty(self):
        self.assertEqual(format_rankings([]), "No valid match results to display.")
    
    def test_format_rankings_tie_handling(self):
        rankings = [("TeamA", 3), ("TeamB", 3), ("TeamC", 1)]
        expected_output = """1. TeamA, 3 pts\n1. TeamB, 3 pts\n3. TeamC, 1 pt"""
        self.assertEqual(format_rankings(rankings), expected_output)

if __name__ == '__main__':
    unittest.main()