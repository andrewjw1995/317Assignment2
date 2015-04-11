package bitpacker;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;

public class BitInputStream implements Closeable
{
	private final byte[] MASKS = { 0, 1, 3, 7, 15, 31, 63, 127, -1 };
	
	private int buffer;
	private int index = 0;
	
	private final InputStream input;
	
	public BitInputStream(InputStream stream)
	{
		input = stream;
	}
	
	private void fill() throws IOException
	{
		buffer = input.read();
		index = 8;
	}
	
	public int readBit() throws IOException
	{
		if (buffer == -1)
			return -1;

		if (index == 0)
			fill();

		if (buffer == -1)
			return -1;

		index--;
		return (buffer >> index) & MASKS[1];
	}
	
	public int readByte() throws IOException
	{
		if (buffer == -1)
			return -1;

		if (index == 0)
		{
			buffer = input.read();
			return buffer;
		}
		
		int top = buffer & MASKS[index];
		buffer = input.read();

		if (buffer == -1)
			return -1;
		
		int overflow = 8 - index;
		int bottom = (buffer >> index) & MASKS[overflow];
		
		return top << overflow | bottom;
	}
	
	public long readBits(int bits) throws IOException
	{
		long result = 0;
		for (int offset = bits; offset > 0; offset--)
		{
			int bit = readBit();
			if (bit == -1)
				return -1;
			result = (result << 1) | bit;
		}
		return result;
	}

	@Override
	public void close() throws IOException
	{
		input.close();
	}
}