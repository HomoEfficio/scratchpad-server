package io.homo_efficio.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-14
 */
public class EchoSocketServerMultiThread {

    public static void main(String[] args) throws IOException {
        EchoSocketServerMultiThread echoSocketServerMultiThread = new EchoSocketServerMultiThread();
        echoSocketServerMultiThread.start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
        System.out.printf("[%s] Echo Server 시작\n", Thread.currentThread().getName());
        System.out.printf("[%s] Echo Server 대기 중\n", Thread.currentThread().getName());

        // 스레드 풀
        ExecutorService es = Executors.newFixedThreadPool(2);

        while (true) {
            // accept() 는 연결 요청이 올 때까지 return 하지 않고 blocking
            Socket acceptedSocket = serverSocket.accept();

            // 연결 요청이 오면 accept() 가 반환하고 요청 처리 로직 수행
//            System.out.println("Client 접속!!!");
//            EchoProcessor echoProcessor = new EchoProcessor();
//            echoProcessor.echo(acceptedSocket);

            //// 연결 요청이 오면 새 thread 에서 요청 처리 로직 수행
            es.execute(() -> {
                System.out.printf("[%s] Client 접속!!!\n", Thread.currentThread().getName());
                EchoProcessor echoProcessor = new EchoProcessor();
                try {
                    echoProcessor.echo(acceptedSocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
