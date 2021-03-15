package entities;

import java.io.Serializable;

public class BorrowExtension  implements Serializable
{
	private String id;
	private BorrowACopyOfBook borrow; // private BorrowACopyOfBook borrowToUpdate; 
	private String extensionDate;
	private String extensionType; //ENUM('automatic', 'manually');
	private String userId; // private User user;
	
	
	
	public BorrowExtension(String id, BorrowACopyOfBook borrow, String extensionDate, String extensionType,
			String userId) 
	{
		this.id = id;
		this.borrow = borrow;
		this.extensionDate = extensionDate;
		this.extensionType = extensionType;
		this.userId = userId;
	}

	public BorrowExtension(BorrowACopyOfBook borrowToExtend) 
	{
		this.borrow = borrowToExtend;
	}
		
	public BorrowExtension(BorrowACopyOfBook borrow, String extensionDate, String extensionType, String userId)
	{
		this.borrow = borrow;
		this.extensionDate = extensionDate;
		this.extensionType = extensionType;
		this.userId = userId;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	
	public BorrowACopyOfBook getBorrow() 
	{
		return borrow;
	}

	public void setBorrow(BorrowACopyOfBook borrow)
	{
		this.borrow = borrow;
	}

	public String getExtensionDate()
	{
		return extensionDate;
	}

	public void setExtensionDate(String extensionDate)
	{
		this.extensionDate = extensionDate;
	}

	public String getExtensionType()
	{
		return extensionType;
	}

	public void setExtensionType(String extensionType)
	{
		this.extensionType = extensionType;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	

}
