package network.tcp.exercise;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class Server {

    static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("서버 시작");

        SessionManager sessionManager = new SessionManager();
        ServerSocket serverSocket = new ServerSocket(PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownHook(serverSocket, sessionManager), "shutdown"));
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new Session(sessionManager, socket));
                thread.start();
            }
        } catch (IOException e) {
            log(e);
        }
    }

    static class ShutDownHook implements Runnable {

        ServerSocket serverSocket;
        SessionManager sessionManager;

        public ShutDownHook(ServerSocket serverSocket, SessionManager sessionManager) {
            this.serverSocket = serverSocket;
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {
            try {
                serverSocket.close();
                sessionManager.closeAll();

                Thread.sleep(1000);
            } catch (Exception e) {
                log(e);
            }
        }
    }
}
