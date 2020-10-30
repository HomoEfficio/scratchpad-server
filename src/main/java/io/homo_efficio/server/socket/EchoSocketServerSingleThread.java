package io.homo_efficio.server.socket;

import io.homo_efficio.server.common.Constants;
import io.homo_efficio.server.common.EchoProcessor;
import io.homo_efficio.server.common.Utils;

import java.io.FileOutputStream;
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
        FileOutputStream fos = Utils.getCommonFileOutputStream();
        Utils.serverTimeStamp("===============================", fos);
        Utils.serverTimeStamp("Echo Server 시작", fos);

        while (true) {
            Utils.serverTimeStamp("---------------------------", fos);
            Utils.serverTimeStamp("Single Thread Socket Echo Server 대기 중", fos);
            // accept() 는 연결 요청이 올 때까지 return 하지 않고 blocking
            Socket acceptedSocket = serverSocket.accept();

            // 연결 요청이 오면 accept() 가 반환하고 요청 처리 로직 수행
            Utils.serverTimeStamp("Client 접속!!!", fos);
            Utils.sleep(5000L);
            EchoProcessor echoProcessor = new EchoProcessor();
            Utils.serverTimeStamp("Echo 시작", fos);
            echoProcessor.echo(acceptedSocket);
            Utils.serverTimeStamp("Echo 완료", fos);
        }
    }

}
