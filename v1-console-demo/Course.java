/**
 * Course — Represents a course in the Southern University catalog.
 */
public class Course {
    private String code;
    private String name;
    private String instructor;
    private String dept;
    private String days;
    private String time;
    private String endTime;
    private String building;
    private String room;
    private int credits;
    private int seats;
    private int enrolled;

    public Course(String code, String name, String instructor, String dept,
                  String days, String time, String endTime,
                  String building, String room, int credits, int seats, int enrolled) {
        this.code = code;
        this.name = name;
        this.instructor = instructor;
        this.dept = dept;
        this.days = days;
        this.time = time;
        this.endTime = endTime;
        this.building = building;
        this.room = room;
        this.credits = credits;
        this.seats = seats;
        this.enrolled = enrolled;
    }

    // Getters
    public String getCode()       { return code; }
    public String getName()       { return name; }
    public String getInstructor() { return instructor; }
    public String getDept()       { return dept; }
    public String getDays()       { return days; }
    public String getTime()       { return time; }
    public String getEndTime()    { return endTime; }
    public String getBuilding()   { return building; }
    public String getRoom()       { return room; }
    public int getCredits()       { return credits; }
    public int getSeats()         { return seats; }
    public int getEnrolled()      { return enrolled; }

    public void setEnrolled(int enrolled) { this.enrolled = enrolled; }

    @Override
    public String toString() {
        return code + " - " + name + " (" + days + " " + time + ")";
    }
}
