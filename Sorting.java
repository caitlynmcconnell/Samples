
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry; 

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 */

	public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K,V> results) {
		ArrayList<K> sortedUrls = new ArrayList<K>();
		sortedUrls.addAll(results.keySet());
		mergeSort(results, sortedUrls, 0, sortedUrls.size()-1);
		return sortedUrls;
	}

	private static  <K, V extends Comparable> void mergeSort(HashMap<K,V> map, ArrayList<K> sortedUrls, int begin, int end)  {
		ArrayList<K> merged = new ArrayList<K>();	
		if(begin>=end) {
			return;
		}
		int halfway = ((begin+end)/2);
		mergeSort(map, sortedUrls, begin, halfway);
		mergeSort(map, sortedUrls, halfway+1, end);
		merge(map, sortedUrls, begin, halfway, end);

	}

	private static <K, V extends Comparable> void merge(HashMap<K,V> map, ArrayList<K> sortedUrls, int begin, int halfway, int end ) {
		ArrayList<K> list1 = new ArrayList<K>();
		ArrayList<K> list2 = new ArrayList<K>();
		int n1 = halfway-begin +1;
		int n2 = end-halfway;

		for(int i = 0; i<n1; i++) {
			list1.add(sortedUrls.get(begin+i));
		}

		for(int j = 0; j<n2; j++) {
			list2.add(sortedUrls.get(halfway+1+j));
		}

		int i = 0; 
		int j = 0; 
		int position = begin;

		while(i<n1 && j<n2) {
			if(map.get(list1.get(i)).compareTo(map.get(list2.get(j)))>= 0) {
				sortedUrls.set(position,list1.get(i));
				i++;
			}else {
				sortedUrls.set(position, list2.get(j));
				j++;

			}
			position++;
		}

		while(i<n1) {
			sortedUrls.set(position,list1.get(i));
			i++;
			position++;
		}

		while(j<n2) {
			sortedUrls.set(position,list2.get(j));
			j++;
			position++;
		}

	}

