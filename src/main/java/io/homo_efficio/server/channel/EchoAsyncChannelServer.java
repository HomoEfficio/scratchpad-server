package io.homo_efficio.server.channel;

import io.homo_efficio.server.common.Constants;
import io.homo_efficio.server.common.EchoProcessor;
import io.homo_efficio.server.common.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-31
 */
public class EchoAsyncChannelServer {

    public static void main(String[] args) throws IOException {
        EchoAsyncChannelServer echoSelectorServer = new EchoAsyncChannelServer();
        echoSelectorServer.start();
    }

    public void start() throws IOException {
        ExecutorService es = Utils.getCommonExecutorService();
        try (FileOutputStream fos = Utils.getCommonFileOutputStream();
             // Listening 전용 ServerSocketChannel. 서버가 종료될 때까지 close() 하면 안 됨
             AsynchronousServerSocketChannel asyncServerSocketChannel =
                     AsynchronousServerSocketChannel
                             .open(AsynchronousChannelGroup.withThreadPool(es))
                             .bind(new InetSocketAddress(Constants.SERVER_HOST_NAME, Constants.SERVER_PORT))
        ) {
            Utils.serverTimeStamp("===============================", fos);
            Utils.serverTimeStamp("Selector Async Channel Echo Server 시작", fos);

            Utils.serverTimeStamp("asyncServerSocketChannel open, bind 완료", fos);

            Utils.serverTimeStamp("Async Channel Echo Server accept()", fos);
            asyncServerSocketChannel.accept(null,
                    new CompletionHandler<AsynchronousSocketChannel, Object>() {
                        @Override
                        public void completed(AsynchronousSocketChannel asyncClientChannel, Object attachment) {
                            Utils.serverTimeStamp("---------------------------", fos);
                            Utils.serverTimeStamp("Async Channel Echo Server accept completed", fos);
                            EchoProcessor.echo(asyncClientChannel, es);

                            // while 대신 재귀
                            asyncServerSocketChannel.accept(null, this);
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            Utils.serverTimeStamp(exc.getLocalizedMessage(), fos);
                        }
                    });
            // 서버 종료 방지
            System.in.read();
        }
    }
}
