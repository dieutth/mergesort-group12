package inoutstream;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ReadStream_4 {
	private FileChannel fileChannel;
	private MappedByteBuffer buffer;
	private long position;
	private int bufferSize;
	private long size;
	
	private void loadBuffer(){
		try {
			long s = Math.min(bufferSize, size - position); 
			if (s == 0)
				return;
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, s);
			position += s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void open(File in, int bufferSize){
		try {
			fileChannel = new RandomAccessFile(in, "r").getChannel();
			size = fileChannel.size();
			position = 0;
			this.bufferSize = bufferSize;
			loadBuffer();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isEndOfStream(){
		if (buffer.hasRemaining())
			return false;
		return position == size;
	}
	
	public int read_next() throws EOFException{
		if (!buffer.hasRemaining())
			loadBuffer();
		
		if (buffer.hasRemaining())
			return buffer.getInt();
		else
			throw new EOFException();
	}
	
	public void close(){
		try {
			fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
