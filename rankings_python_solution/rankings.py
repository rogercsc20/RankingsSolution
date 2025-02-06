import sys
import logging
import argparse
from collections import defaultdict

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

def parse_match_result(line):
    """Parses a match result line and returns team names with their respective scores."""
    try:
        parts = line.strip().split(', ')
        if len(parts) != 2:
            raise ValueError("Line must contain exactly two teams separated by ', '")

        team1, score1 = parts[0].rsplit(' ', 1)
        team2, score2 = parts[1].rsplit(' ', 1)

        score1 = int(score1)
        score2 = int(score2)

        if not team1 or not team2:
            raise ValueError("Team names cannot be empty")

        # Check for negative scores
        if score1 < 0 or score2 < 0:
            raise ValueError("Scores cannot be negative")
        
        return team1, score1, team2, score2
    
    except (ValueError, IndexError) as e:
        logging.warning(f"Skipping invalid input: '{line}'. Error: {e}")
        return None

def calculate_rankings(matches):
    """Computes rankings based on match results."""
    points = defaultdict(int)

    for match in matches:
        parsed = parse_match_result(match)
        if parsed is None:
            continue

        team1, score1, team2, score2 = parsed

        # Ensure all teams are registered with at least 0 points
        if team1 not in points:
            points[team1] = 0
        if team2 not in points:
            points[team2] = 0

        # Assign points based on match results
        if score1 > score2:
            points[team1] += 3
        elif score2 > score1:
            points[team2] += 3
        else:
            points[team1] += 1
            points[team2] += 1
    
    return sorted(points.items(), key=lambda x: (-x[1], x[0]))

def format_rankings(sorted_teams):
    """Formats the ranking table."""
    if not sorted_teams:
        return "No valid match results to display."

    output = []
    rank = 1
    prev_points = None

    for i, (team, pts) in enumerate(sorted_teams, start=1):
        if pts != prev_points:
            rank = i

        output.append(f"{rank}. {team}, {pts} {'pt' if pts == 1 else 'pts'}")
        prev_points = pts

    return "\n".join(output)

def get_args():
    """Parses command-line arguments."""
    parser = argparse.ArgumentParser(description="Calculate league rankings from match results.")
    parser.add_argument("file", nargs="?", type=argparse.FileType("r"), help="File containing match results.")
    return parser.parse_args()

def main():
    """Reads input (from file or stdin) and prints the league ranking."""
    args = get_args()

    if args.file:
        matches = args.file.readlines()
    else:
        print("Enter match results (one per line). Press Ctrl+D (Linux/macOS) or Ctrl+Z (Windows) when done:")
        matches = sys.stdin.read().splitlines()

    if not matches:
        print("No match results provided.")
        sys.exit(1)
    
    rankings = calculate_rankings(matches)
    print(format_rankings(rankings))

if __name__ == "__main__":
    main()
