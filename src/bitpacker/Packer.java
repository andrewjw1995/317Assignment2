package bitpacker;

import java.nio.ByteBuffer;

public class Packer
{
	public static void main(String[] args)
	{
		try(BitOutputStream output = new BitOutputStream(System.out))
		{
			int phrases = 1; // The escape phrase (0)
			int bits = 0;
			byte[] bytes = new byte[5];
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			
			while (System.in.read(bytes) == 5)
			{
				buffer.clear();
				int phrase = buffer.getInt();
				byte mismatch = buffer.get();
				
				output.write(phrase, bits);
				output.write(mismatch);

				// phrase is an index, so we add one to compare to the total
				// mismatch byte creates a new phrase
				if ((phrases & (phrases - 1)) == 0)
					bits++;
				phrases++;
			}
		}
		catch(Exception e)
		{
			System.err.println("An unhandled runtime exception has occurred");
			e.printStackTrace();
		}
	}
}
