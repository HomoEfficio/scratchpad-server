package io.homo_efficio.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-10
 *
 * 터미널에서 echo -n '아무거나' | nc localhost 7777 를 입력해도 서버에 데이터를 보낼 수 있다.
 */
public class EchoSocketClient {

    private static final String SERVER_HOST_NAME = "localhost";

    public static void main(String[] args) throws IOException {
        String message = "안녕, echo server";

        try (Socket clientSocket = new Socket(SERVER_HOST_NAME, EchoSocketServer.PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            sleep(5000L);
            out.println(message);
            out.flush();
            // in.readLine() 은 읽을 데이터가 들어올 때까지 blocking 이므로 while (true) 불필요
            String messageFromServer = in.readLine();
            System.out.println("OOO Echo from Server: " + messageFromServer);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
