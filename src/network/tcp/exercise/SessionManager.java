package network.tcp.exercise;

import java.util.HashMap;
import java.util.Set;

public class SessionManager {

    private HashMap<String, Session> sessions = new HashMap<>();

    public synchronized void add(Session session) {
        sessions.put(session.getName(), session);
    }

    public synchronized void change(String oldName, String newName, Session session) {
        remove(oldName);
        add(session);
    }

    public synchronized void remove(String name) {
        sessions.remove(name);
    }

    public synchronized void remove(Session session) {
        sessions.remove(session.getName());
    }

    public synchronized boolean hasName(String name) {
        return sessions.containsKey(name);
    }

    public synchronized Set<String> getNameAll() {
        return sessions.keySet();
    }

    public synchronized Session getSession(String name) {
        return sessions.get(name);
    }

    public synchronized void closeAll() {
        for (Session s : sessions.values()) s.close();
        sessions.clear();
    }
}
