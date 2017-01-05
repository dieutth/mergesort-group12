package inoutstream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class WriteStream_4 {
	private FileChannel fileChannel;
	private MappedByteBuffer buffer;
	
	public void create(File out, int bufferSize){
		try {
			if (out.exists())
				out.delete();
			out.createNewFile();
			fileChannel = new RandomAccessFile(out, "rw").getChannel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void write(int[] bs){
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileChannel.size(), bs.length*4);
			for (int b : bs)
				buffer.putInt(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		WriteStream_1 ws1 = new WriteStream_1();
		WriteStream_2 ws2 = new WriteStream_2();
		WriteStream_3 ws3 = new WriteStream_3();
		WriteStream_4 ws4 = new WriteStream_4();
		File f1 = new File("F:\\Data\\intergers\\test\\teststream\\1000\\1");
		File f2 = new File("F:\\Data\\intergers\\test\\teststream\\1000\\2");
		File f3 = new File("F:\\Data\\intergers\\test\\teststream\\1000\\3");
		File f4 = new File("F:\\Data\\intergers\\test\\teststream\\1000\\4");
		int N = 2000000;
		ws1.create(f1);
		ws2.create(f2);
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < N; i++)
			ws1.write(i);
		ws1.close();
		System.out.println("time: " + (System.currentTimeMillis() - startTime));
		
		startTime = System.currentTimeMillis();
		for (int i = 0; i < N; i++)
			ws2.write(i);
		ws2.close();
		System.out.println("time: " + (System.currentTimeMillis() - startTime));
		
		
		ws3.create(f3, 2048);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < N; i++)
			ws3.write(i);
		System.out.println("time: " + (System.currentTimeMillis() - startTime));
		ws3.close();
		
		ws4.create(f4, 0);
		
		int[] b = new int[N];
		for (int i = 0; i < N; i++)
			b[i] = i;
		startTime = System.currentTimeMillis();
		ws4.write(b);
		System.out.println("time: " + (System.currentTimeMillis() - startTime));
		ws4.close();
	}
}
