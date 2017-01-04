package inoutstream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadStream_1 {
	private InputStream is;
	private DataInputStream dis;
	private long position; 
	public void open(File in) throws FileNotFoundException{
		is = new FileInputStream(in);
		dis = new DataInputStream(is);
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
