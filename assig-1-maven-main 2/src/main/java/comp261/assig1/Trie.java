package comp261.assig1;

import java.util.*;

/**
 * This is an implementation of a trie, used for the search box.
 */

public class Trie {
	TrieNode root = new TrieNode(); // the root node of the trie

	public Trie() {
	}

	/**
	 * Adds a given stop to the Trie.
	 */
	public void add(Stop stop) {
		// TODO
		// write the code to add a stop object into the trie
		TrieNode node = root; //start from root node
		char [] stopName = new char[stop.getName().length()]; //appened char of stops 
		int stop_len = stop.getName().length(); //length of stop char

		//run through the length of stop name
		for(int i = 0; i<stop_len;i++){
			stopName[i]=stop.getName().toLowerCase().charAt(i); //store characters
		}
		//run through the char
		for(char n : stopName){
			if(!node.children.keySet().contains(n)){ //if not found
				TrieNode childNode = new TrieNode();
				node.children.put(n, childNode); //add to hashmap
			}
			node = node.children.get(n); 
		}
		node.data.add(stop); //add the stop with same char
		// END of TODO
	}


	/**
	 * Returns all the stops whose names start with a given prefix.
	 */
	public List<Stop> get(char[] word) {
		// TODO
		// write the code to get all the stops whose names match the prefix.
		TrieNode node = root; //start from top
		for(char n : word){ //for each char in word[]
			//return null if char is not found in the keySet
			if(!node.children.keySet().contains(n)) return null;
			node = node.children.get(n);
		}
		//return the list
		return node.data;

		// END of TODO
	}
    /**
	 * 
	 * return all the stops that are connected to the given prefix
	 * 
	 */
	public HashMap<String, Stop> getAll(String prefix){
		List<Stop> stops = new ArrayList<Stop>(); // list of stops
		HashMap<String, Stop> allStops = new HashMap<String, Stop>(); //hashmap of all the stops
		char[] word = new char[prefix.length()]; //declare char[] with the prefix length
		for(int i = 0; i < prefix.length(); i++){ //run through all the characters in the prefix
			word[i] = prefix.charAt(i); 
		}
		TrieNode node = root; //start at the top node
		for(char c : word){ //for each character in the word
			//return null if it is not found
			if(!node.children.keySet().contains(c)){
				return null;

			}
			//else appened
			node = node.children.get(c);
		}

		getAllFrom(node, stops); //grab all the stops found in the node
		for(Stop s: stops){ 
			allStops.put(s.getId(),s); //then add the stops' individual id together with the stop
		}
		return allStops; //return hashmap with the stops of the given prefix
	} 	
	/**
	 * 
	 * add the data to the list of stops 
	 * gather all the stops in the node
	 */
		public void getAllFrom(TrieNode node, List<Stop> stops){
			for(int i = 0;i < node.data.size(); i++){
				stops.add(node.data.get(i));
			}
			node.children.values().forEach(child ->  {
				getAllFrom(child, stops);
			});
		}

	/**
	 * Represents a single node in the trie. It contains a collection of the
	 * stops whose names are exactly the traversal down to this node.
	 */
	private class TrieNode {
		List<Stop> data = new ArrayList<>();
		Map<Character, TrieNode> children = new HashMap<>();
	}
}