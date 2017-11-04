package com.barryredmond.util;

public class Tuple<S1, S2> 
{
	public static <S1, S2> Tuple<S1, S2> of(S1 first, S2 second) {
		return new Tuple<>(first, second);
	}

	private final S1 first;
	private final S2 second;
	
	public Tuple (S1 first, S2 second) {
		this.first = first;
		this.second = second;
	}

	public S1 getFirst()
	{
		return first;
	}
	
	public S2 getSecond()
	{
		return second;
	}
}
