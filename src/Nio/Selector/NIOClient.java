package Nio.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        socketChannel.configureBlocking(false);
        // 连接服务器
        boolean connect = socketChannel.connect(address);
        if (!connect) {
            // 等待连接，判断是否连接
            while (!socketChannel.finishConnect()) {
                System.out.println("连接服务器中...............");
            }
        }

        String msg = "hello server";
        ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
        // 将数据写入到通道之中
        socketChannel.write(wrap);
        // 防止程序停止
        System.in.read();
    }
}