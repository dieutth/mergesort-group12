package generator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class IntegerGenerator {
	private final int NUMBER_OF_INTERGER = 100;
	public void generate_32bit_integer(File file) throws FileNotFoundException{
		FileOutputStream fos = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(fos);
		Random random = new Random();
		
		try{
			for (int i=0; i < NUMBER_OF_INTERGER; i++){
				int r = random.nextInt();
				dos.writeInt(r);
				
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
	public static void main(String[] args) {
		int number_of_files = 5000;
		try {
			for (int i=0; i<number_of_files; i++){
				String fileLocation = "F://Data//intergers//small_interger_" + Integer.toString(i);
				new IntegerGenerator().generate_32bit_integer(new File(fileLocation));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
