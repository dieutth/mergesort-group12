package inoutstream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadStream_2 {
	private InputStream is;
	private DataInputStream dis;
	private BufferedInputStream bis;
	private long position; 
	
	public void open(File in) throws FileNotFoundException{
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
	public static void main(String[] args) throws FileNotFoundException {
//		ReadStream_1 rs = new ReadStream_1();
		ReadStream_2 rs2 = new ReadStream_2();
		ReadStream_3 rs3 = new ReadStream_3();
		ReadStream_4 rs4 = new ReadStream_4();
		//File in = new File("F:\\Data\\intergers\\big_file_1");
//		File in = new File("F:\\Data\\intergers\\test\\f25mil0");
//		File in = new File("F:\\Data\\intergers\\test\\f250mil_10mil");
		File in = new File("C:\\Users\\Alhakeem\\dieu_workspace\\data\\1k\\merged_8");
//		File in = new File("C:\\Users\\Alhakeem\\dieu_workspace\\data\\100mil\\merged_100");
//		File in = new File("C:\\Users\\Alhakeem\\dieu_workspace\\data\\100mil\\merged_100");
//		rs.open(in);
		rs4.open(in, 12000*4);
		rs2.open(in);
		int count = 0;
			
		long startTime = System.nanoTime();
		/*while(!rs2.isEndOfStream())
			try {
				rs2.read_next();
//				count++;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println(count);
		long r2 = System.nanoTime() - startTime;
		System.out.println("time 2: " + r2);
		rs2.close();*/
		
		
//		count = 0;
//		int B = 2000;
//
//		while (B > 0){
//			count = 0;
//			rs3.open(in, B);
//			startTime = System.nanoTime();
//			while(!rs3.isEndOfStream())
//				try {
//					rs3.read_next();
//					count++;	
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			System.out.println(count);
//			long r3 = System.nanoTime() - startTime;
//			System.out.println("time 3: " + r3 + "------ B = " + B);
//			rs3.close();
//			B *= 2;
//		}
		
		count = 0;
		startTime = System.nanoTime();
		while(!rs4.isEndOfStream())
			try {
				int r = rs4.read_next();
//				if (count > 99999000)
					System.out.println(r);
				count++;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println(count);
		long r4 = System.nanoTime() - startTime;
		System.out.println("time 4: " + r4);
		
	}
}
