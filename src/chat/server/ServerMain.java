package chat.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        SessionManager sessionManager = new SessionManager();
//        CommandManager commandManager = new CommandManagerV1(sessionManager);
//        CommandManager commandManager = new CommandManagerV2(sessionManager);
//        CommandManager commandManager = new CommandManagerV3(sessionManager);
        CommandManager commandManager = new CommandManagerV4(sessionManager);
        Server server = new Server(12345, sessionManager, commandManager);
        server.start();
    }
}
