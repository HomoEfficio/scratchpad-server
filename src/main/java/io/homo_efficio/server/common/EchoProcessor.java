package io.homo_efficio.server.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-10
 */
public abstract class EchoProcessor {

    public static void echo(Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream())
        ) {
            String clientMessage = in.readLine();
            String serverMessage = "Server Echo - " + clientMessage + System.lineSeparator();

//            System.out.println("------------------");
//            System.out.println(serverMessage);
//            System.out.println("------------------");

            out.println(serverMessage);
            out.flush();
        }
    }

    public static void echo(SocketChannel socketChannel) throws IOException {
//        ByteBuffer buf = ByteBuffer.allocateDirect(256);  // backed array 가 없으므로 buf.array() 에서 UnsupportedOperationException 유발
        ByteBuffer buf = ByteBuffer.allocate(256);
        int readBytes = socketChannel.read(buf);
//        System.out.println("readBytes: " + readBytes);
        String serverMessage = "Server Echo - " + new String(buf.array()).trim() + System.lineSeparator();
//        System.out.println("serverMessage: " + serverMessage);
        buf.clear();
        buf.put(serverMessage.getBytes(StandardCharsets.UTF_8));
        buf.flip();
        socketChannel.write(buf);
    }
}
