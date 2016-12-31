package generator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import inoutstream.MMWriteStream;

public class Abc {
	private static final int NUMBER_OF_INTERGER = 500000000;
	public static void generateWithMM(String fileLocation){
		MMWriteStream mmws = new MMWriteStream(fileLocation);
		Random random = new Random();
		int oneLoop = 5000000;
//		mmws.setBuffer(0, oneLoop*4);
		int loop = NUMBER_OF_INTERGER/oneLoop;
		ByteBuffer bb = ByteBuffer.allocate(oneLoop*4);
		for (int i = loop; i > 0; i--){
//			System.out.println("" + bb.capacity() + "----" + bb.position());
			
			for (int j = 0; j < oneLoop; j++)
				bb.putInt(random.nextInt());
			
//			System.out.println("" + bb.capacity() + "----" + bb.position());
			
			try {
//				if (i != 1)
//					mmws.setBuffer(mmws.fileChannel.position(), oneLoop*4);
//					System.out.println(mmws.fileChannel.position());
					bb.rewind();
					mmws.fileChannel.write(bb, mmws.fileChannel.size());
					bb.flip();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mmws.close();
	}
	public void generate_32bit_integer(File file) throws FileNotFoundException{
		FileOutputStream fos = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(fos);
		
		Random random = new Random();
		int oneLoop = 1000000;
		byte[] buffer;
		int[] intBuffer = new int[oneLoop];
		int loop = NUMBER_OF_INTERGER/oneLoop;
		
		ByteBuffer bb = ByteBuffer.allocate(oneLoop*4);
		IntBuffer ib = bb.asIntBuffer();

		try{
			for (int i=0; i < loop; i++){
				for (int j = 0; j < oneLoop; j++)
					ib.put(random.nextInt());
				buffer = bb.array();
				dos.write(buffer, 0, buffer.length);
				bb.flip();
				ib.flip();
			}
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	public static void main(String[] args) throws FileNotFoundException {
//		int number_of_files = 500;
		int number_of_files = 1;
		long startTime = System.currentTimeMillis();
		for (int i=0; i<number_of_files; i++){
			String fileLocation = "F://Data//intergers//test//test500M//f500mil_noMM";// + Integer.toString(i);
//				new IntegerGenerator().generate_32bit_integer(new File(fileLocation));
//			new Abc().generateWithMM(fileLocation);
			new Abc().generate_32bit_integer(new File(fileLocation));
		}
		System.out.println(System.currentTimeMillis() - startTime);
	}
	
}
