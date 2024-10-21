package network.tcp.v6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static io.util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class SessionV6 implements Runnable {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManagerV6 sessionManagerV6;
    private boolean closed = false;

    public SessionV6(Socket socket, SessionManagerV6 sessionManagerV6) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManagerV6 = sessionManagerV6;
        this.sessionManagerV6.add(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 클라이언트로부터 문자 받기
                String received = input.readUTF(); // 블로킹
                log("client -> server: " + received);

                // 클라이언트 종료 . 서버도 함께 종료
                if (received.equals("exit")) {
                    break;
                }

                // 클라이언트에게 문자 보내기
                String toSend = received + " World!";
                output.writeUTF(toSend);
                log("client - server: " + toSend);
            }
        } catch (IOException e) {
            log(e);
        } finally {
            sessionManagerV6.remove(this);
            close();
        }
    }

    public synchronized void close() {
        if (closed) {
            return;
        }
        closeAll(socket, input, output);
        closed = true;
        log("연결 종료: " + socket + " isClosed: " + socket.isClosed());
    }
}
