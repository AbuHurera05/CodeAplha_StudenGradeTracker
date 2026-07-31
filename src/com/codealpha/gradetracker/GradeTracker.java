package com.codealpha.gradetracker;

import java.util.*;
import java.io.*;

public class GradeTracker {
    private List<Student> students;
    private Scanner scanner;

    public GradeTracker() {
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addMultipleStudents();
                    break;
                case 3:
                    displayAllStudents();
                    break;
                case 4:
                    displayStatistics();
                    break;
                case 5:
                    searchStudent();
                    break;
                case 6:
                    removeStudent();
                    break;
                case 7:
                    saveToFile();
                    break;
                case 8:
                    loadFromFile();
                    break;
                case 9:
                    generateReport();
                    break;
                case 10:
                    System.out.println("Thank you for using Student Grade Tracker!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n═════════════════════════════════════════");
        System.out.println("     📚 STUDENT GRADE TRACKER");
        System.out.println("═════════════════════════════════════════");
        System.out.println("1. Add Single Student");
        System.out.println("2. Add Multiple Students");
        System.out.println("3. Display All Students");
        System.out.println("4. Display Statistics");
        System.out.println("5. Search Student");
        System.out.println("6. Remove Student");
        System.out.println("7. Save Data to File");
        System.out.println("8. Load Data from File");
        System.out.println("9. Generate Report");
        System.out.println("10. Exit");
        System.out.println("═════════════════════════════════════════");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        int grade = getValidGrade();
        students.add(new Student(name, grade));
        System.out.println("✅ Student added successfully!");
    }

    private void addMultipleStudents() {
        System.out.print("How many students do you want to add? ");
        try {
            int count = Integer.parseInt(scanner.nextLine().trim());
            for (int i = 0; i < count; i++) {
                System.out.println("\nStudent #" + (i + 1));
                addStudent();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private int getValidGrade() {
        while (true) {
            System.out.print("Enter grade (0-100): ");
            try {
                int grade = Integer.parseInt(scanner.nextLine().trim());
                if (grade >= 0 && grade <= 100) {
                    return grade;
                }
                System.out.println("Grade must be between 0 and 100!");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("⚠️ No students found!");
            return;
        }
        
        System.out.println("\n📋 Student List:");
        System.out.println("─────────────────────");
        System.out.printf("%-20s %s%n", "Student Name", "Grade");
        System.out.println("─────────────────────");
        for (Student student : students) {
            System.out.printf("%-20s %d%n", student.getName(), student.getGrade());
        }
        System.out.println("─────────────────────");
        System.out.println("Total Students: " + students.size());
    }

    private void displayStatistics() {
        if (students.isEmpty()) {
            System.out.println("⚠️ No students found!");
            return;
        }

        List<Integer> grades = students.stream()
            .map(Student::getGrade)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        double average = GradeUtils.calculateAverage(grades);
        int highest = GradeUtils.findHighestGrade(grades);
        int lowest = GradeUtils.findLowestGrade(grades);
        
        System.out.println("\n📊 Grade Statistics:");
        System.out.println("─────────────────────");
        System.out.printf("📈 Average: %.2f%n", average);
        System.out.printf("🏆 Highest Score: %d%n", highest);
        System.out.printf("📉 Lowest Score: %d%n", lowest);
        
        System.out.println("\nGrade Distribution:");
        int[] distribution = new int[5]; // A, B, C, D, F
        for (int grade : grades) {
            String letter = GradeUtils.getGradeLetter(grade);
            switch (letter) {
                case "A": distribution[0]++; break;
                case "B": distribution[1]++; break;
                case "C": distribution[2]++; break;
                case "D": distribution[3]++; break;
                case "F": distribution[4]++; break;
            }
        }
        
        System.out.printf("A (90-100): %d students%n", distribution[0]);
        System.out.printf("B (80-89):  %d students%n", distribution[1]);
        System.out.printf("C (70-79):  %d students%n", distribution[2]);
        System.out.printf("D (60-69):  %d students%n", distribution[3]);
        System.out.printf("F (0-59):   %d students%n", distribution[4]);
        
        // Top 3 performers
        System.out.println("\n🏅 Top Performers:");
        List<Student> topPerformers = GradeUtils.getTopPerformers(students, 3);
        for (int i = 0; i < topPerformers.size(); i++) {
            Student s = topPerformers.get(i);
            System.out.printf("#%d: %s - %d (%s)%n", 
                i+1, s.getName(), s.getGrade(), 
                GradeUtils.getGradeLetter(s.getGrade()));
        }
    }

    private void searchStudent() {
        System.out.print("Enter student name to search: ");
        String searchName = scanner.nextLine().trim().toLowerCase();
        
        List<Student> found = students.stream()
            .filter(s -> s.getName().toLowerCase().contains(searchName))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        if (found.isEmpty()) {
            System.out.println("⚠️ No students found with that name!");
        } else {
            System.out.println("\n🔍 Search Results:");
            for (Student student : found) {
                System.out.printf("%-20s %d (%s)%n", 
                    student.getName(), student.getGrade(),
                    GradeUtils.getGradeLetter(student.getGrade()));
            }
        }
    }

    private void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("⚠️ No students to remove!");
            return;
        }
        
        displayAllStudents();
        System.out.print("Enter the name of student to remove: ");
        String name = scanner.nextLine().trim();
        
        boolean removed = students.removeIf(s -> s.getName().equalsIgnoreCase(name));
        if (removed) {
            System.out.println("✅ Student removed successfully!");
        } else {
            System.out.println("⚠️ Student not found!");
        }
    }

    private void saveToFile() {
        System.out.print("Enter filename to save (e.g., grades.txt): ");
        String filename = scanner.nextLine().trim();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Student Name,Grade");
            for (Student s : students) {
                writer.println(s.getName() + "," + s.getGrade());
            }
            System.out.println("✅ Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        System.out.print("Enter filename to load (e.g., grades.txt): ");
        String filename = scanner.nextLine().trim();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // Skip header
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        String name = parts[0].trim();
                        int grade = Integer.parseInt(parts[1].trim());
                        students.add(new Student(name, grade));
                        count++;
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid grade in line: " + line);
                    }
                }
            }
            System.out.println("✅ Loaded " + count + " students from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found!");
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    private void generateReport() {
        if (students.isEmpty()) {
            System.out.println("⚠️ No students found!");
            return;
        }
        
        System.out.println("\n📄 DETAILED REPORT");
        System.out.println("═════════════════════════════════════════");
        System.out.println("Generated: " + new Date());
        System.out.println("═════════════════════════════════════════");
        displayStatistics();
        System.out.println("═════════════════════════════════════════");
        
        // Save report to file
        try (PrintWriter writer = new PrintWriter(new FileWriter("report.txt"))) {
            writer.println("STUDENT GRADE REPORT");
            writer.println("Generated: " + new Date());
            writer.println("─────────────────────");
            writer.println("Student Name,Grade,Letter Grade");
            for (Student s : students) {
                writer.printf("%s,%d,%s%n", 
                    s.getName(), s.getGrade(), 
                    GradeUtils.getGradeLetter(s.getGrade()));
            }
            System.out.println("✅ Report saved to report.txt");
        } catch (IOException e) {
            System.out.println("❌ Error saving report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GradeTracker tracker = new GradeTracker();
        tracker.run();
    }
}