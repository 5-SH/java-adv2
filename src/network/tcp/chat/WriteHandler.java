package network.tcp.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static io.util.MyLogger.log;

public class WriteHandler implements Runnable {

    private final DataOutputStream output;
    private final Client client;
    private final String DELEMITER = "|";
    private boolean closed = false;

    public WriteHandler(DataOutputStream output, Client client) {
        this.output = output;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            String username = inputUsername(scanner);
            output.writeUTF("/join" + DELEMITER + username);

            while (true) {
                String toSend = scanner.nextLine();

                if (toSend.equals("/exit")) {
                    output.writeUTF(toSend);
                    break;
                }

                if (toSend.startsWith("/")) {
                    output.writeUTF(toSend);
                } else {
                    output.writeUTF("/message" + DELEMITER + toSend);
                }
            }
        } catch (IOException | NoSuchElementException e) {
            log(e);
        } finally {
            client.close();
        }
    }

    private static String inputUsername(Scanner scanner) {
        System.out.println("이름을 입력하세요.");
        String username;
        do {
            username = scanner.nextLine();
        } while (username.isEmpty());
        return username;
    }

    public synchronized void close() {
        if (closed) return;

        try {
            System.in.close();
        } catch (IOException e) {
            log(e);
        }

        closed = true;
        log("write handler 종료");
    }
}
