package entities;

import java.io.Serializable;
import java.util.Map;

public class Report_BorrowDurationInfo implements Serializable
{
	private Map<String, Integer> wantedBooks;
	private Map<String, Integer> regularBooks;
	
	/*
	 * this map is between wanted BookID and average borrow time in dates
	 */
	public Map<String, Integer> getWantedBooks()
	{
		return wantedBooks;
	}
	public void setWantedBooks(Map<String, Integer> wantedBooks)
	{
		this.wantedBooks = wantedBooks;
	}
	/*
	 * this map is between regular BookID and average borrow time in dates
	 */
	public Map<String, Integer> getRegularBooks()
	{
		return regularBooks;
	}
	public void setRegularBooks(Map<String, Integer> regularBooks)
	{
		this.regularBooks = regularBooks;
	}
	
	

}
