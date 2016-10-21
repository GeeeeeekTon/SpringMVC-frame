import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将一个文件的所有内容拷贝到另一个文件中。
 * 
 * CopyFile.java 执行三个基本操作： 首先创建一个 Buffer，然后从源文件中将数据读到这个缓冲区中，然后将缓冲区写入目标文件。 程序不断重复 — 读、写、读、写 — 直到源文件结束。
 * */
public class TestNio {

    public static void main(String[] args) throws Exception {
        String infile = "E:/GitHub.txt";
        String outfile = "E:/GitHub2.txt";

        // 获取源文件和目标文件的输入输出流
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);

        // 获取输入输出通道
        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            // clear方法重设缓冲区，使它可以接受读入的数据
            buffer.clear();

            // 从输入通道中将数据读到缓冲区
            int r = fcin.read(buffer);

            // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
            if (r == -1) {
                break;
            }

            // flip方法让缓冲区可以将新读入的数据写入另一个通道
            buffer.flip();

            // 从输出通道中将数据写入缓冲区
            fcout.write(buffer);
        }
    }
}