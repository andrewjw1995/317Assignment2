package test.encoder;

import static org.junit.Assert.*;
import org.junit.Test;
import encoder.Trie;

public class TrieTest {
	@Test
	public void shouldAddValues() {
		Trie<Character, Integer> trie = new Trie<Character, Integer>();
		trie.add(new Character[]{}, 0);
		trie.add(new Character[]{'a'}, 1);
		trie.add(new Character[]{'a', 'a'}, 2);
		trie.add(new Character[]{'a', 'b', 'a'}, 3);
		trie.add(new Character[]{'b'}, 4);
		
	}
	
	@Test
	public void addShouldReturnOldValue() {
		Trie<Character, Integer> trie = new Trie<Character, Integer>();
		trie.add(new Character[0], 0);
		trie.add(new Character[]{'a'}, 1);
		trie.add(new Character[]{'a', 'a'}, 2);
		trie.add(new Character[]{'a', 'b', 'a'}, 3);
		trie.add(new Character[]{'b'}, 4);
		
		assertEquals(0, trie.add(new Character[0], 0).intValue());
		assertEquals(1, trie.add(new Character[]{'a'}, 0).intValue());
		assertEquals(2, trie.add(new Character[]{'a', 'a'}, 0).intValue());
		assertEquals(3, trie.add(new Character[]{'a', 'b', 'a'}, 0).intValue());
		assertEquals(4, trie.add(new Character[]{'b'}, 0).intValue());
	}
	
	@Test
	public void shouldGetValues() {
		Trie<Character, Integer> trie = new Trie<Character, Integer>();
		trie.add(new Character[0], 0);
		trie.add(new Character[]{'a'}, 1);
		trie.add(new Character[]{'a', 'a'}, 2);
		trie.add(new Character[]{'a', 'b', 'a'}, 3);
		trie.add(new Character[]{'b'}, 4);
		
		assertEquals(0, trie.get(new Character[0]).intValue());
		assertEquals(1, trie.get(new Character[]{'a'}).intValue());
		assertEquals(2, trie.get(new Character[]{'a', 'a'}).intValue());
		assertEquals(3, trie.get(new Character[]{'a', 'b', 'a'}).intValue());
		assertEquals(4, trie.get(new Character[]{'b'}).intValue());
	}
	
	@Test
	public void sizeShouldStartFromZero() {
		Trie<Character, Integer> trie = new Trie<Character, Integer>();
		assertEquals(0, trie.size());
		trie.add(new Character[0], 0);
		assertEquals(1, trie.size());
		trie.add(new Character[]{'a'}, 1);
		assertEquals(2, trie.size());
		trie.add(new Character[]{'a', 'a'}, 2);
		assertEquals(3, trie.size());
		trie.add(new Character[]{'a', 'b', 'a'}, 3);
		assertEquals(4, trie.size());
		trie.add(new Character[]{'b'}, 4);
		assertEquals(5, trie.size());
	}
}
