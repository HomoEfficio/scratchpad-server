package io.homo_efficio.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-10
 */
public class EchoProcessor {

    public void echo(Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream());
        ) {

            String clientMessage = in.readLine();
            System.out.println("Msg from client: " + clientMessage);
            String serverMessage = "Server Echo - " + clientMessage + "\n";

            out.println(serverMessage);
            out.flush();
        }
    }
}
