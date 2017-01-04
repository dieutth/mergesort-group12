package systemcall;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RunStream implements Runnable{
	private int method_index;
	private String in_fileLocation;
	private String out_fileLocation;
	private int buffer_size;
	
	public RunStream(int method_index, String in_fileLocation, String out_fileLocation){
		this.method_index = method_index;
		this.in_fileLocation = in_fileLocation;
		this.out_fileLocation = out_fileLocation;
	}
	public RunStream(int method_index, String in_fileLocation, String out_fileLocation, int buffer_size){
		this(method_index, in_fileLocation, out_fileLocation);
		this.buffer_size = buffer_size;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		File inFile = new File(in_fileLocation);
		File outFile = new File(out_fileLocation);
		if (method_index==1)
			try {
				new ReadStream().readInputStream(inFile, outFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if (method_index==2) {
			try {
				new ReadStream().readInputStream_withBuffer(inFile, outFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (method_index==3) {
			try {
				new ReadStream().readWithBuffer(inFile, outFile, buffer_size);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				new ReadStream().useMemoryMapping(inFile, outFile, buffer_size);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	
}

public class ReadStream {
	private final static String in_rootLocation = "F://Data//intergers//small_interger_";
	private final static String out_rootLocation = "F://Data//intergers//out_";
	private final static int buffer_size = 20;
	
	public void readInputStream(File f, File out) throws IOException{
		InputStream is = new FileInputStream(f);
		DataInputStream dStream = new DataInputStream(is);
		FileOutputStream fos = new FileOutputStream(out);
		DataOutputStream dos = new DataOutputStream(fos);
		try{
			while(dStream.available()>0){
				int r = dStream.readInt();
				dos.writeInt(r);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if (dStream != null)
				dStream.close();
			if (dos != null)
				dos.close();
		}
	}
	
	public void readInputStream_withBuffer(File f, File out) throws IOException{
		InputStream is = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(is);
		DataInputStream dStream = new DataInputStream(bis);
		
		OutputStream os = new FileOutputStream(out);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		DataOutputStream dos = new DataOutputStream(bos);
		try{
			while(dStream.available()>0){
				int r = dStream.readInt();
				dos.writeInt(r);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if (dStream != null)
				dStream.close();
			if (dos != null)
				dos.close();
		}
	}
	
	public void readWithBuffer(File f, File out, int buffer_size) throws FileNotFoundException{
		InputStream is = new FileInputStream(f);
		DataInputStream dStream = new DataInputStream(is);
		
		OutputStream os = new FileOutputStream(out);
		DataOutputStream dos = new DataOutputStream(os);
		byte[] buffer = new byte[buffer_size]; 
		try{
			int len = 0;
	        while((len = is.read(buffer)) != -1) {
	            dos.write(buffer, 0, len);
	        }
	        dos.flush();

		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if (dStream != null)
				try {
					dStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	public void useMemoryMapping(File f, File out, int buffer_size) throws FileNotFoundException, IOException{
		MappedByteBuffer mappedByteBuffer = new RandomAccessFile(f, "r")
        .getChannel().map(FileChannel.MapMode.READ_ONLY, 0, buffer_size);
		
	}
	public static void executeMultithread(int k, int method_index) {
		ExecutorService executor = Executors.newFixedThreadPool(k);
		for (int i=0; i<k; i++){
			String in_fileLocation = in_rootLocation + Integer.toString(i);
			String out_fileLocation = out_rootLocation + Integer.toString(i);
			Runnable r;
			if (method_index == 3) {
				r = new RunStream(method_index, in_fileLocation, out_fileLocation, buffer_size);
			}
			else {
				r = new RunStream(method_index, in_fileLocation, out_fileLocation);
			}
			
			executor.execute(r);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		int k = 3;
		int N = 5;
		for (int i=0; i<N; i++){
			System.out.println("----" + i + "----");
			long startTime = System.currentTimeMillis();
			executeMultithread(k, 1);
			System.out.println("time: " + (System.currentTimeMillis() - startTime));
			
			startTime = System.currentTimeMillis();
			executeMultithread(k, 2);
			System.out.println("time: " + (System.currentTimeMillis() - startTime));
			
			startTime = System.currentTimeMillis();
			executeMultithread(k, 3);
			System.out.println("time: " + (System.currentTimeMillis() - startTime));
		}
	}
}
