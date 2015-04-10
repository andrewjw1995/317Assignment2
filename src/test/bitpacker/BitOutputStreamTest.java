package test.bitpacker;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import bitpacker.BitOutputStream;

public class BitOutputStreamTest
{
	private final File testoutput;

	public BitOutputStreamTest() throws IOException
	{
		testoutput = File.createTempFile("BitOutputStreamTest", "tmp");
	}
	
	@Test
	public void booleansShouldBeWritten()
	{
		try (BitOutputStream stream = new BitOutputStream(new FileOutputStream(testoutput)))
		{
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write(false);
			stream.write(false);
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for writing");
		}
		catch (IOException e)
		{
			fail("Could not write to temporary output file");
		}

		byte[] expected = new byte[] { 108 };
		expectValuesToBeInFile(expected);
	}
	
	@Test
	public void booleansAndBytesShouldBeInterleaved()
	{
		try (BitOutputStream stream = new BitOutputStream(new FileOutputStream(testoutput)))
		{
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write((byte) -58); //11000110
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write(false);
			stream.write(false);
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for writing");
		}
		catch (IOException e)
		{
			fail("Could not write to temporary output file");
		}

		byte[] expected = new byte[] { 120, -52 };
		expectValuesToBeInFile(expected);
	}
	
	@Test
	public void booleansAndIntsShouldBeInterleaved()
	{
		try (BitOutputStream stream = new BitOutputStream(new FileOutputStream(testoutput)))
		{
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write(917255852); // 00110 11010101 10000110 11010101 100
			stream.write(false);
			stream.write(true);
			stream.write(true);
			stream.write(false);
			stream.write(false);
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for writing");
		}
		catch (IOException e)
		{
			fail("Could not write to temporary output file");
		}
		
		byte[] expected = new byte[] { 102, -43, -122, -43, -116 };
		expectValuesToBeInFile(expected);
	}
	
	@Test
	public void partialBytesShouldBeWritten()
	{
		try (BitOutputStream stream = new BitOutputStream(new FileOutputStream(testoutput)))
		{
			stream.write((byte) 11, 2); // 11
			stream.write((byte) 10, 3); // 010
			stream.write((byte) 9, 2);  // 01
			stream.write((byte) 13, 4); // 1101
			stream.write((byte) 13, 5); // 01101
			stream.write((byte) 77, 8);
			// 11010011 10101101
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for writing");
		}
		catch (IOException e)
		{
			fail("Could not write to temporary output file");
		}
		
		byte[] expected = new byte[] { -45, -83, 77 };
		expectValuesToBeInFile(expected);
	}
	
	@Test
	public void partialIntsShouldBeWritten()
	{
		try (BitOutputStream stream = new BitOutputStream(new FileOutputStream(testoutput)))
		{
			stream.write(14, 3); 		// 110
			stream.write(2675547, 21);	// 010001101001101011011
			stream.write(27505, 16);	// 0110101101110001
			// 11001000 11010011 01011011 01101011 01110001
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for writing");
		}
		catch (IOException e)
		{
			fail("Could not write to temporary output file");
		}
		
		byte[] expected = new byte[] { -56, -45, 91, 107, 113 };
		expectValuesToBeInFile(expected);
	}
	
	private void expectValuesToBeInFile(byte[] values)
	{
		final int total = values.length * 8;
		try (FileInputStream stream = new FileInputStream(testoutput))
		{
			for (int i = 0; i < values.length; i++)
			{
				int value = stream.read();
				if (value == -1)
					fail("Expected " + total + " bits to be written to the file, but " + i * 8 + " were");
				
				byte actual = (byte) value;
				if (actual != values[i])
					fail("Expected " + values[i] + " to be the next value in the file, but instead read " + actual);
			}
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary output file for reading");
		}
		catch(IOException e)
		{
			fail("Could not read from temporary output file");
		}
	}
}
