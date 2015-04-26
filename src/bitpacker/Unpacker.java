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
			
			while (true)
			{
				long phrase = input.readBits(bits);
				long mismatch = input.readBits(8);
				
				if (phrase == -1 || mismatch == -1)
					break;
				
				byte[] bytes = ByteBuffer.allocate(5).putInt((int)phrase).put((byte) mismatch).array();
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
