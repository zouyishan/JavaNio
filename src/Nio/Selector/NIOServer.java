package Nio.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        // 打开ServerSocketChannel，绑定本机端口9999，然后设置为非阻塞
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
        serverSocketChannel.bind(inetSocketAddress);
        serverSocketChannel.configureBlocking(false);

        // 打开选择器
        Selector selector = Selector.open();

        // serverSocketChannel注册到选择器中，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(3000) == 0) {
                System.out.println("服务器等待3秒，没有连接");
                continue;
            }
            // 如果有事件 selector.select(3000) > 0 的情况，获取事件
            // 这里的事件我理解的是通过监听服务端，可以监听到很多连接，也就有了很多不同Client的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 获取迭代器遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                // 获取事件，也就是selectionKey
                SelectionKey selectionKey = iterator.next();

                // 连接事件
                if (selectionKey.isAcceptable()) {
                    // 获取客户端通道，这时客户端和服务端已经建立好了连接
                    SocketChannel accept = serverSocketChannel.accept();
                    System.out.println("连接成功");

                    // 设置为非阻塞
                    accept.configureBlocking(false);

                    // 把socketChannel注册到selector中，监听读事件， 并绑定一个缓冲区
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 读事件
                if (selectionKey.isReadable()) {
                    // 获取客户端通道
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    // 获取关联的ByteBuffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from 客户端： " + new String(buffer.array()));
                }
                // 删除已经有的事件
                iterator.remove();
            }

        }
    }
}












