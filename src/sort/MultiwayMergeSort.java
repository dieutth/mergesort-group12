package sort;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

import inoutstream.MMReadStream;
import inoutstream.MMWriteStream;
import main.Agent;

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
		try {
			mmrs.setBuffer(mmrs.fileChannel.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int loopSize = N/M;
		int[] stream = new int[M]; 
		int i = 0;
		for (; i < loopSize; i++){
			MMWriteStream mmws = new MMWriteStream(outFileLocation + Integer.toString(i));
			mmws.setBuffer(0, M*4);
			for (int j = 0; j < M; j++)
				stream[j] = mmrs.readNext();
			Arrays.sort(stream);
			for (int value : stream)
				mmws.write(value);
		}
		if (!mmrs.isEndOfStream()){
			MMWriteStream mmws = new MMWriteStream(outFileLocation + Integer.toString(i));
			ArrayList<Integer> ls = new ArrayList<Integer>();
			while (!mmrs.isEndOfStream()){
				ls.add(mmrs.readNext());
			}
			Collections.sort(ls);
			for (Integer value : ls){
				mmws.write(value);
			}
		}
	}
	public static void mergeSort(int B, ArrayList<MMReadStream> inputs, MMWriteStream mmws){
		long totalSize = 0;
		int bufferSize = B * 4;
		Entry[] entries = new Entry[inputs.size()];
		for (int i=0; i < inputs.size(); i++){
			inputs.get(i).setBuffer(0, bufferSize);
			
			Entry e = new Entry(i, inputs.get(i).readNext());
			pq.add(e);
			entries[i] = e;
			try {
				totalSize += inputs.get(i).fileChannel.size();
//				System.out.println(inputs.get(i).fileChannel.position());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.err.println("Error file: " + i);
				e1.printStackTrace();
			}
		}
		mmws.setBuffer(0, bufferSize);
		totalSize -= bufferSize;
	
		while (!pq.isEmpty()){
			mmws.write(pq.peek().getValue());
			int ind = pq.poll().getStream();
			if (!inputs.get(ind).buffer.hasRemaining()){
				try {
					long s = Math.min(bufferSize, inputs.get(ind).fileChannel.size()-inputs.get(ind).getPosition());
					inputs.get(ind).setBuffer(s);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (inputs.get(ind).buffer.hasRemaining()){
				entries[ind].setValue(inputs.get(ind).readNext());
				pq.add(entries[ind]);
			}
			
			if (!mmws.buffer.hasRemaining()){
				try {
					if (totalSize > bufferSize){
						mmws.setBuffer(mmws.fileChannel.position(), bufferSize);
						
					}else{
						mmws.setBuffer(mmws.fileChannel.position(), totalSize);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				totalSize -= bufferSize;
			}
		}
		mmws.close();
	}
	public static void mergeSort(ArrayList<MMReadStream> inputs, MMWriteStream mmws, int availableMemoryForIntermediateResult){
		//size for result of all merging 
		long totalSize = 0;
		
		int outBufferSize = availableMemoryForIntermediateResult * 4;
		Entry[] entries = new Entry[inputs.size()];
		
		for (int i=0; i < inputs.size(); i++){
//			inputs.get(i).setBuffer();
			Entry e = new Entry(i, inputs.get(i).readNext());
			pq.add(e);
			entries[i] = e;
			try {
				totalSize += inputs.get(i).fileChannel.size();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.err.println("Error file: " + i);
				e1.printStackTrace();
			}
		}
		int limit = outBufferSize*2;
		mmws.setBuffer(0, outBufferSize);
		
		
		int count = availableMemoryForIntermediateResult;
		while (!pq.isEmpty()){
			if (count == 0){
				try {
					if (totalSize > limit){
						mmws.setBuffer(mmws.fileChannel.position(), outBufferSize);
					}else{
						mmws.setBuffer(mmws.fileChannel.position(), totalSize-outBufferSize);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count = availableMemoryForIntermediateResult;
				totalSize -= outBufferSize;
				
			}
			mmws.write(pq.peek().getValue());
			int ind = pq.poll().getStream();
			if (!inputs.get(ind).isEndOfStream()){
				entries[ind].setValue(inputs.get(ind).readNext());
				pq.add(entries[ind]);
				
			}
			count--;
		}
		mmws.close();
		
	}
	
	public static void main(String[] args) {
		int loop = 1000;
			while (loop > 0){
			int M = 100;
			//currently no use
			int B = 25;
			int d = M/B - 1;
			
			new MultiwayMergeSort(d);
			int number_of_files = 10;
			ArrayList<MMReadStream> queue = new ArrayList<MMReadStream>();
			ArrayList<String> outfiles = new ArrayList<String>();
			//storing references to splitted files into a queue
			for (int i=0; i<number_of_files; i++){
	//			String fileLocation = "F://Data//intergers//test_//small_interger_" + Integer.toString(i);
				String fileLocation = "F://Data//intergers//test_readable//splitted_" + Integer.toString(i);
	//			String fileLocation = "F://Data//intergers//test//test250M//splitted_" + Integer.toString(i);
				queue.add(new MMReadStream(fileLocation));
			}
			int number_of_file_left = number_of_files;
			int start = 0;
			long startTime = System.currentTimeMillis();
			while (number_of_file_left > 1){
				String output = "F://Data//intergers//test_readable//merged_" + Integer.toString(start) + Integer.toString(loop);
	//			String output = "F://Data//intergers//test//test250M//merged_" + Integer.toString(start);
				outfiles.add(output);
				MMWriteStream mmws = new MMWriteStream(output); 
				mergeSort(B, new ArrayList(queue.subList(start, d+start < queue.size()? d+start:queue.size())), mmws);
				start += d;
				number_of_file_left -= d-1;
				if (number_of_file_left > 1)
					queue.add(new MMReadStream(output));
	
			}
			System.out.println("time: " + (System.currentTimeMillis() - startTime));
			
			loop --;
		}
	}

}
