package com.barryredmond.ctci.chapter5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.barryredmond.util.Tuple;

public class BinaryManipulatorTest 
{
	// Fixed random seed for consistent tests;
	private static final long SEED = 98723490872345L;
	
	private final BinaryManipulator manipulator = new BinaryManipulator();
	
	@DataProvider
	public Object[][] simpleTests()
	{
		return new Object[][] {
			{10},
			{45678},
			{-103},
			{-67493},
			
		};
	}
	
	@DataProvider
	public Object[][] nextSmallestAndNextLargestWithSameBits_edgeCasesValues()
	{
		return new Object[][] {
			{0},
			{1},
			{-1},
			{Integer.MAX_VALUE},
			{Integer.MAX_VALUE - 1},
			{Integer.MIN_VALUE},
			{Integer.MIN_VALUE + 1},
		};
	}
	
	@Test(dataProvider = "simpleTests")
	public void nextSmallestAndNextLargestWithSameBits_simpleTests(int edgeCase)
	{
		testNextSmallestAndNextLargestWithSameBits(edgeCase);	
	}
	
	@Test(dataProvider = "nextSmallestAndNextLargestWithSameBits_edgeCasesValues")
	public void nextSmallestAndNextLargestWithSameBits_edgeCases(int edgeCase)
	{
		testNextSmallestAndNextLargestWithSameBits(edgeCase);	
	}
	
	@Test
	public void nextSmallestAndNextLargestWithSameBits_manyRandom()
	{
		Random random = new Random(SEED);
		for (int i = 0; i < 10000; i++)
		{
			testNextSmallestAndNextLargestWithSameBits(random.nextInt());
		}
	}

	private void testNextSmallestAndNextLargestWithSameBits(int value) {
		final Tuple<Integer, Integer> result = manipulator.nextSmallestAndNextLargestWithSameBits(value);
		_smallerTests(result.getFirst(), value);
		_biggerTests(result.getSecond(), value);
	}

	private void _smallerTests(Integer calculatedSmaller, int input) {
		if (calculatedSmaller == null)
		{ 
			_ensureOnlyLeading1s(input);
			return;
		}
		assertEquals(Integer.bitCount(calculatedSmaller), Integer.bitCount(input));
		assertTrue(calculatedSmaller < input, "Expected " + calculatedSmaller + "(" + Integer.toBinaryString(calculatedSmaller) + ") to be less than " + input + "(" + Integer.toBinaryString(input) + ")");
		for (int i = input-1; i > calculatedSmaller; i--)
		{
			assertNotEquals(Integer.bitCount(i), Integer.bitCount(input));
		}
	}

	private void _biggerTests(Integer calculatedBigger, int input) {
		if (calculatedBigger == null)
		{ 
			_ensureOnlyLeading0s(input);
			return;
		}
		assertEquals(Integer.bitCount(calculatedBigger), Integer.bitCount(input));
		assertTrue(calculatedBigger > input, "Expected " + calculatedBigger + "(" + Integer.toBinaryString(calculatedBigger) + ") to be greater than " + input + "(" + Integer.toBinaryString(input) + ")");
		for (int i = input+1; i > calculatedBigger; i++)
		{
			assertNotEquals(Integer.bitCount(i), Integer.bitCount(input));
		}
	}

	private void _ensureOnlyLeading0s(int input) {
		_ensureOnlyLeadingBit(input, 0);
	}

	private void _ensureOnlyLeading1s(int input) {
		_ensureOnlyLeadingBit(input, 1);
	}
	private void _ensureOnlyLeadingBit(int input, int leadingBit) {
		boolean foundLeadingBit = false;
		for (int i = 0; i < 31; i++)
		{
			int bit = input | 1;
			input = input >> 1;
			if (bit == leadingBit) 
			{
				foundLeadingBit = true;
			}
			else if (foundLeadingBit)
			{
				fail("Trying to ensure all leading 1s, (e.g. 11111000), but there was a a trailing 1 in  " + input + "(" + Integer.toBinaryString(input));
			}
			
		}
	}

}
