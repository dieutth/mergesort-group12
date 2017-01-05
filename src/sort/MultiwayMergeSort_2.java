package sort;

import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import inoutstream.MMReadStream;
import inoutstream.MMWriteStream;
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
		return value > v ? 1: (value == v? 0 : -1) ;
	}
	
}
public class MultiwayMergeSort_2 {
	
	public List<String> split(File in, int N, int M, String outFolder){
		ReadStream_4 rs = new ReadStream_4();
		WriteStream_4 ws = new WriteStream_4();
		List<String> splittedFiles = new ArrayList<String>();
		
		/*
		 * open and load M elements from input file. M is the number of 32-bit integer
		 * so the size to be allocated is multiplied by 4
		 */
		rs.open(in, M*4);
		int loop = N/M; //number of time to loop
		
		int[] temp = new int[M]; //store M numbers read from input file. 
		for (int i = 0; i < loop; i++){
			for (int j = 0; j < M; j++)
				try {
					temp[j] = rs.read_next();
				} catch (EOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Arrays.sort(temp); //sort temp before writing to splitted file
			
			ws.create(new File(outFolder + i), 0); //create splitted file
			ws.write(temp); //write temp to file.
			ws.close(); 
			splittedFiles.add(outFolder+i);
		}
		//if there is remainder in division N/M
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
			splittedFiles.add(outFolder + loop);
		}
		return splittedFiles;
	}
	public void mergeSort(List<ReadStream_4> d_queue, int blockSize, File out) throws EOFException{
		WriteStream_4 ws = new WriteStream_4();
		
		int d = d_queue.size(); 
		Entry[] entries = new Entry[d];
		PriorityQueue<Entry> pq = new PriorityQueue<Entry>(d);
		ws.create(out, 0);
		for (int i = 0; i < d; i++){
			entries[i] = new Entry(i, d_queue.get(i).read_next());
			pq.add(entries[i]);
		}
		
		int[] outputBlock = new int[blockSize];
		int filled = 0;
		int ind;
		
		while (!pq.isEmpty()){
			if (filled == blockSize){ //when outputBlock is fully filled then write it to output file
				ws.write(outputBlock);
				filled = 0;
			}
			outputBlock[filled] = pq.peek().getValue();
			filled++;
			ind = pq.poll().getStream();
			if (!d_queue.get(ind).isEndOfStream()){
				entries[ind].setValue(d_queue.get(ind).read_next());
;				pq.add(entries[ind]);
			}
		}
		//the last write
		if (filled == blockSize){
			ws.write(outputBlock);
		}
		else{
			ws.write(Arrays.copyOfRange(outputBlock, 0, filled+1));
		}
		ws.close();
	}
	
	public void runPasses(List<String> queue,
							int M, int d, String mergedFolder) throws EOFException{
		
		int numberOfFileLeft = queue.size();
		
		File mergedFile;
		String mergedFileLocation; 
		int start = 0;
		int blockSize = M/(d+1);
		while (numberOfFileLeft > 1){
			mergedFileLocation = mergedFolder + start;
			mergedFile = new File(mergedFileLocation);
			
			//take only d files for each merge-sort. If there is only x<d files left, merge them all
			int endIndex = Math.min(queue.size(), start+d);
			List<ReadStream_4> d_queue = new ArrayList<ReadStream_4>();
			
			for (int i = start; i < endIndex; i++){
				ReadStream_4 rs = new ReadStream_4();
				rs.open(new File(queue.get(i)), blockSize);
				d_queue.add(rs);
			}

			//sort d files in d_queue, output stream is written to mergedFile
			mergeSort(d_queue, blockSize, mergedFile);
			
			//put the location of the newly created mergedFile to the end of the queue of files
			queue.add(mergedFileLocation);
			start += d;
			numberOfFileLeft -= d-1;
		}
	}
	
	
	public static void main(String[] args) throws EOFException {
//		String fileLocation = "F:\\Data\\intergers\\test\\test100M\\f250mil_10mil";
//		String outFolder = "F:\\Data\\intergers\\test\\test100M\\";
		
//		String fileLocation = "F:\\Data\\intergers\\test\\test100M\\f250mil_10mil";
//		String outFolder = "F:\\Data\\intergers\\test\\test100M\\splitted_";
//		String mergedFolder = "F:\\Data\\intergers\\test\\test100M\\merged_";

		String fileLocation = "F:\\Data\\intergers\\test\\test1k\\big_file";
		String outFolder = "F:\\Data\\intergers\\test\\test1k\\splitted_";
		String mergedFolder = "F:\\Data\\intergers\\test\\test1k\\merged_";

		File in = new File(fileLocation);
		int N = 250000000;
		int M = 1300000;
		int d = 9;
		MultiwayMergeSort_2 mms = new MultiwayMergeSort_2();
		long startTime = System.nanoTime();
		List<String> queue = mms.split(in, N, M, outFolder);
		mms.runPasses(queue, M, d, mergedFolder);
		long r4 = System.nanoTime() - startTime;
		System.out.println("time 4: " + r4);
		
	}
}
