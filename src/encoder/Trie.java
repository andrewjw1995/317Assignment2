package encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Represents a trie where K is the type of the key, only used as sequences, and V is the type of the values.
 */
public class Trie<K, V>
{
    private K key;
    private V value;
    // Type of the list is not important, so long as it can add, remove, and iterate
    private List<Trie<K, V>> children = new ArrayList<Trie<K, V>>();

    public Trie() { }
    
    private Trie(K[] sequence, V value)
    {
    	key = sequence[0];
    	if (sequence.length > 1)
    	{
    		K[] subsequence = Arrays.copyOfRange(sequence, 1, sequence.length);
    		children.add(new Trie<K, V>(subsequence, value));
    	}
    	else
    		this.value = value;
    }
    
    /*
     * Returns the number of values stored in the trie. Null values are not counted.
     */
    public int size()
    {
    	int size = 0;
    	if (value != null)
    		size = 1;
    	
    	for (Trie<K, V> child : children)
    		size += child.size();
    	
    	return size;
    }
    
    /*
     * Adds the value into the trie, creating any child nodes as necessary.
     * Returns the value that was previously stored for the sequence, or
     * null if it did not exist.
     */
    public V add(K[] sequence, V value)
    {
    	if (sequence.length == 0)
    	{
    		V old = this.value;
    		this.value = value;
    		return old;
    	}
    	
		K key = sequence[0];
		K[] subsequence = Arrays.copyOfRange(sequence, 1, sequence.length);
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.add(subsequence, value);
		
		children.add(new Trie<K, V>(sequence, value));
		return null;
    }
    
    /*
     * Returns the value stored for the sequence, or null if it does not exist.
     */
    public V get(K[] sequence)
    {
    	if (sequence.length == 0)
    		return value;

		K key = sequence[0];
		K[] subsequence = Arrays.copyOfRange(sequence, 1, sequence.length);
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.get(subsequence);
    	
		return null;
    }
}
