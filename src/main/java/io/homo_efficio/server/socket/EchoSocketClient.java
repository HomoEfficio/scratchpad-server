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
            out.println(message);
            out.flush();
            String messageFromServer;
            while (true) {
                if ((messageFromServer = in.readLine()) != null) {
                    System.out.println("OOO Echo from Server: " + messageFromServer);
                    break;
                }
            }
        }
    }
}
