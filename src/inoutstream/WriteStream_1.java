package inoutstream;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteStream_1 {
	private OutputStream os;
	private DataOutputStream dos; 
	
	public void create(File out) throws IOException{
		if (out.exists())
			out.delete();
		out.createNewFile();
		os = new FileOutputStream(out);
		dos = new DataOutputStream(os);
	}
	
	public void write(int b){
		try {
			dos.writeInt(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
