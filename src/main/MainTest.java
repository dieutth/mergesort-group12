package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

class MMReadStream{
	private FileChannel fileChannel;
	private MappedByteBuffer buffer;
	
	public MMReadStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "r").getChannel();
			buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEndOfStream(){
		return buffer.hasRemaining();
	}
	public void readNext(){
		buffer.getInt();
	}
	
}

class MMWriteStream{
	
}
public class MainTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int number_of_files = 1;
		int NUMBER_OF_INTERGER = 10000000;
		
		try {
			for (int i=0; i<number_of_files; i++){
				String in_fileLocation = "F://Data//intergers//small_interger_" + Integer.toString(i);
				String out_fileLocation = "F://Data//intergers//sorted_small_interger_" + Integer.toString(i);
				
				InputStream is = new FileInputStream(new File(in_fileLocation));
				BufferedInputStream bis = new BufferedInputStream(is);
				DataInputStream dStream = new DataInputStream(bis);
				
				OutputStream os = new FileOutputStream(new File(out_fileLocation));
				BufferedOutputStream bos = new BufferedOutputStream(os);
				DataOutputStream dos = new DataOutputStream(bos);
				
				int[] temp = new int[NUMBER_OF_INTERGER];
				long startTime = System.currentTimeMillis();
				try{
					int ind = 0;
					while(dStream.available()>0){
						int r = dStream.readInt();
						temp[ind] = r;
						ind++;
						
					}
//					System.out.println(ind);
					
					Arrays.sort(temp);
					
					for (int itg : temp){
						dos.writeInt(itg);
						ind--;
					}
					System.out.println("time: " + (System.currentTimeMillis() - startTime));
//					System.out.println(ind);
					
				}catch (IOException e){
					e.printStackTrace();
				}finally{
					if (dStream != null)
						dStream.close();
					if (dos != null)
						dos.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
