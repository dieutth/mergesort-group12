package test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sort.MultiwayMergeSort;

public class TestCorrectness {
	public static void readInputStream_withBuffer(File f) throws IOException{
		InputStream is = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(is);
		DataInputStream dStream = new DataInputStream(bis);
		
		try{
			while(dStream.available()>0){
				int r = dStream.readInt();
				System.out.println(r);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if (dStream != null)
				dStream.close();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFileLocation = "F://Data//intergers//test_readable//small_interger_0";
		int N = 1000;
		int M = 100;
		String outFileLocation = "F://Data//intergers//test_readable//splitted_";
		MultiwayMergeSort.split(inputFileLocation, N, M, outFileLocation);
		for (int i = 0; i < 10; i++)
			try {
				System.out.println("Reading file " + i);
				readInputStream_withBuffer(new File(outFileLocation + Integer.toString(i))); 
						
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
