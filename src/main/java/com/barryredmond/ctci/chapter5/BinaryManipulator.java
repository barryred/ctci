package com.barryredmond.ctci.chapter5;

import com.barryredmond.util.Tuple;
import com.google.common.base.Preconditions;

/**
 * 
 */
public class BinaryManipulator 
{
	
	/**
	 * 5.3 Given a positive integer, print the next smallest and the next largest 
	 *     number that have the same number of 1 bits in the binary representation
	 */
	public Tuple<Integer, Integer> nextSmallestAndNextLargestWithSameBits(int startingValue)
	{
		return Tuple.of(nextSmallestWithSameBits(startingValue), nextLargestWithSameBits(startingValue));
	}
	
	/**
	 * 5.5 Write a function to determine the number of bits you would need to flip to convert integer A to integer B
	 * Input 29(11101) -> 15(01111) would be 2
	 */
	public int countNumberOfBitsToFlip(int a, int b)
	{
		int count = 0;
		for (int i = 0; i < 32; i++)
		{
			if ((a & 1) != (b & 1))
			{
				count++;
			}
			a = a >>> 1;
			b = b >>> 1;
		}
		return count;
	}

	private Integer nextSmallestWithSameBits(int startingValue) {
		boolean found1AfterZero = false;
		int copy = startingValue;
		int countOf1s = 0;
		int countOf0s = 0;
		for (int i = 0; i < 31; i++) // 31, since the msb is sign.
		{
			int lastBit = copy & 1;
			copy = copy >> 1;
			if (lastBit == 0)
			{
				countOf0s++;
				
			}
			else
			{
				countOf1s++;
				found1AfterZero = countOf0s > 0;
				if (found1AfterZero)
				{
					break;
				}
			}
		}
		
		if (countOf0s == 0 || countOf1s == 0 || !found1AfterZero)
		{   // All 1s or all 0s. No other number has this.
			return null;
		}
		copy = copy << 1;
		copy = copy | 0;
		for (int i = 0; i < countOf1s; i++)
		{
			copy = copy << 1;
			copy = copy | 1;
		}
		for (int i = 0; i < countOf0s-1; i++)
		{
			copy = copy << 1;
		}
		return copy;
	}
	
	private Integer nextLargestWithSameBits(int value) {
		boolean foundZeroAfterAOne = false;
		int countOf1s = 0;
		int countOf0s = 0;
		for (int i = 0; i < 31; i++) // 31, since the msb is sign.
		{
			int lastBit = value & 1;
			value = value >> 1;
			if (lastBit == 0)
			{
				countOf0s++;
				foundZeroAfterAOne = countOf1s > 0;
				if (foundZeroAfterAOne)
				{
					break;
				}
			}
			else
			{
				countOf1s++;
			}
		}
		
		if (countOf0s == 0 || countOf1s == 0 || !foundZeroAfterAOne)
		{   // No number is larger AND has the same no. of 1s.
			return null;
		}
		value = value << 1;
		value = value | 1;
		for (int i = 0; i < countOf0s; i++)
		{
			value = value << 1;
		}
		for (int i = 0; i < countOf1s-1; i++)
		{
			value = value << 1;
			value = value | 1;
		}
		return value;
	}

	/**
	 * 5.8 A monochrome screen is stored in a single array of bytes, allowing eight 
	 * consecutive pixels to be stored in one byte. The screen has width w, where w 
	 * is divisible by 8(that is, no byte will be spit across rows) The height of the 
	 * screen, of course, can be derived from the length of the array and the width. 
	 * Implement a function drawHorizontalLine(byte[] screen, int width, int x1, int x2, int y)
	 *  which draw a horizontal line from (x1, y) to (x2, y)
	 */
	private static final byte ALL_ONES = -128;
	public void drawHorizontalLine(byte[] screen, int width, int x1, int x2, int y)
	{
		Preconditions.checkArgument(x1 <= x2);
		Preconditions.checkArgument(x2 < width);
		Preconditions.checkArgument(width % 8 == 0);
		final int bytesPerLine = width/8;
		Preconditions.checkArgument((y+1) * bytesPerLine < screen.length);
				
		byte firstByteValue = _printSuffixBits(8 - (x1 % 8));
		byte lastByteValue = _printPrefixBits(1 + (x2 % 8));
		
		int firstByteIndex = (bytesPerLine * y) + x1 / 8;
		int lastByteIndex = (bytesPerLine * y) + x2 / 8;
		
		screen[firstByteIndex] = firstByteValue;
		for (int i = firstByteIndex+1; i < lastByteIndex; i++)
		{
			screen[i] = ALL_ONES;
		}
		screen[lastByteIndex] = lastByteValue;
	}

	private byte _printPrefixBits(int bits) {
		byte b = _printSuffixBits(bits);
		return (byte) (b << 8-bits);
	}

	private byte _printSuffixBits(int bits) {
		int b = 0;
		for (int i = 0; i < bits; i++)
		{
			b = b << 1;
			b = b | 1;
		}
		return (byte) b;
	}
}
