package encoder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LZ78Decoder implements Closeable
{
	private List<byte[]> dictionary = new ArrayList<byte[]>();
	private InputStream input;
	private OutputStream output;
	
	public LZ78Decoder(InputStream input, OutputStream output)
	{
		this.input = input;
		this.output = output;
		dictionary.add(new byte[0]);
	}
	
	public void decode() throws IOException
	{
		byte[] bytes = new byte[5];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		while(input.read(bytes) == 5)
		{
			buffer.clear();
			int index = buffer.getInt();
			byte mismatch = buffer.get();
			
			byte[] phrase = dictionary.get(index);
			byte[] extended = Arrays.copyOf(phrase, phrase.length + 1);
			extended[phrase.length] = mismatch;
			dictionary.add(extended);
			output.write(extended);
		}
		close();
	}

	@Override
	public void close() throws IOException
	{
		input.close();
		output.close();
	}
	
	public static void main(String[] args)
	{
		try(LZ78Decoder decoder = new LZ78Decoder(System.in, System.out))
		{
			decoder.decode();
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
