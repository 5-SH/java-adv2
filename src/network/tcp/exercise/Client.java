package network.tcp.exercise;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import static util.MyLogger.log;
import static network.tcp.SocketCloseUtil.closeAll;

public class Client {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", PORT), 5000);

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        Receiver receiver = new Receiver(input);
        Thread thread = new Thread(receiver, "Receiver");
        thread.start();

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String toSend = scanner.nextLine();

                output.writeUTF(toSend);
                log("client -> server: " + toSend);

                if (toSend.equals("/exit")) break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            receiver.stopRunning();
            closeAll(socket, input, output);
            log("연결 종료");
        }
    }

    static class Receiver implements Runnable {

        private DataInputStream input;
        private boolean running = true;

        public Receiver(DataInputStream input) {
            this.input = input;
        }

        public void stopRunning() {
            this.running = false;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    String received = input.readUTF();
                    log("client <- server: " + received);
                }
            } catch (IOException e) {
                log(e);
            } finally {
                log("Receiver 종료");
            }
        }
    }
}
