package backend;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args){
        System.out.println("ClassConnect Backend Running...");
        try {
            Database.loadAll();
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/login", Main::handleLogin);
            server.createContext("/api/register", Main::handleRegister);
            server.createContext("/api/alerts", Main::handleAlerts);
            server.createContext("/api/proxy-image", Main::handleProxyImage);
            server.createContext("/api/schedule", Main::handleSchedule);
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            server.start();
            System.out.println("HTTP server listening on http://localhost:8080");
        } catch (IOException e) {
            System.out.println("Failed to start HTTP server: " + e.getMessage());
        }
    }

    private static void handleLogin(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        String id = extractField(body, "id");
        String pw = extractField(body, "pw");

        boolean ok = new backend.controllers.LoginController().login(id, pw);
        String resp = ok ? "{\"ok\":true}" : "{\"ok\":false}";
        byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);

        addCors(exchange);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(ok ? 200 : 401, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    static void handleRegister(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String id = extractField(body, "id");
        String email = extractField(body, "email");
        String pw = extractField(body, "pw");
        String confirm = extractField(body, "confirm");
        boolean ok = new backend.controllers.RegistrationController().register(id, email, pw, confirm);
        String resp = ok ? "{\"ok\":true}" : "{\"ok\":false}";
        byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
        addCors(exchange);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(ok ? 200 : 400, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static void addCors(HttpExchange exchange) {
        Headers h = exchange.getResponseHeaders();
        h.add("Access-Control-Allow-Origin", "*");
        h.add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        h.add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static String extractField(String json, String key) {
        String needle = "\"" + key + "\"";
        int i = json.indexOf(needle);
        if (i < 0) return "";
        int colon = json.indexOf(':', i);
        if (colon < 0) return "";
        int start = json.indexOf('"', colon + 1);
        if (start < 0) return "";
        int end = json.indexOf('"', start + 1);
        if (end < 0) return "";
        return json.substring(start + 1, end);
    }

    private static void handleAlerts(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0;i<Database.alerts.size();i++){
            backend.models.Alert a = Database.alerts.get(i);
            sb.append("{\"class\":\"")
              .append(a.className==null?"":escape(a.className))
              .append("\",\"message\":\"")
              .append(a.message==null?"":escape(a.message))
              .append("\"}");
            if (i < Database.alerts.size()-1) sb.append(",");
        }
        sb.append("]");
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        addCors(exchange);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String escape(String s){
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void handleProxyImage(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCors(exchange);
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }
        String q = exchange.getRequestURI().getQuery();
        String url = null;
        if (q != null) {
            for (String part : q.split("&")) {
                int eq = part.indexOf('=');
                String k = eq>0 ? part.substring(0,eq) : part;
                String v = eq>0 ? part.substring(eq+1) : "";
                if ("url".equals(k)) { url = java.net.URLDecoder.decode(v, java.nio.charset.StandardCharsets.UTF_8); }
            }
        }
        if (url == null || url.isBlank()) {
            addCors(exchange);
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
            return;
        }
        try {
            java.net.URL u = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", "ClassConnect/1.0");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            int code = conn.getResponseCode();
            if (code != 200) { addCors(exchange); exchange.sendResponseHeaders(502, -1); exchange.close(); return; }
            String ct = conn.getContentType();
            byte[] data;
            try (java.io.InputStream is = conn.getInputStream()) { data = is.readAllBytes(); }
            addCors(exchange);
            exchange.getResponseHeaders().add("Content-Type", ct != null ? ct : "image/jpeg");
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
        } catch (Exception e) {
            addCors(exchange);
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }

    private static void handleSchedule(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { addCors(exchange); exchange.sendResponseHeaders(204, -1); exchange.close(); return; }
        addCors(exchange);
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String q = exchange.getRequestURI().getQuery();
            String userId = null;
            if (q != null) for (String part : q.split("&")) { int eq = part.indexOf('='); String k = eq>0?part.substring(0,eq):part; String v = eq>0?part.substring(eq+1):""; if ("userId".equals(k)) userId = java.net.URLDecoder.decode(v, java.nio.charset.StandardCharsets.UTF_8); }
            java.util.List<backend.models.Schedule> list = Database.schedules;
            backend.models.Schedule found = null;
            if (userId != null) for (backend.models.Schedule s : list) { if (userId.equals(s.userId)) { found = s; break; } }
            String resp = found==null?"{\"classes\":[]}" : "{\"classes\":[" + (sClasses(found)) + "]}";
            byte[] bytes = resp.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type","application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
            return;
        }
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            String userId = extractField(body, "userId");
            String classesStr = extractField(body, "classes");
            String action = extractField(body, "action");
            if (userId.isBlank()) { exchange.sendResponseHeaders(400, -1); exchange.close(); return; }
            java.util.List<String> classes = new java.util.ArrayList<>();
            if (!classesStr.isBlank()) for (String c : classesStr.split(",")) { if (!c.isBlank()) classes.add(c.trim()); }
            backend.models.Schedule found = null;
            for (backend.models.Schedule s : Database.schedules) { if (userId.equals(s.userId)) { found = s; break; } }
            if (found == null) { found = new backend.models.Schedule(); found.userId = userId; found.classes = new java.util.ArrayList<>(); Database.schedules.add(found); }
            if ("set".equalsIgnoreCase(action)) {
                java.util.List<String> normalized = new java.util.ArrayList<>();
                for (String c : classes) {
                    String uc = c.toUpperCase();
                    if (!isValidCode(uc)) { exchange.sendResponseHeaders(400, -1); exchange.close(); return; }
                    normalized.add(uc);
                }
                found.classes = normalized;
            } else {
                if (found.classes == null) found.classes = new java.util.ArrayList<>();
                for (String c : classes) {
                    String uc = c.toUpperCase();
                    if (!isValidCode(uc)) { exchange.sendResponseHeaders(400, -1); exchange.close(); return; }
                    if (!found.classes.contains(uc)) found.classes.add(uc);
                }
            }
            Database.saveSchedules();
            byte[] bytes = "{\"ok\":true}".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type","application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(405, -1);
        exchange.close();
    }


    private static String sClasses(backend.models.Schedule s){
        if (s == null || s.classes == null || s.classes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<s.classes.size();i++){
            sb.append("\"").append(escape(s.classes.get(i))).append("\"");
            if (i<s.classes.size()-1) sb.append(",");
        }
        return sb.toString();
    }

    private static boolean isValidCode(String code){
        if (code == null) return false;
        if (code.length() != 6) return false;
        for (int i=0;i<3;i++){ char ch = code.charAt(i); if (!(ch>='A' && ch<='Z')) return false; }
        for (int i=3;i<6;i++){ char ch = code.charAt(i); if (!(ch>='0' && ch<='9')) return false; }
        return true;
    }

}
