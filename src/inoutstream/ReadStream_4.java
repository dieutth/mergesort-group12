package inoutstream;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/*
 * Note: This class supports reading 32 bit integer from files.
 * Each 32-bit integer is used by the term "element".
 */
public class ReadStream_4 {
	/*
	 * @attribute fileChannel connect to a particular file
	 * @attribute buffer refers to the  mapped region corresponding with a portion of the file
	 * @attribute position refers to the first position of the file that has not been mapped into memory
	 * @attribute bufferSize size of the buffer (ie. file portion, or mapped region, in bytes) every time a mapping is created
	 * @attribute  size refers to the size of the underlying file 
	 */
	private FileChannel fileChannel;
	private MappedByteBuffer buffer;
	private long position;
	private int bufferSize;
	private long size;
	
	/*
	 * @output: This method loads bufferSize byte (or bufferSize/4 elements) from the file to the mapped region.
	 * If position is equal to size, there is nothing to map, and the method returns immediately.
	 * Otherwise, it creates a new mapped region, start from current position for size bufferSize or size-position, whichever smaller.  
	 */
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
	
	/*
	 * @output: This create a channel for the input file. It then creates the mapping (initial loadBuffer) between file and memory
	 * @param in: file to get the channel
	 * @bufferSize: size of the region to be mapped each time, ie. size of MappedByteBuffer
	 */
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
	
	/*
	 * @output: This method, despite its name, is actually about checking if there is still element to be read from the file. 
	 * It, therefore, check the buffer first. If there is element in buffer, then return false.
	 * If the buffer has no remaining, then it check if the position is equal to size. Position is equal to size indicates
	 * that the last portion of the file has been loaded to buffer, and since buffer is full, it is the end of the file.
	 * Otherwise, there is still data in the file, and it has not reached the end yet. 
	 */
	public boolean isEndOfStream(){
		if (buffer.hasRemaining())
			return false;
		return position == size;
	}
	
	/*
	 * @output: This method return the next element from the buffer.
	 * If the buffer has no remaining, then call loadBuffer() to fill it
	 * If the buffer has remaining (either from before, or after being loaded successfully)
	 * return the next element.
	 * If the buffer still has no remaining after being loaded (because it reaches the end of the file)
	 * throw the EOFException
	 */
	public int read_next() throws EOFException{
		if (!buffer.hasRemaining())
			loadBuffer();
		
		if (buffer.hasRemaining())
			return buffer.getInt();
		else
			throw new EOFException();
	}
	
	/*
	 * @output: This method close the channel that connect the file and the mapped region.
	 * Note that it doesn't ensure the unmapping between the file portion and the mapped region.
	 * From Java docs: A mapping, once established, is not dependent upon the file channel that was used to create it. 
	 * Closing the channel, in particular, has no effect upon the validity of the mapping.
	 */
	public void close(){
		try {
			fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
