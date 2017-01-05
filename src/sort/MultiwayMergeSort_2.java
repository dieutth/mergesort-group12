package sort;

import java.io.EOFException;
import java.io.File;
import java.util.Arrays;

import inoutstream.ReadStream_4;
import inoutstream.WriteStream_4;

class Entry implements Comparable<Entry>{
	private int stream;
	private int value;
	
	public Entry(int s, int v){
		stream = s;
		value = v;
	}
	public int getStream() {
		return stream;
	}

	public void setStream(int stream) {
		this.stream = stream;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int compareTo(Entry o) {
		// TODO Auto-generated method stub
		int v = o.getValue();
		return value > v ? 1: (value==v? 0 : -1) ;
	}
	
}
public class MultiwayMergeSort_2 {
	
	public void split(File in, int N, int M, String outFolder){
		ReadStream_4 rs = new ReadStream_4();
		WriteStream_4 ws = new WriteStream_4();
		rs.open(in, M);
		int loop = N/M;
		int[] temp = new int[M];
		for (int i = 0; i < loop; i++){
			for (int j = 0; j < M; j++)
				try {
					temp[j] = rs.read_next();
				} catch (EOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Arrays.sort(temp);
			ws.create(new File(outFolder + i), 0);
			ws.write(temp);
			ws.close();
		}
		int r = N - loop*M;
		if (r > 0){
			temp = new int[r];
			for (int i = 0; i<r; i++)
				try {
					temp[i] = rs.read_next();
				} catch (EOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Arrays.sort(temp);
			ws.create(new File(outFolder + loop), 0);
			ws.write(temp);
			ws.close();
		}
	}
	public void mergeSort(){
		
	}
	public static void main(String[] args) {
		String fileLocation = "F:\\Data\\intergers\\test\\test100M\\f250mil_10mil";
		String outFolder = "F:\\Data\\intergers\\test\\test100M\\";
		File in = new File(fileLocation);
		int N = 250000000;
		int M = 1000000;
		new MultiwayMergeSort_2().split(in, N, M, outFolder);
	}
}
