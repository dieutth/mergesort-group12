package inoutstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MMWriteStream {
//	private FileChannel fileChannel;
	public FileChannel fileChannel;
	private MappedByteBuffer buffer;
	public MMWriteStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "rw").getChannel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBuffer(long position, long size){
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, position, size);
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
	public void write(int value){
		
		buffer.putInt(value);
	}
	public void clear(){
		buffer.clear();
	}
	public void flush(){
		try {
			fileChannel.force(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
