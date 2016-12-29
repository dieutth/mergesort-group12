package sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import inoutstream.MMReadStream;
import inoutstream.MMWriteStream;

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

public class MultiwayMergeSort {
	private static PriorityQueue<Entry> pq;
	public MultiwayMergeSort(int d){
		pq = new PriorityQueue<Entry>(d);
	}
	
	public static void split(String inFileLocation, int N, int M, String outFileLocation){
		MMReadStream mmrs = new MMReadStream(inFileLocation);
		int loopSize = N/M;
		int[] stream = new int[M]; 
		for (int i = 0; i < loopSize; i++){
			MMWriteStream mmws = new MMWriteStream(outFileLocation + Integer.toString(i));
			mmws.setBuffer(0, M*4);
			for (int j = 0; j < M; j++)
				stream[j] = mmrs.readNext();
			Arrays.sort(stream);
			for (int value : stream)
				mmws.write(value);
		}
	}
	public static void mergeSort(int B, ArrayList<MMReadStream> inputs, MMWriteStream mmws){
		for (int i=0; i < inputs.size(); i++){
			Entry e = new Entry(i, inputs.get(i).readNext());
			pq.add(e);
			mmws.setBuffer(0, B*4);
			
		}
		int count = B;
		while (!pq.isEmpty()){
			if (count == 0){
//				mmws.flush();
				try {
					mmws.setBuffer(mmws.fileChannel.position(), B*4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count = B;
			}
			mmws.write(pq.peek().getValue());
			int ind = pq.poll().getStream();
			if (!inputs.get(ind).isEndOfStream())
				pq.add(new Entry(ind, inputs.get(ind).readNext()));
			count--;
		}
		mmws.close();
		
	}
	public static void mergeSort(ArrayList<MMReadStream> inputs, MMWriteStream mmws, int availableMemoryForIntermediateResult){
		//size for result of all merging 
//		int outBufferSize = 0;
		int outBufferSize = availableMemoryForIntermediateResult;
		for (int i=0; i < inputs.size(); i++){
			Entry e = new Entry(i, inputs.get(i).readNext());
			pq.add(e);
		mmws.setBuffer(0, outBufferSize*4);
			
		}
		int count = availableMemoryForIntermediateResult;
		while (!pq.isEmpty()){
			if (count == 0){
//				mmws.flush();
				try {
					mmws.setBuffer(mmws.fileChannel.position(), outBufferSize*4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count = availableMemoryForIntermediateResult;
			}
			mmws.write(pq.peek().getValue());
			int ind = pq.poll().getStream();
			if (!inputs.get(ind).isEndOfStream())
				pq.add(new Entry(ind, inputs.get(ind).readNext()));
			count--;
		}
		mmws.close();
		
	}
	
	
	public static void main(String[] args) {
		int d = 50;
		int M = 10000;
		int B = 10;
		
		
		new MultiwayMergeSort(d);
		int number_of_files = 1000;
		ArrayList<MMReadStream> queue = new ArrayList<MMReadStream>();
		long startTime = System.currentTimeMillis();
		//storing references to splitted files into a queue
		for (int i=0; i<number_of_files; i++){
			String fileLocation = "F://Data//intergers//test_//small_interger_" + Integer.toString(i);
			queue.add(new MMReadStream(fileLocation));
		}
		int number_of_file_left = number_of_files;
		int start = 0;
		
		while (number_of_file_left > 1){
			String output = "F://Data//intergers//test_//merged_" + Integer.toString(start);
			MMWriteStream mmws = new MMWriteStream(output); 
			int availableMemoryForIntermediateResult = M - d;
			mergeSort(new ArrayList(queue.subList(start, d+start < queue.size()? d+start:queue.size())), mmws, availableMemoryForIntermediateResult);
			
			queue.add(new MMReadStream(output));
			start += d;
			number_of_file_left -= d-1;

		}
		System.out.println("time: " + (System.currentTimeMillis() - startTime));
	}

}
