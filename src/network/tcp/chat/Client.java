package network.tcp.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static io.util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class Client {

    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final ReadHandler readHandler;
    private final WriteHandler writeHandler;
    private boolean closed = false;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.readHandler = new ReadHandler(input, this);
        this.writeHandler = new WriteHandler(output, this);
    }

    public void start() {
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
