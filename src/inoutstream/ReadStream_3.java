package inoutstream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadStream_3 {
	private InputStream is;
	private DataInputStream dis;
	private BufferedInputStream bis;
	private long position; 
	
	public void open(File in, int bufferSize) throws FileNotFoundException{
		is = new FileInputStream(in);
		bis = new BufferedInputStream(is);
		dis = new DataInputStream(bis);
		position = in.length();
	}
	public boolean isEndOfStream(){
		return position == 0;
	}
	public int read_next() throws IOException{
		int r = dis.readInt();
		position -= 4;
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
