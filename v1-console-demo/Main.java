import java.util.Scanner;

/**
 * ClassConnect — Southern University A&M College
 * Main entry point for the backend application.
 *
 * Helps Southern University students:
 *   - View and manage class schedules
 *   - Navigate campus and locate buildings
 *   - Search course catalog and register for classes
 *   - Track credits and academic progress
 *
 * @author ClassConnect Team
 * @version 1.0 — Spring 2026
 */
public class Main {

    private static Database db = new Database();
    private static Scanner scanner = new Scanner(System.in);
    private static Student currentStudent = null;

    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("   ClassConnect — Southern University A&M");
        System.out.println("   Helping Jaguars Navigate Campus & Classes");
        System.out.println("================================================\n");

        db.initializeSampleData();
        login();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": viewSchedule();  break;
                case "2": searchCourses(); break;
                case "3": findBuilding();  break;
                case "4": viewProfile();   break;
                case "5": addCourse();     break;
                case "6": dropCourse();    break;
                case "7": viewCampusMap(); break;
                case "0":
                    running = false;
                    System.out.println("\nGoodbye, Jaguar! See you on campus.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void login() {
        System.out.print("Enter your Student ID (or press Enter for demo): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            currentStudent = db.getDemoStudent();
        } else {
            currentStudent = db.getStudentById(input);
            if (currentStudent == null) {
                System.out.println("Student not found. Using demo account.");
                currentStudent = db.getDemoStudent();
            }
        }
        System.out.println("\nWelcome back, " + currentStudent.getName() + "!");
        System.out.println("  Major: " + currentStudent.getMajor());
        System.out.println("  ID:    " + currentStudent.getStudentId() + "\n");
    }

    private static void printMenu() {
        System.out.println("\n========== CLASSCONNECT MENU ==========");
        System.out.println("  1. View My Schedule");
        System.out.println("  2. Search Courses");
        System.out.println("  3. Find a Building");
        System.out.println("  4. View My Profile");
        System.out.println("  5. Add a Course");
        System.out.println("  6. Drop a Course");
        System.out.println("  7. Campus Map");
        System.out.println("  0. Logout & Exit");
        System.out.println("========================================");
    }

    private static void viewSchedule() {
        System.out.println("\n--- MY SCHEDULE — Spring 2026 ---");
        var schedule = currentStudent.getSchedule();
        if (schedule.isEmpty()) {
            System.out.println("  (No classes registered yet)");
            return;
        }

        int totalCredits = 0;
        for (Course c : schedule) {
            System.out.printf("  %-10s %-30s %s%n", c.getCode(), c.getName(),
                    c.getDays() + " " + c.getTime());
            System.out.printf("             Location: %-18s Instructor: %s  (%d cr)%n",
                    c.getBuilding() + " " + c.getRoom(), c.getInstructor(), c.getCredits());
            System.out.println("  ----------------------------------------");
            totalCredits += c.getCredits();
        }
        System.out.println("  Total: " + schedule.size() + " classes, " + totalCredits + " credit hours");
    }

    private static void searchCourses() {
        System.out.print("\nSearch (course code, name, or instructor): ");
        String query = scanner.nextLine().trim().toLowerCase();

        var results = db.searchCourses(query);
        if (results.isEmpty()) {
            System.out.println("  No courses found matching '" + query + "'");
            return;
        }

        System.out.println("\n  Found " + results.size() + " course(s):");
        for (Course c : results) {
            String status = c.getSeats() > c.getEnrolled() ? "OPEN" : "FULL";
            System.out.printf("  [%s] %-10s %-28s %s %s%n",
                    status, c.getCode(), c.getName(), c.getDays(), c.getTime());
            System.out.printf("          Instructor: %-18s Location: %s %s  (%d/%d seats)%n",
                    c.getInstructor(), c.getBuilding(), c.getRoom(),
                    c.getEnrolled(), c.getSeats());
        }
    }

