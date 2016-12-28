package sort;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import generator.IntegerGenerator;

class MMReadStream{
//	private FileChannel fileChannel;
	public FileChannel fileChannel;
	private MappedByteBuffer buffer;
	
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
	public MMWriteStream(String fileLocation, int length){
		try {
			fileChannel = new RandomAccessFile(new File(fileLocation), "rw").getChannel();
			buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
/*InsertionPQ implement PriorityQueue using Insertion Sort for inserting.*/
class InsertionPQ{

	private ArrayList<Integer> keys;
	private ArrayList<Integer> ind;
	private int n;
	
	public InsertionPQ(){
		keys = new ArrayList<Integer>();
		ind = new ArrayList<Integer>();
		n = 0;
	}
	public void insert(int e){
		int index = keys.size();
		set(index, e);
		n++;
	}
	
	public void set(int index, int element){
		if (index == keys.size()){
			keys.add(element);
		}else{
			if (keys.get(index)==null)
				n++;
			keys.set(index, element);
		}
		int i;
		for (i = 0; i < ind.size(); i++){
			int in = ind.get(i);
			if (keys.get(in) != null && keys.get(in) > element){
				ind.add(i, index);
				break;
			}
		}
		if (i == ind.size())
			ind.add(index);
		
	}
	public void removeMin(){
		if (isEmpty())
			return;
		int minIndex = getMinIndex();
		keys.set(minIndex, null);
		n--;
		ind.remove(0);
		
	}
	public int getMinValue(){
		return keys.get(getMinIndex());
	}
	public int getMinIndex(){
		return ind.get(0);
	}
	
	public boolean isEmpty(){
		return n == 0;
	}
}

public class MultiwayMergeSort {
	public void mergeSort(DataInputStream[] diss){
		PriorityQueue pq = new PriorityQueue(diss.length);
		for (DataInputStream dis : diss){
			
		}
			
	}
	
	public static void mergeSort(ArrayList<MMReadStream> inputs, ArrayList<Integer> result){
		ArrayList<ArrayList<Integer>> inp = new ArrayList<ArrayList<Integer>>();
		for (MMReadStream mms : inputs){
			ArrayList<Integer> l = new ArrayList<Integer>();
			while (!mms.isEndOfStream())
				l.add(mms.readNext());
			inp.add(l);
		}
		mergeSort_(inp, result);
	}
	
	
	public static void mergeSort_(ArrayList<ArrayList<Integer>> inputs, ArrayList<Integer> result){
		InsertionPQ pq = new InsertionPQ();
		for (ArrayList<Integer> a : inputs){
			if (!a.isEmpty()){
				pq.insert(a.get(0));
				a.remove(0);
			}
		}
		int minIndex;
		while (!pq.isEmpty()){
			result.add(pq.getMinValue());
			minIndex = pq.getMinIndex();
			pq.removeMin();
			
			if (!inputs.get(minIndex).isEmpty()){
				pq.set(minIndex, inputs.get(minIndex).get(0));
				inputs.get(minIndex).remove(0);
			}
		}
		
	}
	public static void main(String[] args) {
		int d = 3;
		ArrayList<MMReadStream> queue = new ArrayList<MMReadStream>();
		
		int number_of_files = 1000;
		number_of_files = 5;
		//storing references to splitted files into a queue
		for (int i=1; i<=number_of_files; i++){
			String fileLocation = "F://Data//intergers//test//small_interger_" + Integer.toString(i);
			queue.add(new MMReadStream(fileLocation));
		}
		
		
		
		int number_of_file_left = number_of_files;
		int start = 0;
		
		while (number_of_file_left > 1){
			String output = "F://Data//intergers//test//merged_" + Integer.toString(start);
			ArrayList<Integer> result = new ArrayList<Integer>(); 
			mergeSort(new ArrayList(queue.subList(start, d+start)), result);
			
			MMWriteStream mmws = new MMWriteStream(output, result.size()*4);
//			try {
//				System.out.println(mmws.fileChannel.size());
//				System.out.println(queue.get(0).fileChannel.size());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			for (int r : result){
				
				mmws.write(r);
			}
			System.out.println(result.size());
			mmws.clear();
			mmws.close();
			
			queue.add(new MMReadStream(output));
			start += d;
			number_of_file_left -= d-1;
			
		
			try {
				MMReadStream m = queue.get(queue.size()-1);
				int size_1 = 0;
				while (!m.isEndOfStream()){
					m.readNext();
					size_1++;
				}
				System.out.println(size_1);
				
				m.fileChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
