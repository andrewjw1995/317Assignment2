package bitpacker;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

// USES BIG-ENDIAN ORDER
public class BitOutputStream implements Closeable
{
	private final byte[] MASKS = { 0, 1, 3, 7, 15, 31, 63, 127, -128 };
	
	private byte buffer;
	private int index = 0;
	
	private final OutputStream output;
	
	public BitOutputStream(OutputStream stream)
	{
		output = stream;
	}
	
	public void write(boolean value) throws IOException
	{
		index++;
		
		if (value)
			buffer |= (byte) 1 << (8 - index);
		
		if (index == 8)
			flush();
	}
	
	public void write(byte value) throws IOException
	{
		buffer |= value >> index;
		output.write(buffer);
		buffer = (byte) (value << (8 - index));
	}
	
	public void write(int value) throws IOException
	{
		for (int offset = 24; offset >= 0; offset -= 8)
			write((byte) (value >>> offset));
	}
	
	public void write(byte value, int bits) throws IOException
	{
		if (bits == 0)
			return;
		
		int total = index + bits;
		if (total >= 8)
		{
			int overflow = total - 8;
			buffer |= value >>> overflow;
			output.write(buffer);
			buffer = (byte) (value << (8 - overflow));
			index = overflow;
		}
		else
		{
			buffer |= (value & MASKS[bits]) << (8 - total);
			index = total;
		}
	}
	
	public void write(int value, int bits) throws IOException
	{
		while(bits > 8)
		{
			bits -= 8;
			write((byte) (value >>> bits));
		}
		
		write((byte)value, bits);
	}
	
	public void flush() throws IOException
	{
		output.write(buffer);
		buffer = 0;
		index = 0;
	}

	@Override
	public void close() throws IOException
	{
		if (index > 0)
			flush();
		output.close();
	}
}
