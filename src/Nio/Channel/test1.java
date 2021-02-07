package Nio.Channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class test1 {
    public static void main(String[] args) throws Exception {
        // 获取文件输入流
        File file = new File("1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 从文件输入流获取通道
        FileChannel inputChannel = fileInputStream.getChannel();

        // 获取文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("2.txt"));
        // 从文件输出流获取通道
        FileChannel outputChannel = fileOutputStream.getChannel();

        // 创建一个byteBuffer，小文件所以就直接一次读取，不分多次循环了
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        // 读到缓冲区
        inputChannel.read(allocate);
        // 切换成读模式
        allocate.flip();
        // 把数据从缓冲区写入到输出流通道
        outputChannel.write(allocate);

        // 关闭通道和流
        inputChannel.close();
        outputChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }
}
