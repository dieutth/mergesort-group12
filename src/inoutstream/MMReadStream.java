package inoutstream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
	public boolean isEndOfStream(){
		return !buffer.hasRemaining();
	}
	public int readNext(){
		return buffer.getInt();
		
	}
	public MMReadStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "r").getChannel();
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
