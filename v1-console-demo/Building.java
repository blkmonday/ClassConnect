/**
 * Building — Represents a building on Southern University's campus.
 */
public class Building {
    private String code;
    private String name;
    private String description;

    public Building(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode()        { return code; }
    public String getName()        { return name; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "[" + code + "] " + name;
    }
}
