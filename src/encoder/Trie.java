package encoder;

import java.util.ArrayList;
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

    private Trie(K[] sequence, int offset, V value)
    {
    	key = sequence[offset];
    	if (sequence.length - offset > 1)
    		children.add(new Trie<K, V>(sequence, offset + 1, value));
    	else
    		this.value = value;
    }
    
    private Trie(List<K> sequence, int offset, V value)
    {
    	key = sequence.get(offset);
    	if (sequence.size() - offset > 1)
    		children.add(new Trie<K, V>(sequence, offset + 1, value));
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
    	return add(sequence, 0, value);
    }
    
    public V add(List<K> sequence, int offset, V value)
    {
    	if (sequence.size() == offset)
    	{
    		V old = this.value;
    		this.value = value;
    		return old;
    	}
    	
		K key = sequence.get(offset);
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.add(sequence, offset + 1, value);
		
		children.add(new Trie<K, V>(sequence, offset, value));
		return null;
    }
    
    public V add(K[] sequence, int offset, V value)
    {
    	if (sequence.length == offset)
    	{
    		V old = this.value;
    		this.value = value;
    		return old;
    	}
    	
		K key = sequence[offset];
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.add(sequence, offset + 1, value);
		
		children.add(new Trie<K, V>(sequence, offset, value));
		return null;
    }
    
    /*
     * Returns the value stored for the sequence, or null if it does not exist.
     */
    public V get(K[] sequence)
    {
    	return get(sequence, 0);
    }
    
    public V get(List<K> sequence, int offset)
    {
    	if (sequence.size() == offset)
    		return value;

		K key = sequence.get(offset);
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.get(sequence, offset + 1);
    	
		return null;
    }
    
    public V get(K[] sequence, int offset)
    {
    	if (sequence.length == offset)
    		return value;

		K key = sequence[offset];
		for (Trie<K, V> child : children)
			if (child.key == key)
				return child.get(sequence, offset + 1);
    	
		return null;
    }
}
