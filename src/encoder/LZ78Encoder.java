/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encoder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LZ78Encoder implements Closeable {
	private Trie<Byte, Integer> dictionary = new Trie<Byte, Integer>();
	private int size = 1; // trie has size method, but it expands all nodes
	private InputStream input;
	private OutputStream output;

	public LZ78Encoder(InputStream input, OutputStream output)
	{
		this.input = input;
		this.output = output;
		dictionary.add(new ArrayList<Byte>(), 0);
	}
    
    public void encode() throws IOException
    {
    	List<Byte> phrase = new ArrayList<Byte>();
    	
    	while(true)
    	{
    		int next = input.read();
    		if (next == -1)
    		{
    			if (phrase.size() > 0)
    				write(phrase);
    			return;
    		}
    		
    		phrase.add((byte)next);
    		Integer index = dictionary.get(phrase);
    		if (index == null)
    		{
    			dictionary.add(phrase, size);
    			size++;
    			write(phrase);
    		}
    	}
    }
    
    // Assumes phrase exists in dictionary
    private void write(List<Byte> phrase) throws IOException
    {
		byte mismatch = phrase.remove(phrase.size() - 1);
		int index = dictionary.get(phrase);
		phrase.clear();
		
    	output.write(index << 24);
    	output.write(index << 16);
    	output.write(index << 8);
    	output.write(index);
    	output.write(mismatch);
    }

	@Override
	public void close() throws IOException
	{
		input.close();
		output.close();
	}
	
	public static void main(String[] args)
	{
		try(LZ78Encoder encoder = new LZ78Encoder(System.in, System.out))
		{
			encoder.encode();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.flush();
		}
	}
}
