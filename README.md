Rankings Project

This repository contains two implementations of the Rankings problem in Java and Python. The program calculates team rankings from match results, either via command-line input or a text file.

Project Structure

/RankingsProject
  ├── RankingsJavaSolution/       # Java implementation
  ├── rankings_python_solution/   # Python implementation
  ├── .gitignore
  ├── README.md                   # This file

Problem Description

Given match results in the format:

TeamA 3, TeamB 1
TeamC 2, TeamD 2

Win → 3 points

Draw → 1 point each

Loss → 0 points

Sorted by points, tiebreakers use alphabetical order.

Implementations

Java Solution

📂 Located in RankingsJavaSolution/

How to Run:

Compile & Run Manually:

javac -d out RankingsJavaSolution/src/main/java/Rankings.java
java -cp out Rankings

Run with a TXT File:

java -cp out Rankings matches.txt

Java 17+ Required

Python Solution

📂 Located in rankings_python_solution/

How to Run:

Run Manually:

python rankings_python_solution/rankings.py

Run with a TXT File:

python rankings_python_solution/rankings.py matches.txt

📌 Python 3.8+ Required

Tests
Java: mvn test (cd into RankingsJavaSolution first)

Python: python rankings_python_solution/test_rankings.py

Features

✅ Manual or file input
✅ Handles invalid inputs
✅ Prevents negative scores
✅ Does not crash with invalid input
✅ Proper ranking & tiebreakersss