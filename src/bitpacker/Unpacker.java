package bitpacker;

import java.nio.ByteBuffer;

public class Unpacker
{
	public static void main(String[] args)
	{
		try(BitInputStream input = new BitInputStream(System.in))
		{
			int phrases = 1; // The escape phrase (0)
			int bits = 0;
			byte[] bytes = new byte[5];
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			
			while (true)
			{
				long phrase = input.readBits(bits);
				long mismatch = input.readBits(8);
				
				if (phrase == -1 || mismatch == -1)
					break;
				
				buffer.clear();
				buffer.putInt((int)phrase);
				buffer.put((byte) mismatch);
				System.out.write(bytes);
				
				// Power of two - https://graphics.stanford.edu/~seander/bithacks.html#DetermineIfPowerOf2
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
		finally
		{
			System.out.flush();
		}
	}
}
