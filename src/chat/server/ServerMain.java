package chat.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        SessionManager sessionManager = new SessionManager();
        CommandManager commandManager = new CommandManagerV1(sessionManager);
        Server server = new Server(12345, sessionManager, commandManager);
        server.start();
    }
}
