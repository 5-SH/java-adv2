package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class Server {

    private final int port;
    private final SessionManager sessionManager;
    private final CommandManager commandManager;

    public Server (int port, SessionManager sessionManager, CommandManager commandManager) {
        this.port = port;
        this.sessionManager = sessionManager;
        this.commandManager = commandManager;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        addShutdownHook(serverSocket);

        running(serverSocket);
    }

    private void addShutdownHook(ServerSocket serverSocket) {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(serverSocket, sessionManager), "shutdown"));
    }

    private void running(ServerSocket serverSocket) {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                log("소캣 연결: " + socket);
                Thread thread = new Thread(new Session(socket, sessionManager, commandManager));
                thread.start();
            }
        } catch (IOException e) {
            log("서버 소캣 종료: " + e);
        }
    }

    static class ShutdownHook implements Runnable {
        private final ServerSocket serverSocket;
        private final SessionManager sessionManager;

        public ShutdownHook(ServerSocket serverSocket, SessionManager sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {
            log("shutdownHook 실행");
            try {
                serverSocket.close();
                sessionManager.closeAll();

                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                log(e);
            }
        }
    }
}
