package io.homo_efficio.server.channel;

import io.homo_efficio.server.common.Constants;
import io.homo_efficio.server.common.EchoProcessor;
import io.homo_efficio.server.common.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-10-30
 */
public class EchoSelectorServer {

    public static void main(String[] args) throws IOException {
        EchoSelectorServer echoSelectorServer = new EchoSelectorServer();
        echoSelectorServer.start();
    }

    public void start() throws IOException {
        FileOutputStream fos = Utils.getCommonFileOutputStream();
        Utils.serverTimeStamp("===============================", fos);
        Utils.serverTimeStamp("Selector Channel Echo Server 시작", fos);
        Selector selector = Selector.open();
        Utils.serverTimeStamp("Selector 오픈됨: " + selector, fos);
        // selectedKeys 는 select() 에 의해 집계된 ready selection keys
        Utils.serverTimeStamp("selector.selectedKeys().size(): " + selector.selectedKeys().size(), fos);  // 0
        // keys 는 register() 로 등록된 registered selection keys
        Utils.serverTimeStamp("selector.keys().size(): " + selector.keys().size(), fos);  // 0

        // Listening 전용 ServerSocketChannel. 서버가 종료될 때까지 close() 하면 안 됨
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
                .bind(new InetSocketAddress(Constants.SERVER_HOST_NAME, Constants.SERVER_PORT));
        int validOps = serverSocketChannel.validOps();  // ServerSocketChannel 은 항상 SelectionKey.OP_ACCEPT 만 반환
        Utils.serverTimeStamp("serverSocketChannel.validOps(): " + validOps, fos);

        SelectableChannel selectableChannel = serverSocketChannel.configureBlocking(false);

        // Listening 전용 ServerSocketChannel 하나를 Selector에 등록
        SelectionKey severSocketChannelSK = serverSocketChannel.register(selector,
                SelectionKey.OP_ACCEPT);  // AbstractSelectableChannel.register() 에서 validOps()와 다른 값은 IllegalArguentException 유발

        Utils.serverTimeStamp("serverSocketChannel을 selector에 등록 완료", fos);
        Utils.serverTimeStamp("selector.selectedKeys().size(): " + selector.selectedKeys().size(), fos);  // 0
        Utils.serverTimeStamp("selector.keys().size(): " + selector.keys().size(), fos);  // 1

        // Interest Set: channel 이 selector에 register 될 때 지정된 ops 값
        int interestSet1 = severSocketChannelSK.interestOps();
        Utils.serverTimeStamp("severSocketChannelSK.interestOps()  : " + interestSet1, fos);
        Utils.serverTimeStamp("severSocketChannelSK.isInterestedInConnect: " + (interestSet1 & SelectionKey.OP_CONNECT), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isInterestedInAccept : " + (interestSet1 & SelectionKey.OP_ACCEPT), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isInterestedInRead   : " + (interestSet1 & SelectionKey.OP_READ), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isInterestedInWrite  : " + (interestSet1 & SelectionKey.OP_WRITE), fos);

        // 아래는 모두 false 반환. 즉 serverSocketChannel 은 register 만 됐을 뿐 ready 상태가 아님
        int readyOps1 = severSocketChannelSK.readyOps();
        Utils.serverTimeStamp("severSocketChannelSK.readyOps()  : " + readyOps1, fos);
        Utils.serverTimeStamp("severSocketChannelSK.isConnectable(): " + severSocketChannelSK.isConnectable(), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isAcceptable(): " + severSocketChannelSK.isAcceptable(), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isReadable(): " + severSocketChannelSK.isReadable(), fos);
        Utils.serverTimeStamp("severSocketChannelSK.isWritable(): " + severSocketChannelSK.isWritable(), fos);

        ByteBuffer buf = ByteBuffer.allocate(256);

        while (true) {
            Utils.serverTimeStamp("---------------------------", fos);
            Utils.serverTimeStamp("Single Thread Socket Echo Server 대기 중", fos);

            // selector.select(): 클라이언트 요청이 들어올 때까지 blocking 대기
            // selectedCount: selector() 호출 시점에서 집계된 selectedKeys()의 갯수
            //     최초에는 1이 반환되고, select() 가 호출되고 후속 처리 후 select() 가 다시 호출될 때까지
            //     여러 요청이 들어와서 ready 인 SelectionKey, 즉 selectedKeys()가 여럿 있으면,
            //     다음 select() 는 selectedKeys().size() 반환
            //     serverSocketChannel 이 하나의 clientChannel 씩만 accept() 해서 selector 에 등록하므로
            //     selectedKeys().size() 는 현실적으로는 항상 많아야 2 이다.
            // 클라이언트 요청이 들어오면 serverSocketChannel 이 먼저 ready[Acceptable] 된다.
            int selectedCount = selector.select();

            Utils.serverTimeStamp("Client 접속!!!, selectedCount: " + selectedCount, fos);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Utils.serverTimeStamp("selector.selectedKeys().size(): " + selector.selectedKeys().size(), fos);
            Utils.serverTimeStamp("selector.keys().size(): " + selector.keys().size(), fos);
            Iterator<SelectionKey> itSelectedKeys = selectedKeys.iterator();

            while (itSelectedKeys.hasNext()) {
                Utils.serverTimeStamp("+++++++++++++++++++", fos);
                // SelectedKey
                SelectionKey selectedKey = itSelectedKeys.next();
                Utils.serverTimeStamp("selectedKey.isValid(): " + selectedKey.isValid(), fos);
                Utils.serverTimeStamp("selectedKey.selector(): " + selectedKey.selector(), fos);
                Utils.serverTimeStamp("selectedKey.channel(): " + selectedKey.channel(), fos);

                // Interest Ops: channel 이 selector에 register 될 때 지정된 ops 값
                int interestOps2 = selectedKey.interestOps();
                Utils.serverTimeStamp("selectedKey.interestOps()  : " + interestOps2, fos);
                Utils.serverTimeStamp("selectedKey.isInterestedInConnect: " + (interestOps2 & SelectionKey.OP_CONNECT), fos);
                Utils.serverTimeStamp("selectedKey.isInterestedInAccept : " + (interestOps2 & SelectionKey.OP_ACCEPT), fos);
                Utils.serverTimeStamp("selectedKey.isInterestedInRead   : " + (interestOps2 & SelectionKey.OP_READ), fos);
                Utils.serverTimeStamp("selectedKey.isInterestedInWrite  : " + (interestOps2 & SelectionKey.OP_WRITE), fos);

                // Ready Ops: select() 의 반환을 유발하는 Ready SelectedKey 의 ops 값
                int readyOps2 = selectedKey.readyOps();
                Utils.serverTimeStamp("selectedKey.readyOps()  : " + readyOps2, fos);
                Utils.serverTimeStamp("selectedKey.isConnectable: " + selectedKey.isConnectable(), fos);
                Utils.serverTimeStamp("selectedKey.isAcceptable : " + selectedKey.isAcceptable(), fos);
                Utils.serverTimeStamp("selectedKey.isReadable  : " + selectedKey.isReadable(), fos);
                Utils.serverTimeStamp("selectedKey.isWritable   : " + selectedKey.isWritable(), fos);

                if (selectedKey.isAcceptable()) {
                    Utils.serverTimeStamp("SelectionKey is Acceptable", fos);
                    Utils.serverTimeStamp("serverSocketChannel.equals(selectedKey.channel()): " + serverSocketChannel.equals(selectedKey.channel()), fos);
                    // serverSocketChannel.accept() 로 (Client)SocketChannel 을 얻고
                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
                    Utils.serverTimeStamp("clientSocketChannel: " + clientSocketChannel, fos);
                    clientSocketChannel.configureBlocking(false);
                    // (Client)SocketChannel 을 selector 에 등록한다.
                    clientSocketChannel.register(selector, SelectionKey.OP_READ);
                    Utils.serverTimeStamp("새 Client Channel을 OP_READ 로 Selector에 등록 완료", fos);
                    // 다음 selector.select() 가 호출될 때 clientSocketChannel 은 ready[Readable] 상태로 selector의 selectedKeys에 포함
                }

                if (selectedKey.isReadable()) {
                    Utils.serverTimeStamp("SelectionKey is Readable", fos);
                    SocketChannel clientChannel = (SocketChannel) selectedKey.channel();
                    // echo
                    EchoProcessor.echo(clientChannel);

                    // clientChannel.close()를 하지 않으면 selector에 register 된 채로 계속 남아서,
                    // 최상위 while (true) 문 안에 있는 selector.select() 가 계속 1을 반환하면서 무한루프 돌게됨
                    // clientChannel.unregister() 같은 메서드가 없으므로 channel.close() 가 비슷한 효과
                    clientChannel.close();
                }
                // iter의 모태가 되는 컬렉션은 selector.selectedKeys() 다.
                // 따라서 처리 완료돤 SelectionKey 를 제거하지 않으면,
                // selector 에 계속 남아 있게 되고,
                // 다음 번 select() 에 의해 iteration 을 돌 때
                // serverSocketChannel 이 non-blocking 일 경우 클라이언트와 연결된 connection이 없으면
                // serverSocketChannel.accept() 이 null을 반환하고 결국 NullPointerException 이 발생한다.
                itSelectedKeys.remove();
                Utils.serverTimeStamp("+++++++++++++++++++", fos);
            }
        }
    }
}
