package bitpacker;

public class Packer
{
	public static void main(String[] args)
	{
		try(BitOutputStream output = new BitOutputStream(System.out))
		{
			int phrases = 1; // The escape phrase (0)
			int bits = 0;
			
			while (true)
			{
				int phrase = System.in.read();
				int mismatch = System.in.read();
				if (phrase == -1 || mismatch == -1)
					break;
				
				output.write(phrase, bits);
				output.write((byte)mismatch);

				// phrase is an index, so we add one to compare to the total
				// mismatch byte creates a new phrase
				if (phrase + 1 == phrases)
				{
					if ((phrases & (phrases - 1)) == 0)
						bits++;
					phrases++;
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("An unhandled runtime exception has occurred");
			e.printStackTrace();
		}
	}
}
