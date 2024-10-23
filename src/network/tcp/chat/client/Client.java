package network.tcp.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static io.util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class Client {

    private final String host;
    private final int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private ReadHandler readHandler;
    private WriteHandler writeHandler;
    private boolean closed = false;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        this.socket = new Socket(host, port);
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());

        this.readHandler = new ReadHandler(input, this);
        this.writeHandler = new WriteHandler(output, this);

        Thread readThread = new Thread(readHandler, "readThread");
        Thread writeThread = new Thread(writeHandler, "writeThread");

        readThread.start();
        writeThread.start();
    }

    public synchronized void close() {
        if (closed) return;

        readHandler.close();
        writeHandler.close();
        closeAll(socket, input, output);
        closed = true;
        log("Client 종료");
    }
}
