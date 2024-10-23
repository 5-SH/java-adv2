package network.tcp.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static io.util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private final CommandManager commandManager;
    private boolean closed = false;
    private String username;

    public Session(Socket socket, SessionManager sessionManager, CommandManager commandManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.commandManager = commandManager;
        this.sessionManager = sessionManager;
        this.sessionManager.add(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String received = input.readUTF();
                log("client -> server: " + received);
                commandManager.execute(received, this);
            }
        } catch (IOException e) {
            log(e);
        } finally {
            sessionManager.remove(this);
            sessionManager.sendAll(username + "님이 퇴장했습니다.");
            close();
        }
    }

    public void send(String message) throws IOException {
        log("server -> client: " + message);
        output.writeUTF(message);
    }

    public synchronized void close() {
        if (closed) return;

        closeAll(socket, input, output);
        closed = true;
        log("연결 종료: " + socket);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
