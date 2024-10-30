package network.tcp.exercise;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class Session implements Runnable {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private String name;
    private boolean closed = false;

    public Session(SessionManager sessionManager, Socket socket) throws IOException {
        this.name = "";
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String received = input.readUTF();
                String[] data = received.split("\\|");

                String command = data[0];
                String content = "";
                if (data.length > 1) content = data[1];

                switch (command) {
                    case "/join":
                        if (sessionManager.hasName(content)) {
                            output.writeUTF("이미 있는 이름 입니다.");
                            break;
                        }

                        setName(content);
                        sessionManager.add(this);
                        output.writeUTF("이름을 추가 했습니다.");
                        break;
                    case "/message":
                        for (String n : sessionManager.getNameAll()) {
                            if (!name.equals(n)) {
                                sessionManager.getSession(n).output.writeUTF("[" + name + "] " + content);
                            }
                        }
                        output.writeUTF("메시지를 전송했습니다.");
                        break;
                    case "/change":
                        String oldName = this.name;
                        setName(content);
                        sessionManager.change(oldName, content, this);
                        output.writeUTF("이름을 변경 했습니다.");
                        break;
                    case "/users":
                        output.writeUTF(String.join(", ", new ArrayList<>(sessionManager.getNameAll())));
                        break;
                    case "/exit":
                        close();
                        return;
                    default:
                        output.writeUTF("잘못된 입력 입니다.");
                }
            }
        } catch (IOException e) {
            log(e);
        } finally {
            sessionManager.remove(this);
            close();
        }
    }

    public synchronized void close() {
        if (closed) return;

        closeAll(socket, input, output);
        closed = true;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
