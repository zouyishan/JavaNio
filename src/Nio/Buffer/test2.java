package Nio.Buffer;

import java.nio.ByteBuffer;

public class test2 {
    public static void main(String[] args) {
        String msg = "一顿操作猛如虎，一看战绩0杠5";
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        byte[] bytes = msg.getBytes();
        allocate.put(bytes); // 写入数据到Buffer中
        allocate.flip(); // 切换成读的模式
        byte[] tempByte = new byte[bytes.length]; // 用于存放数据的数组
        int i = 0;
        while (allocate.hasRemaining()) {
            byte b = allocate.get();
            tempByte[i] = b;
            i++;
        }
        System.out.println(new String(tempByte));
    }
}
