package Nio.QuickStar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class test2 {
    public static void main(String[] args) throws Exception {
        // 获取输入流通道
        File file = new File("1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        // 获取输出流通道
        FileOutputStream fileOutputStream = new FileOutputStream(new File("2.txt"));
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        // 得到缓冲数组
        ByteBuffer allocate1 = ByteBuffer.allocate(5);
        ByteBuffer allocate2 = ByteBuffer.allocate(5);
        ByteBuffer allocate3 = ByteBuffer.allocate(5);

        ByteBuffer[] buffers = new ByteBuffer[]{allocate1, allocate2, allocate3};
        long read;
        long sumRead = 0;

        while ((read = fileInputStreamChannel.read(buffers)) != -1) {
            sumRead += read;
            // 分散读
            Arrays.stream(buffers)
                    .map(buffer -> "position = " + buffer.position() + ", limit = " + buffer.limit())
                    .forEach(System.out::println);
            // 切换读模式
            Arrays.stream(buffers).forEach(Buffer::flip);

            // 聚合写入
            fileOutputStreamChannel.write(buffers);
            Arrays.stream(buffers).forEach(Buffer::clear);
        }

        System.out.println("总长度：" + sumRead);

        // 关闭通道
        fileInputStream.close();
        fileInputStreamChannel.close();
        fileOutputStream.close();
        fileOutputStreamChannel.close();
    }
}