    private static void findBuilding() {
        System.out.print("\nEnter building name or code: ");
        String query = scanner.nextLine().trim().toLowerCase();

        var results = db.searchBuildings(query);
        if (results.isEmpty()) {
            System.out.println("  No buildings found matching '" + query + "'");
            return;
        }

        for (Building b : results) {
            System.out.println("\n  " + b.getName() + " (" + b.getCode() + ")");
            System.out.println("  " + b.getDescription());

            var classesHere = currentStudent.getClassesInBuilding(b.getCode());
            if (!classesHere.isEmpty()) {
                System.out.println("  Your classes here:");
                for (Course c : classesHere) {
                    System.out.println("    - " + c.getCode() + " " + c.getName() +
                            " (" + c.getDays() + " " + c.getTime() + ", Room " + c.getRoom() + ")");
                }
            }
        }
    }

    private static void viewProfile() {
        System.out.println("\n--- STUDENT PROFILE ---");
        System.out.println("  Name:           " + currentStudent.getName());
        System.out.println("  Student ID:     " + currentStudent.getStudentId());
        System.out.println("  Email:          " + currentStudent.getEmail());
        System.out.println("  Major:          " + currentStudent.getMajor());
        System.out.println("  Concentration:  " + currentStudent.getConcentration());
        System.out.println("  Classification: " + currentStudent.getClassification());
        System.out.println("  Advisor:        " + currentStudent.getAdvisor());
        System.out.println("  GPA:            " + currentStudent.getGpa());
        System.out.println("  Credits Done:   " + currentStudent.getCreditsCompleted());
        System.out.println("  Current Credits:" + currentStudent.getCurrentCredits());
    }

    private static void addCourse() {
        System.out.print("\nEnter course code to add (e.g. CMPS 301): ");
        String code = scanner.nextLine().trim().toUpperCase();

        Course course = db.getCourseByCode(code);
        if (course == null) {
            System.out.println("  Course '" + code + "' not found in catalog.");
            return;
        }
        if (currentStudent.hasCourse(code)) {
            System.out.println("  You're already enrolled in " + code);
            return;
        }
        if (course.getEnrolled() >= course.getSeats()) {
            System.out.println("  " + code + " is full (" + course.getEnrolled() + "/" + course.getSeats() + ")");
            return;
        }

        currentStudent.addCourse(course);
        System.out.println("  Added " + code + ": " + course.getName());
        System.out.println("  " + course.getDays() + " " + course.getTime() +
                " @ " + course.getBuilding() + " " + course.getRoom());
    }

    private static void dropCourse() {
        var schedule = currentStudent.getSchedule();
        if (schedule.isEmpty()) {
            System.out.println("\n  You have no courses to drop.");
            return;
        }

        System.out.println("\nYour current courses:");
        for (int i = 0; i < schedule.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + schedule.get(i).getCode() +
                    " - " + schedule.get(i).getName());
        }

        System.out.print("Enter number to drop (0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice == 0) return;
            if (choice < 1 || choice > schedule.size()) {
                System.out.println("  Invalid selection.");
                return;
            }
            Course dropped = schedule.remove(choice - 1);
            System.out.println("  Dropped " + dropped.getCode() + ": " + dropped.getName());
        } catch (NumberFormatException e) {
            System.out.println("  Invalid input.");
        }
    }

    private static void viewCampusMap() {
        System.out.println("\n--- SOUTHERN UNIVERSITY CAMPUS MAP ---\n");
        System.out.println("    [PN]              [AD]           [ME]  [NB]");
        System.out.println("                       |                       ");
        System.out.println("    [LH]   [LB]       |      [TH]            ");
        System.out.println("            |          |       |              ");
        System.out.println("  ------- Harding Blvd ------------ [CA] ----");
        System.out.println("            |          |       |              ");
        System.out.println("    [SH]   [PH]      [SB]           [MS]    ");
        System.out.println("                                              ");
        System.out.println("           [SG]      [BH]           [TT]    ");
        System.out.println();
        System.out.println("  Legend (* = You have classes here):");

        for (Building b : db.getAllBuildings()) {
            var classesHere = currentStudent.getClassesInBuilding(b.getCode());
            String marker = classesHere.isEmpty() ? "  " : "* ";
            System.out.printf("    %s[%s] %s%n", marker, b.getCode(), b.getName());
        }
    }
}
