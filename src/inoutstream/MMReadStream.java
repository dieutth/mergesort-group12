package inoutstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MMReadStream {
//	private FileChannel fileChannel;
	public FileChannel fileChannel;
	private MappedByteBuffer buffer;
	
	public MappedByteBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(long position, long size){
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, size);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setBuffer(){
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEndOfStream(){
		return !buffer.hasRemaining();
	}
	public int readNext(){
		return buffer.getInt();
		
	}
	public MMReadStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "r").getChannel();
//			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
		} catch (FileNotFoundException e) {
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
	public static void main(String[] args){
//		MMReadStream mmrs = new MMReadStream("F://Data//intergers//test_readable//small_interger_0");
		MMReadStream mmrs = new MMReadStream("F://Data//intergers//test//test250M//f250mil_5mil");
		MMWriteStream mmws = new MMWriteStream("F://Data//intergers//test//test250M//out_f250mil");
		int N = 250000000;
		int M = 1000;
		int loop = N/M;
//		mmrs.setBuffer(0, M*4);
		long startTime = System.currentTimeMillis();
		try{
			while (loop > 0 ){
				mmrs.setBuffer(mmrs.fileChannel.position(), M*4);
				mmws.fileChannel.write(mmrs.buffer);
				loop --;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
//		for (int i = 0; i < loop; i++){
//			try {
//				mmrs.setBuffer(mmrs.fileChannel.position(), M*4);
//				mmws.fileChannel.write(mmrs.buffer);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if (i == loop-1)
//				break;
//		}
		
		
		System.out.println(System.currentTimeMillis() - startTime);
		try {
			mmrs.fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
