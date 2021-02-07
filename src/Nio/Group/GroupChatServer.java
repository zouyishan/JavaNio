package Nio.Group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public static final int Port = 9999;
    public GroupChatServer() throws Exception {
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        this.serverSocketChannel.bind(new InetSocketAddress("localhost", Port));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把通道注册到选择器中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws Exception {
        while (true) {
            // 获取事件的总数
            int count = selector.select(2000);
            if (count > 0) {
                // 获取事件的集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 获取迭代器，感觉这个和分散读很像，呸，感觉就是分散读
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    // 一个单独的事件
                    SelectionKey key = iterator.next();
                    // 是否可以获取连接
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        // 设置为非阻塞
                        socketChannel.configureBlocking(false);
                        // 注册到选择器之中
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("连接成功");
                        System.out.println(socketChannel.getRemoteAddress() + "上线了~");
                    }

                    if (key.isReadable()) {
                        readData(key);
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("等着上线........");
            }
        }
    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            // 从selectionKey中获取channel
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println(socketChannel.getRemoteAddress() + "from 客户端: " + msg);
                notifyAllClient(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了......");
                // 取消注册
                selectionKey.cancel();
                // 关闭流
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void notifyAllClient(String msg, SocketChannel noNotifyChannel) throws IOException {
        System.out.println("服务器转发信息~");
        // 这里不能用selector.selectedKeys(), 这里是要给无论在线还是离线的人都发送消息。
        // 用这个就是只是给无论离线还是在线的人都发送
        for (SelectionKey selectionKey : selector.keys()) {
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel != noNotifyChannel) {
                // 转成SocketChannel类型
                SocketChannel socketChannel = (SocketChannel) channel;
                // 创建一个缓冲区
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                // 将这个缓冲区的内容发送的所有的客户机
                socketChannel.write(wrap);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        GroupChatServer groupChatServer = new GroupChatServer();
        // 启动服务器进行监听
        groupChatServer.listen();
    }
}

