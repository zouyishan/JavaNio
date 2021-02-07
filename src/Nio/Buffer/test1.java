package Nio.Buffer;

import java.nio.ByteBuffer;

public class test1 {
    public static void main(String[] args) {
        // 创建堆内内存块
        ByteBuffer allocate = ByteBuffer.allocate(1024);

        String msg = "哈哈哈";
        // 包装一个byte[]数组获得一个Buffer，实际类型是HeapByteBuffer
        ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());

        // 创建堆外内存块DirectByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
    }
}
