package sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.PriorityQueue;

class MMReadStream{
//	private FileChannel fileChannel;
	public FileChannel fileChannel;
	private MappedByteBuffer buffer;
	
	public MappedByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(MappedByteBuffer buffer) {
		this.buffer = buffer;
	}

	public MMReadStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "r").getChannel();
			buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEndOfStream(){
		return !buffer.hasRemaining();
	}
	public int readNext(){
		return buffer.getInt();
		
	}
	
}
class MMWriteStream{
//	private FileChannel fileChannel;
	public FileChannel fileChannel;
	private MappedByteBuffer buffer;
	public MMWriteStream(String fileLocation){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "rw").getChannel();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBuffer(int length){
		try {
			buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public void close(){
		try {
			fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write(int value){
		
		buffer.putInt(value);
	}
	public void clear(){
		buffer.clear();
	}
}

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
	public static void mergeSort(ArrayList<MMReadStream> inputs, MMWriteStream mmws, int availableMemoryForIntermediateResult){
		//size for result of all merging 
		int outBufferSize = 0;
		for (int i=0; i < inputs.size(); i++){
			Entry e = new Entry(i, inputs.get(i).readNext());
			pq.add(e);
			try {
				outBufferSize += inputs.get(i).fileChannel.size();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		mmws.setBuffer(outBufferSize);
			
		}
		int count = availableMemoryForIntermediateResult;
		while (!pq.isEmpty()){
			if (count != 0)
				mmws.write(pq.peek().getValue());
			else{
				mmws.clear();
				mmws.write(pq.peek().getValue());
			}
			int ind = pq.poll().getStream();
			if (!inputs.get(ind).isEndOfStream())
				pq.add(new Entry(ind, inputs.get(ind).readNext()));
			count--;
		}
		mmws.close();
		
	}
	
	
	public static void main(String[] args) {
		int d = 50;
		int M = 1000;

		
		
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
