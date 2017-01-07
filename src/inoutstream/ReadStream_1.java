package inoutstream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadStream_1 {
	/*
	 * @attribute is: the input stream to pipe the file with
	 * @attribute dis: the DataInputStream to pipe is (attribute name) with
	 * @attribute remaining: the remaining of the file that hasn't been read yet. 
	 */
	private InputStream is;
	private DataInputStream dis;
	private long remaining; 
	
	/*
	 * @output: This method open file in to read
	 */
	public void open(File in) throws FileNotFoundException{
		is = new FileInputStream(in);
		dis = new DataInputStream(is);
		remaining = in.length();
	}
	
	/*
	 * @output: This method check if it is the end of stream. Return true if remaining is 0. Return false otherwise.
	 */
	public boolean isEndOfStream(){
		return remaining == 0;
	}
	
	/*
	 * 
	 */
	public int read_next() throws IOException{
		int r = dis.readInt();
		remaining -= 4;
		return r;
	}
	public void close(){
		try {
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
