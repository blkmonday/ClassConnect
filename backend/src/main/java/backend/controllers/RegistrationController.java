package backend.controllers;

import backend.Database;
import backend.models.User;

public class RegistrationController {
    public boolean register(String id, String email, String pw, String confirm){
        if (id == null || id.isBlank()) return false;
        if (email == null || email.isBlank()) return false;
        if (!isSusEmail(email)) return false;
        if (pw == null || confirm == null || pw.isBlank()) return false;
        if (!pw.equals(confirm)) return false;
        if (!hasUpperAndDigit(pw)) return false;
        for (User u : Database.users){
            if (u.id != null && u.id.equals(id)) return false;
            if (u.email != null && u.email.equalsIgnoreCase(email)) return false;
        }
        User u = new User();
        u.id = id;
        u.email = email.toLowerCase();
        u.password = pw;
        Database.users.add(u);
        Database.saveUsers();
        return true;
    }

    private boolean isSusEmail(String email){
        String e = email.toLowerCase();
        return e.endsWith("@sus.edu") && e.indexOf('@') > 0;
    }

    private boolean hasUpperAndDigit(String pw){
        boolean upper=false,digit=false;
        for(char c: pw.toCharArray()){ if(Character.isUpperCase(c)) upper=true; if(Character.isDigit(c)) digit=true; }
        return upper && digit;
    }
}
