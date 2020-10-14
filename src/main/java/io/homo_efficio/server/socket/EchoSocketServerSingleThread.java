package io.homo_efficio.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-10
 */
public class EchoSocketServerSingleThread {


    public static void main(String[] args) throws IOException {
        EchoSocketServerSingleThread echoSocketServerSingleThread = new EchoSocketServerSingleThread();
        echoSocketServerSingleThread.start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
        System.out.println("Echo Server 시작");
        System.out.println("Echo Server 대기 중");

        while (true) {
            // accept() 는 연결 요청이 올 때까지 return 하지 않고 blocking
            Socket acceptedSocket = serverSocket.accept();

            // 연결 요청이 오면 accept() 가 반환하고 요청 처리 로직 수행
            System.out.println("Client 접속!!!");
            EchoProcessor echoProcessor = new EchoProcessor();
            echoProcessor.echo(acceptedSocket);
        }
    }

}
