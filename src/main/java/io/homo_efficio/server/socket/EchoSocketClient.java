package io.homo_efficio.server.socket;

import java.io.*;
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
        FileOutputStream fos = Utils.getCommonFileOutputStream();
        Utils.clientTimeStamp("Client 시작", fos);

        try (Socket clientSocket = new Socket(SERVER_HOST_NAME, Constants.SERVER_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            Utils.sleep(800L);
            Utils.clientTimeStamp("메시지 전송 시작", fos);
            out.println(message);
            Utils.clientTimeStamp("메시지 print 완료", fos);
            out.flush();
            Utils.clientTimeStamp("메시지 flush 완료", fos);
            Utils.clientTimeStamp("서버 Echo 대기...", fos);
            // in.readLine() 은 읽을 데이터가 들어올 때까지 blocking 이므로 while (true) 불필요
            String messageFromServer = in.readLine();
            Utils.clientTimeStamp("서버 Echo 도착", fos);
            Utils.clientTimeStamp("서버 Echo msg: " + messageFromServer, fos);
        }
    }

}
