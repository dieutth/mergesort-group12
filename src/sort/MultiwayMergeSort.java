package sort;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

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
/* HeapPQ implement PriorityQueue using Heap
class HeapPQ{
	private ArrayList<Integer> keys;
	private ArrayList<Integer> ind;
	private int n;
	
	public HeapPQ(){
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

*/

public class MultiwayMergeSort {
	public void mergeSort(DataInputStream[] diss){
		PriorityQueue pq = new PriorityQueue(diss.length);
		for (DataInputStream dis : diss){
			
		}
			
	}
	public static void mergeSort(ArrayList<ArrayList<Integer>> inputs){
		InsertionPQ pq = new InsertionPQ();
		ArrayList<Integer> result = new ArrayList<Integer>();
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
		System.out.println(result);
	}
	public static void main(String[] args) {
				
		Integer[] a = {16};
		ArrayList<Integer> aList = new ArrayList(Arrays.asList(a));
		Integer[] b = {2, 3, 5, 8};
		ArrayList<Integer> bList = new ArrayList(Arrays.asList(b));
		Integer[] c = {4, 6};
		ArrayList<Integer> cList = new ArrayList(Arrays.asList(c));
		ArrayList<ArrayList<Integer>> inputs = new ArrayList<ArrayList<Integer>>();
		inputs.add(aList);
		inputs.add(bList);
		inputs.add(cList);
		mergeSort(inputs);

	}

}
