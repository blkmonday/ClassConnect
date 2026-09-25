package backend.controllers;

import backend.Database;

public class LoginController {
    public boolean login(String id,String pw){
        if (id == null || pw == null) return false;
        for (backend.models.User u : Database.users){
            if (id.equals(u.id) && pw.equals(u.password)) return true;
        }
        return false;
    }
}
