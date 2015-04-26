package encoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*
 * Represents a trie where K is the type of the key, only used as sequences, and V is the type of the values.
 */
public class Trie<K extends Comparable<K>, V> implements Comparable<K>
{
    private K key;
    private V value;
    // Type of the list is not important, so long as it can add, remove, and iterate
    private List<Trie<K, V>> children = new LinkedList<Trie<K, V>>();
    
    // Our algorithm will repeatedly search the trie with one more key in the sequence
    // Storing the last accessed child will give good speed up.
    private Trie<K, V> last;

    public Trie() { }
    
    private Trie(List<K> sequence, int offset, V value)
    {
    	key = sequence.get(offset);
    	if (sequence.size() - offset > 1)
    	{
    		last = new Trie<K, V>(sequence, offset + 1, value);
    		children.add(last);
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
    public V add(List<K> sequence, V value)
    {
    	return add(sequence, 0, value);
    }
    
    private V add(List<K> sequence, int offset, V value)
    {
    	if (sequence.size() == offset)
    	{
    		V old = this.value;
    		this.value = value;
    		return old;
    	}
    	
		K key = sequence.get(offset);
		if (last != null && last.key == key)
			return last.add(sequence, offset + 1, value);
		
		int index = Collections.binarySearch(children, key);
	    if (index >= 0)
	    {
			last = children.get(index);
			return last.add(sequence, offset + 1, value);
	    }
	    
	    children.add(-index - 1, new Trie<K, V>(sequence, offset, value));
		return null;
    }
    
    /*
     * Returns the value stored for the sequence, or null if it does not exist.
     */
    public V get(List<K> sequence)
    {
    	return get(sequence, 0);
    }
    
    private V get(List<K> sequence, int offset)
    {
    	if (sequence.size() == offset)
    		return value;

		K key = sequence.get(offset);
		if (last != null && last.key == key)
			return last.get(sequence, offset + 1);

		int index = Collections.binarySearch(children, key);
	    if (index >= 0)
	    {
			last = children.get(index);
			return last.get(sequence, offset + 1);
	    }
    	
		return null;
    }

	@Override
	public int compareTo(K other) {
		return key.compareTo(other);
	}
}
