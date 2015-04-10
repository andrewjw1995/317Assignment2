package encoder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decoder implements Closeable
{
	private List<byte[]> dictionary = new ArrayList<byte[]>();
	private InputStream input;
	private OutputStream output;
	
	public Decoder(InputStream input, OutputStream output)
	{
		this.input = input;
		this.output = output;
		dictionary.add(new byte[0]);
	}
	
	public void decode() throws IOException
	{
		while(true)
		{
			int index = input.read();
			if (index == -1)
			{
				close();
				return;
			}
			
			index = index << 24;
			for (int offset = 16; offset >= 0; offset -= 8)
			{
				int part = input.read();
				if (part == -1)
					throw new IOException("File ended unexpectedly");
				index |= part << offset;
			}
			
			int mismatch = input.read();
			if (mismatch == -1)
				throw new IOException("File ended unexpectedly");
			
			byte[] phrase = dictionary.get(index);
			byte[] extended = Arrays.copyOf(phrase, phrase.length + 1);
			extended[phrase.length] = (byte) mismatch;
			dictionary.add(extended);
			
			for (byte part : extended)
				output.write(part);
		}
	}

	@Override
	public void close() throws IOException
	{
		input.close();
		output.close();
	}
}
