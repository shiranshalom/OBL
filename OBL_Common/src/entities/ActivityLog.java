package entities;

import java.io.Serializable;

public class ActivityLog implements Serializable
{
	public String activity;
	public String bookname;
	public String date;
	public String comments;

	public ActivityLog(String activity, String bookname, String date, String comments)
	{
		this.activity = activity;
		this.bookname = bookname;
		this.date = date;
		this.comments = comments;
	}

	public String getActivity()
	{
		return activity;
	}

	public void setActivity(String activity)
	{
		this.activity = activity;
	}

	public String getBookname()
	{
		return bookname;
	}

	public void setBookname(String bookname)
	{
		this.bookname = bookname;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}
}
