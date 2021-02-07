package Nio.Group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;

    public GroupChatClient() throws IOException {
        this.selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(userName + "is ok~");
    }
    // 发送消息
    private void sendMsg(String msg) throws IOException {
        msg = userName + "说：" + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    private void readMsg() throws IOException {

        int count = selector.select();
        if (count > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array()));
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();
        new Thread(() -> {
            while (true) {
                try {
                    chatClient.readMsg();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sendMsg(msg);
        }
    }

}