package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Kishwar KUMAR
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
		size = 0;
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word)
	{
	    // Implement this method.
		word = word.toLowerCase();
		TrieNode current = root;
		int c = 0;
		while(true) {
			if(current.getText().compareTo(word) == 0) {
				if(!current.endsWord()) {
					size++;
				}
				current.setEndsWord(true);
				return false;
			} else if(current.getChild(word.charAt(c=c<word.length()?c:word.length()-1)) == null) {
				break;
			} else {
				current = current.getChild(word.charAt(c));
				c++;
			}
		}
		
		while(c < word.length()) {
			current = current.insert(word.charAt(c));
			c++;
		}
		current.setEndsWord(true);
		size++;
	    return true;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    // Implement this method
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) 
	{
	    // Implement this method
		s = s.toLowerCase();
		TrieNode current = root;
		int c = 0;
		while(true) {
			if(current.getText().compareTo(s) == 0) {
				break;
			} else if(current.getChild(s.charAt(c=c<s.length()?c:s.length()-1)) == null) {
				return false;
			} else {
				current = current.getChild(s.charAt(c));
				c++;
			}
		}
		
	    return current.endsWord();
	}

	/** 
	 *  * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param text The text to use at the word stem
     * @param n The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 
    	 List<String> ListToReturn = new LinkedList<String>();
    	 if((prefix=="") && (numCompletions==0)) {
    		 return ListToReturn;
    	 }
    	 
    	 Queue<TrieNode> q = new LinkedList<TrieNode>();
    	 TrieNode start = root;
    	 
    	 if(prefix != "") {
    		 for(int c=0; c<prefix.length(); c++) {
    			 start = start.getChild(prefix.charAt(c));
    			 if(start == null)
    				 return ListToReturn;
    			 if(start.endsWord()  && (start.getText().length() >= prefix.length()))
    				 ListToReturn.add(start.getText());
    		 }
    	 }
    	 
    	 for(Character c: start.getValidNextCharacters()) {
    		 q.add(start.getChild(c));
    	 }
    	 
    	 while(!q.isEmpty()) {
    		 if(ListToReturn.size() == numCompletions)
    			 break;
    		 TrieNode current = q.remove();
    		 if(current != null) {
    			 if(current.endsWord() && (current.getText().length() >= prefix.length()))
    				 ListToReturn.add(current.getText());
    			 for(Character d: current.getValidNextCharacters())
    				 q.add(current.getChild(d));
    		 }
    	 }
         return ListToReturn;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}