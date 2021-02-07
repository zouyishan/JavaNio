package Nio.QuickStar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class test1 {
    public static void main(String[] args) throws Exception {
        // 获取输入通道
        File file = new File("1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannelchannel = fileInputStream.getChannel();

        // 获取输出通道
        FileOutputStream fileOutputStream = new FileOutputStream(new File("2.txt"));
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        // 获取缓冲
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        // 将输入流通道的数据读取到输出流的通道
//        fileInputStreamChannelchannel.transferTo(0, allocate.limit(), fileOutputStreamChannel);
        fileOutputStreamChannel.transferFrom(fileInputStreamChannelchannel, 0, allocate.limit());
        // 关闭流

        fileInputStreamChannelchannel.close();
        fileOutputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
