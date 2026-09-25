package backend;

import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class Database {
    public static List<backend.models.User> users = new ArrayList<>();
    public static List<backend.models.Alert> alerts = new ArrayList<>();
    public static List<backend.models.Schedule> schedules = new ArrayList<>();

    public static void loadAll() { loadUsers(); loadAlerts(); loadSchedules(); }

    public static void loadUsers(){
        users.clear();
        String base = "backend/data/users.txt";
        if (!Files.exists(Paths.get(base))) {
            if (Files.exists(Paths.get("data/users.txt"))) base = "data/users.txt";
        }
        try{
            if (Files.exists(Paths.get(base))){
                List<String> lines = Files.readAllLines(Paths.get(base));
                for(String line: lines){
                    backend.models.User u = new backend.models.User();
                    if (line.contains("|")){
                        String[] parts = line.split("\\|", -1);
                        if (parts.length >= 3){ u.id = parts[0]; u.email = parts[1]; u.password = parts[2]; users.add(u); }
                    } else {
                        int colon = line.indexOf(':');
                        if (colon>0){ u.id = line.substring(0, colon); u.password = line.substring(colon+1); users.add(u); }
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Failed to load users: "+e.getMessage());
        }
    }

    public static void saveUsers(){
        String base = "backend/data/users.txt";
        try{
            Path p = Paths.get(base);
            if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
            List<String> lines = new ArrayList<>();
            for(backend.models.User u: users){
                lines.add((u.id==null?"":u.id)+"|"+(u.email==null?"":u.email)+"|"+(u.password==null?"":u.password));
            }
            Files.write(p, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException e){
            System.out.println("Failed to save users: "+e.getMessage());
        }
    }

    public static void loadAlerts(){
        alerts.clear();
        String base = "backend/data/alerts.txt";
        if (!Files.exists(Paths.get(base))) {
            if (Files.exists(Paths.get("data/alerts.txt"))) base = "data/alerts.txt";
        }
        try{
            if (Files.exists(Paths.get(base))){
                for(String line: Files.readAllLines(Paths.get(base))){
                    int pipe = line.indexOf('|');
                    if (pipe>0){
                        backend.models.Alert a = new backend.models.Alert();
                        a.className = line.substring(0, pipe);
                        a.message = line.substring(pipe+1);
                        alerts.add(a);
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Failed to load alerts: "+e.getMessage());
        }
    }

    public static void loadSchedules(){
        schedules.clear();
        String base = "backend/data/schedules.txt";
        if (!Files.exists(Paths.get(base))) {
            if (Files.exists(Paths.get("data/schedules.txt"))) base = "data/schedules.txt";
        }
        try{
            if (Files.exists(Paths.get(base))){
                for(String line: Files.readAllLines(Paths.get(base))){
                    int pipe = line.indexOf('|');
                    if (pipe>0){
                        backend.models.Schedule s = new backend.models.Schedule();
                        s.userId = line.substring(0, pipe);
                        String rest = line.substring(pipe+1);
                        s.classes = new ArrayList<>();
                        if (!rest.isBlank()) {
                            for(String c : rest.split(",")) { if (!c.isBlank()) s.classes.add(c.trim()); }
                        }
                        schedules.add(s);
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Failed to load schedules: "+e.getMessage());
        }
    }

    public static void saveSchedules(){
        String base = "backend/data/schedules.txt";
        try{
            Path p = Paths.get(base);
            if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
            List<String> lines = new ArrayList<>();
            for(backend.models.Schedule s: schedules){
                String joined = (s.classes==null?"":String.join(",", s.classes));
                lines.add((s.userId==null?"":s.userId) + "|" + joined);
            }
            Files.write(p, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException e){
            System.out.println("Failed to save schedules: "+e.getMessage());
        }
    }

}
