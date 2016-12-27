package sort;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

class InsertionPQ{
//	private ArrayList<Integer> keys;
//	private ArrayList<Integer> sortedKeys;
//	private ArrayList<Integer> ind;
	public ArrayList<Integer> keys;
	public ArrayList<Integer> sortedKeys;
	public ArrayList<Integer> ind;
	
	
	public InsertionPQ(){
		keys = new ArrayList<Integer>();
		sortedKeys = new ArrayList<Integer>();
		ind = new ArrayList<Integer>();
	}
	public void insert(int e){
		keys.add(e);
		int i = 0;
		int in =  keys.size()-1;
		for (; i < sortedKeys.size(); i++){
			if (sortedKeys.get(i) > e){
				sortedKeys.add(i, e);
				ind.add(i, in);
				break;
			}
		}
		if (i==sortedKeys.size()){
			sortedKeys.add(e);
			ind.add(in);

		}
		
	}
	public void insert(int index, int e){
		keys.add(index, e);
		int i;
		
		for (i = 0; i < ind.size(); i++){
			int current = ind.get(i);
			if (current >= index)
				ind.set(i, current+1);
		}
		
		for (i = 0; i < sortedKeys.size(); i++){
			if (sortedKeys.get(i) > e){
				sortedKeys.add(i, e);
				ind.add(i, index);
				break;
			}
		}
		if (i==sortedKeys.size()){
			sortedKeys.add(e);
			ind.add(index);

		}
	}
	public void removeMin(){
		if (isEmpty())
			return;
		int minIndex = getMinIndex();
		keys.remove(minIndex);
		sortedKeys.remove(0);
		ind.remove(0);
		for (int i = 0; i < ind.size(); i++){
			int current = ind.get(i);
			if (current > minIndex)
				ind.set(i, current-1);
		}
	}
	public int getMinValue(){
		return sortedKeys.get(0);
	}
	public int getMinIndex(){
		return ind.get(0);
	}
	
	public boolean isEmpty(){
		return keys.isEmpty();
	}
}
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
			if (!a.isEmpty())
				pq.insert(a.get(0));
				a.remove(0);
		}
		int minIndex;
		while (!pq.isEmpty()){
			result.add(pq.getMinValue());
			minIndex = pq.getMinIndex();
			System.out.println(pq.keys);
			pq.removeMin();
			System.out.println(result);
			
			if (!inputs.get(minIndex).isEmpty()){
				pq.insert(minIndex, inputs.get(minIndex).get(0));
				inputs.get(minIndex).remove(0);
			}
		}
		System.out.println(result);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		InsertionPQ pq = new InsertionPQ();
//		pq.insert(0,7);
//		pq.insert(0,8);
//		pq.insert(0,12);
//		pq.insert(0,4);
//		System.out.println(pq.ind);
//		System.out.println(pq.keys);
//		System.out.println(pq.sortedKeys);
		
		Integer[] a = {1, 3, 7, 8};
		ArrayList<Integer> aList = new ArrayList(Arrays.asList(a));
		Integer[] b = {2, 3, 5, 8};
		ArrayList<Integer> bList = new ArrayList(Arrays.asList(b));
		Integer[] c = {4, 6};
		ArrayList<Integer> cList = new ArrayList(Arrays.asList(c));
		ArrayList<ArrayList<Integer>> inputs = new ArrayList<ArrayList<Integer>>();
		inputs.add(aList);
		inputs.add(bList);
		inputs.add(cList);
		System.out.println(inputs.get(2));
		mergeSort(inputs);

	}

}
