package Nio.Channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class test3 {
    public static void main(String[] args) throws Exception {
        // 获取ServerSocketChannel，相当于服务端的服务通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);

        // 绑定地址，端口号
        serverSocketChannel.bind(address);

        // 创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        while (true) {
            // 获取客户端的SocketChannel
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 客户端读入内容
            while (socketChannel.read(allocate) != -1) {
                // 输出内容
                System.out.println(new String(allocate.array()));
                // 清空，实际就是将position和limit归位
                allocate.clear();
            }
        }
    }
}
