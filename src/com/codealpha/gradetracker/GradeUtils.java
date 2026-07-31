package com.codealpha.gradetracker;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalDouble;

public class GradeUtils {
    
    public static double calculateAverage(List<Integer> grades) {
        if (grades.isEmpty()) return 0.0;
        OptionalDouble average = grades.stream()
            .mapToInt(Integer::intValue)
            .average();
        return average.orElse(0.0);
    }

    public static int findHighestGrade(List<Integer> grades) {
        if (grades.isEmpty()) return 0;
        return Collections.max(grades);
    }

    public static int findLowestGrade(List<Integer> grades) {
        if (grades.isEmpty()) return 0;
        return Collections.min(grades);
    }

    public static String getGradeLetter(int score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }

    public static List<Student> getTopPerformers(List<Student> students, int topN) {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((s1, s2) -> Integer.compare(s2.getGrade(), s1.getGrade()));
        return sorted.subList(0, Math.min(topN, sorted.size()));
    }
}