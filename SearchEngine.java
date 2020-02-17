import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;  
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);

	}
	 /* This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 */
	public void crawlAndIndex(String url) throws Exception {
		internet.addVertex(url);
		internet.setVisited(url, true);	
		ArrayList<String>wordsFound = new ArrayList<String>();
		wordsFound = parser.getContent(url);
		for(int i = 0; i<wordsFound.size(); i++){ 
			String word = wordsFound.get(i).toLowerCase();
			ArrayList<String> urls = wordIndex.get(word);
			if(urls==null){
				urls = new ArrayList<String>();
				urls.add(url);
				wordIndex.put(word, urls);
			}else {
				if(!urls.contains(url)){
					urls.add(url);
				}
			} 
		}
		ArrayList<String> links = new ArrayList<String>();
		links = parser.getLinks(url);
		for(String link: links) {
			internet.addVertex(link);	
			internet.addEdge(url, link);
			if (!(internet.getVisited(link))) {
				crawlAndIndex(link);
			}
		}
	}
	
	/* This computes the pageRanks for every vertex in the web graph.*/
	public void assignPageRanks(double epsilon) {
		for(String url: internet.getVertices()){
			internet.setPageRank(url, 1.0);
		}
		ArrayList<Double> current = new ArrayList<Double>();
		ArrayList<Double> previous = new ArrayList<Double>();
		while(!(compareRanks(previous, current, epsilon))) {
			previous = current;
			current = computeRanks(internet.getVertices());
			ArrayList<String> vertices = internet.getVertices();
			for(int i = 0 ; i < vertices.size(); i++) {
				internet.setPageRank(vertices.get(i), current.get(i));
			}
		}
	}

	public boolean compareRanks(ArrayList<Double> previous, ArrayList<Double> current, double epsilon) {
		if(previous.isEmpty() || current.isEmpty())
			return false;
		for(int i = 0; i<previous.size(); i++) {
			if((Math.abs(previous.get(i)- current.get(i))>= epsilon)){
				return false;
			}
		}
		return true;
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList<Double> pr = new ArrayList<Double>();
		double pageRankV = 0;
		for(int i = 0; i <vertices.size(); i++) {
			double sum = 0; 
			ArrayList<String> w = internet.getEdgesInto(vertices.get(i));
			for(int j = 0; j<internet.getEdgesInto(vertices.get(i)).size(); j++) {
				double wCalculate= (internet.getPageRank(w.get(j))/internet.getOutDegree(w.get(j)));
				sum += wCalculate;
			}
			pageRankV = 0.5+0.5*(sum);
			pr.add(pageRankV);
		}
		return pr;
	}

	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 */
	public ArrayList<String> getResults(String query) {
		ArrayList<String> urls= new ArrayList<String>();
		ArrayList<Double> ranks = new ArrayList<Double>();
		HashMap< String, Double> collection = new HashMap <String, Double>();
		if(wordIndex.containsKey(query)){
			urls = wordIndex.get(query);
		}
		for(int i = 0; i<urls.size(); i++) {
			collection.put(urls.get(i),internet.getPageRank(urls.get(i)));
		}
		return Sorting.fastSort(collection);
	}
	//	public static void main(String []args) throws Exception {
	//		SearchEngine google = new SearchEngine("test.xml");
	//		google.crawlAndIndex("www.unity.com");
	//		google.assignPageRanks(2.0);
	//		System.out.print(google.getResults("life"));
	//
	//		//SearchEngine chrome = new SearchEngine("testAcyclic.xml");
	//		//chrome.crawlAndIndex("siteA");
	//
	//	}
}
