package entities;

import java.io.Serializable;

public class CopyOfBook implements Serializable
{
	private String id;
	private String status;
	
	public CopyOfBook(String id, String status) 
	{
		super();
		this.id = id;
		this.status = status;
	}
	
	public CopyOfBook(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status = status;
	}	
}
