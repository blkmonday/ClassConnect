import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database — In-memory data store for ClassConnect.
 * Manages courses, buildings, and student records for Southern University.
 */
public class Database {

    private List<Course> courses;
    private List<Building> buildings;
    private List<Student> students;

    public Database() {
        courses = new ArrayList<>();
        buildings = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void initializeSampleData() {
        // ─── BUILDINGS ────────────────────────────────────────
        buildings.add(new Building("PH", "Pinchback Hall", "Science & Engineering classrooms, computer labs"));
        buildings.add(new Building("TH", "T.H. Harris Hall", "College of Business, lecture halls"));
        buildings.add(new Building("SH", "Stewart Hall", "Humanities & Social Sciences"));
        buildings.add(new Building("SB", "Smith-Brown Memorial Union", "Student Union, dining, bookstore"));
        buildings.add(new Building("CA", "Clark Activity Center", "Athletics, recreation, events"));
        buildings.add(new Building("MS", "A.C. Mumford Stadium", "Football stadium & athletics fields"));
        buildings.add(new Building("LB", "John B. Cade Library", "Main library, study rooms, research"));
        buildings.add(new Building("LH", "Lee Hall", "Fine Arts, music rooms, auditorium"));
        buildings.add(new Building("ME", "Mechanical Engineering Bldg", "Engineering labs & workshops"));
        buildings.add(new Building("NB", "Nursing Building", "College of Nursing & Allied Health"));
        buildings.add(new Building("AD", "J.S. Clark Admin Building", "Admissions, registrar, financial aid"));
        buildings.add(new Building("SG", "Seymour Gymnasium", "Basketball courts, fitness center"));
        buildings.add(new Building("BH", "Augustus C. Blanks Hall", "Education & Computer Science dept"));
        buildings.add(new Building("PN", "Pennington Hall", "Residence hall"));
        buildings.add(new Building("TT", "Totty Hall", "Residence hall"));

        // ─── COURSES ─────────────────────────────────────────
        courses.add(new Course("CMPS 161", "Introduction to Programming", "Dr. Jackson", "Computer Science", "MWF", "9:00 AM", "9:50 AM", "Pinchback Hall", "210", 3, 35, 28));
        courses.add(new Course("CMPS 301", "Data Structures", "Dr. Williams", "Computer Science", "TTh", "10:30 AM", "11:45 AM", "Pinchback Hall", "305", 3, 30, 30));
        courses.add(new Course("CMPS 340", "Computer Networks", "Dr. Brown", "Computer Science", "MWF", "11:00 AM", "11:50 AM", "Blanks Hall", "102", 3, 25, 22));
        courses.add(new Course("CMPS 370", "Cybersecurity Fundamentals", "Dr. Davis", "Computer Science", "TTh", "1:00 PM", "2:15 PM", "Pinchback Hall", "410", 3, 25, 25));
        courses.add(new Course("CMPS 415", "Operating Systems", "Dr. Jackson", "Computer Science", "MWF", "2:00 PM", "2:50 PM", "Blanks Hall", "205", 3, 30, 18));
        courses.add(new Course("CMPS 450", "Software Engineering", "Dr. Thompson", "Computer Science", "TTh", "3:30 PM", "4:45 PM", "Pinchback Hall", "310", 3, 25, 20));
        courses.add(new Course("MATH 250", "Calculus I", "Dr. Roberts", "Mathematics", "MWF", "8:00 AM", "8:50 AM", "Harris Hall", "112", 4, 40, 38));
        courses.add(new Course("MATH 251", "Calculus II", "Dr. Adams", "Mathematics", "TTh", "9:00 AM", "10:15 AM", "Harris Hall", "115", 4, 35, 35));
        courses.add(new Course("MATH 310", "Linear Algebra", "Dr. Roberts", "Mathematics", "MWF", "1:00 PM", "1:50 PM", "Harris Hall", "210", 3, 30, 21));
        courses.add(new Course("ENGL 101", "Freshman Composition I", "Prof. Martin", "English", "MWF", "10:00 AM", "10:50 AM", "Stewart Hall", "105", 3, 25, 24));
        courses.add(new Course("ENGL 102", "Freshman Composition II", "Prof. Wilson", "English", "TTh", "11:00 AM", "12:15 PM", "Stewart Hall", "202", 3, 25, 25));
        courses.add(new Course("PHYS 201", "General Physics I", "Dr. Chang", "Physics", "MWF", "12:00 PM", "12:50 PM", "Pinchback Hall", "115", 4, 30, 23));
        courses.add(new Course("BIOL 101", "General Biology", "Dr. Green", "Biology", "TTh", "8:00 AM", "9:15 AM", "Pinchback Hall", "105", 3, 40, 37));
        courses.add(new Course("HIST 201", "American History I", "Prof. Carter", "History", "MWF", "10:00 AM", "10:50 AM", "Stewart Hall", "301", 3, 35, 30));
        courses.add(new Course("ACCT 201", "Principles of Accounting I", "Dr. Harris", "Business", "TTh", "2:00 PM", "3:15 PM", "Harris Hall", "305", 3, 35, 33));
        courses.add(new Course("MGMT 310", "Principles of Management", "Dr. Lewis", "Business", "MWF", "3:00 PM", "3:50 PM", "Harris Hall", "208", 3, 30, 30));
        courses.add(new Course("NURS 201", "Foundations of Nursing", "Prof. Mitchell", "Nursing", "TTh", "8:00 AM", "9:15 AM", "Nursing Building", "102", 4, 25, 24));
        courses.add(new Course("MUSC 101", "Music Appreciation", "Prof. Taylor", "Fine Arts", "TTh", "12:30 PM", "1:45 PM", "Lee Hall", "205", 3, 40, 35));
        courses.add(new Course("ENGR 220", "Statics & Dynamics", "Dr. Patel", "Engineering", "MWF", "11:00 AM", "11:50 AM", "Mechanical Engineering Bldg", "110", 3, 25, 22));

        // ─── DEMO STUDENT ────────────────────────────────────
        Student demo = new Student("JAG-001234", "Jaguar Student", "jstudent@subr.edu",
                "Computer Science", "Cybersecurity", "Junior", "Dr. Williams", 3.45, 72);
        // Enroll in default courses
        demo.addCourse(getCourseByCode("CMPS 161"));
        demo.addCourse(getCourseByCode("CMPS 340"));
        demo.addCourse(getCourseByCode("CMPS 370"));
        demo.addCourse(getCourseByCode("CMPS 415"));
        demo.addCourse(getCourseByCode("MATH 250"));
        students.add(demo);
    }

    // ─── QUERY METHODS ───────────────────────────────────────

    public Student getDemoStudent() {
        return students.isEmpty() ? null : students.get(0);
    }

    public Student getStudentById(String id) {
        return students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    public Course getCourseByCode(String code) {
        return courses.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);
    }

    public List<Course> searchCourses(String query) {
        String q = query.toLowerCase();
        return courses.stream()
                .filter(c -> c.getCode().toLowerCase().contains(q)
                        || c.getName().toLowerCase().contains(q)
                        || c.getInstructor().toLowerCase().contains(q)
                        || c.getDept().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Building> searchBuildings(String query) {
        String q = query.toLowerCase();
        return buildings.stream()
                .filter(b -> b.getName().toLowerCase().contains(q)
                        || b.getCode().toLowerCase().contains(q)
                        || b.getDescription().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Building> getAllBuildings() {
        return buildings;
    }

    public List<Course> getAllCourses() {
        return courses;
    }
}
