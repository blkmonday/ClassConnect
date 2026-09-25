import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Student — Represents a student at Southern University.
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private String major;
    private String concentration;
    private String classification;
    private String advisor;
    private double gpa;
    private int creditsCompleted;
    private List<Course> schedule;

    public Student(String studentId, String name, String email, String major,
                   String concentration, String classification, String advisor,
                   double gpa, int creditsCompleted) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.major = major;
        this.concentration = concentration;
        this.classification = classification;
        this.advisor = advisor;
        this.gpa = gpa;
        this.creditsCompleted = creditsCompleted;
        this.schedule = new ArrayList<>();
    }

    // ─── SCHEDULE MANAGEMENT ─────────────────────────────────

    public void addCourse(Course course) {
        if (course != null && !hasCourse(course.getCode())) {
            schedule.add(course);
        }
    }

    public boolean hasCourse(String code) {
        return schedule.stream().anyMatch(c -> c.getCode().equalsIgnoreCase(code));
    }

    public List<Course> getSchedule() {
        return schedule;
    }

    public int getCurrentCredits() {
        return schedule.stream().mapToInt(Course::getCredits).sum();
    }

    /**
     * Returns all courses the student has in a specific building.
     * Matches against building name (partial match) or building code in the name.
     */
    public List<Course> getClassesInBuilding(String buildingCode) {
        return schedule.stream()
                .filter(c -> {
                    String bldg = c.getBuilding().toLowerCase();
                    String code = buildingCode.toLowerCase();
                    // Match building code abbreviations to full building names
                    switch (code) {
                        case "ph": return bldg.contains("pinchback");
                        case "th": return bldg.contains("harris");
                        case "sh": return bldg.contains("stewart");
                        case "sb": return bldg.contains("smith") || bldg.contains("union");
                        case "ca": return bldg.contains("clark activity");
                        case "lb": return bldg.contains("library") || bldg.contains("cade");
                        case "lh": return bldg.contains("lee hall");
                        case "me": return bldg.contains("mechanical") || bldg.contains("engineering bldg");
                        case "nb": return bldg.contains("nursing");
                        case "ad": return bldg.contains("admin") || bldg.contains("clark admin");
                        case "sg": return bldg.contains("seymour") || bldg.contains("gymnasium");
                        case "bh": return bldg.contains("blanks");
                        default:   return bldg.contains(code);
                    }
                })
                .collect(Collectors.toList());
    }

    // ─── GETTERS ─────────────────────────────────────────────

    public String getStudentId()      { return studentId; }
    public String getName()           { return name; }
    public String getEmail()          { return email; }
    public String getMajor()          { return major; }
    public String getConcentration()  { return concentration; }
    public String getClassification() { return classification; }
    public String getAdvisor()        { return advisor; }
    public double getGpa()            { return gpa; }
    public int getCreditsCompleted()  { return creditsCompleted; }

    @Override
    public String toString() {
        return name + " (" + studentId + ") - " + major;
    }
}
