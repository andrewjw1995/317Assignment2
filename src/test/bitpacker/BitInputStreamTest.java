package test.bitpacker;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import bitpacker.BitInputStream;

public class BitInputStreamTest
{
	private final File testinput;

	public BitInputStreamTest() throws IOException
	{
		testinput = File.createTempFile("BitInputStreamTest", "tmp");
	}

	@Test
	public void bitsShouldBeReadable()
	{
		// 01101010 01101010
		byte[] data = new byte[]{ 106, 106 };
		putValuesInFile(data);
		
		try (BitInputStream stream = new BitInputStream(new FileInputStream(testinput)))
		{
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(-1, stream.readBit());
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary input file for reading");
		}
		catch (IOException e)
		{
			fail("Could not read from temporary input file");
		}
	}
	
	@Test
	public void bytesShouldBeInterleaved()
	{
		// 01101010 01101010 01010000
		byte[] data = new byte[]{ 106, 106, 80 };
		putValuesInFile(data);
		
		try (BitInputStream stream = new BitInputStream(new FileInputStream(testinput)))
		{
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(1, stream.readBit());
			
			assertEquals(83, stream.readByte());

			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());
			assertEquals(1, stream.readBit());
			assertEquals(0, stream.readBit());

			assertEquals(80, stream.readByte());
			
			assertEquals(-1, stream.readBit());
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary input file for reading");
		}
		catch (IOException e)
		{
			fail("Could not read from temporary input file");
		}
	}
	
	@Test
	public void partialIntsShouldBeReadable()
	{
		// 110.0101.0 10010110 00.110001 01010111 01101010 01010111
		byte[] data = new byte[]{ -54, -106, 49, 87, 106, 87 };
		putValuesInFile(data);
		
		try (BitInputStream stream = new BitInputStream(new FileInputStream(testinput)))
		{
			assertEquals(6, stream.readBits(3));
			assertEquals(5, stream.readBits(4));
			assertEquals(600, stream.readBits(11));
			assertEquals(49, stream.readBits(6));
			assertEquals(22378, stream.readBits(16));
			assertEquals(87, stream.readBits(8));
			assertEquals(-1, stream.readBits(1));
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary input file for reading");
		}
		catch (IOException e)
		{
			fail("Could not read from temporary input file");
		}
	}
	
	private void putValuesInFile(byte[] values)
	{
		try(FileOutputStream stream = new FileOutputStream(testinput))
		{
			stream.write(values);
		}
		catch (FileNotFoundException e)
		{
			fail("Could not open temporary input file for writing");
		}
		catch(IOException e)
		{
			fail("Could not write to temporary input file");
		}
	}
}
