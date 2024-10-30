package chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private final ArrayList<Session> sessions= new ArrayList<>();

    public void add(Session session) {
        sessions.add(session);
    }

    public void remove(Session session) {
        sessions.remove(session);
    }

    public void sendAll(String message) {
        try {
            for (Session session : sessions) session.send(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<String> getAllUsername() {
        List<String> usernames = new ArrayList<>();
        for (Session session : sessions) {
            if (!session.getUsername().isEmpty()) {
                usernames.add(session.getUsername());
            }
        }

        return usernames;
    }

    public synchronized void closeAll() {
        for (Session session : sessions) session.close();
        sessions.clear();
    }
}
