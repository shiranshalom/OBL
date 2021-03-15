package entities;

import java.io.Serializable;

public class BorrowACopyOfBook implements Serializable
{
	private String id;
	private String subscriberId;
	private String borrowDate;
	private String expectedReturnDate;
	private String actualReturnDate;
	private String isReturnedLate;
	private String bookCatalogNumber;
	private String copyId;
	private boolean flag;
	
	public BorrowACopyOfBook() {}
	public BorrowACopyOfBook(String id, String subscriberId, String borrowDate, String expectedReturnDate,
			String actualReturnDate, String isReturnedLate, String bookCatalogNumber, String copyId) 
	{
		this.id = id;
		this.subscriberId = subscriberId;
		this.borrowDate = borrowDate;
		this.expectedReturnDate = expectedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.isReturnedLate = isReturnedLate;
		this.bookCatalogNumber = bookCatalogNumber;
		this.copyId = copyId;
	}
	
	public BorrowACopyOfBook(String subscriberId, String borrowDate, String expectedReturnDate,
							String actualReturnDate, String isReturnedLate, String bookCatalogNumber) 
	{
		this.subscriberId = subscriberId;
		this.borrowDate = borrowDate;
		this.expectedReturnDate = expectedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.isReturnedLate = isReturnedLate;
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	public BorrowACopyOfBook(String subscriberId, String bookCatalogNumber, String copyId) 
	{
		this.subscriberId = subscriberId;
		this.bookCatalogNumber = bookCatalogNumber;
		this.copyId = copyId;
	}
	
	public BorrowACopyOfBook(String subscriberId, String expectedReturnDate,
							String bookCatalogNumber,String copyId) 
	{
		this.subscriberId = subscriberId;
		this.expectedReturnDate = expectedReturnDate;
		this.bookCatalogNumber = bookCatalogNumber;
		this.copyId = copyId;
	}
	
	public BorrowACopyOfBook(String actualReturnDate, String bookCatalogNumber, String copyId, boolean flag) 
	{
		this.actualReturnDate = actualReturnDate;
		this.bookCatalogNumber = bookCatalogNumber;
		this.copyId = copyId;
		this.flag = flag;
	}
	
	public BorrowACopyOfBook(String id, String subscriberId, String borrowDate, String expectedReturnDate,
			String actualReturnDate, String isReturnedLate, String bookCatalogNumber) 
	{
		this.id = id;
		this.subscriberId = subscriberId;
		this.borrowDate = borrowDate;
		this.expectedReturnDate = expectedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.isReturnedLate = isReturnedLate;
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	public BorrowACopyOfBook(String id) 
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

	public String getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) 
	{
		this.subscriberId = subscriberId;
	}

	public String getBorrowDate() 
	{
		return borrowDate;
	}

	public void setBorrowDate(String borrowDate) 
	{
		this.borrowDate = borrowDate;
	}

	public String getExpectedReturnDate() 
	{
		return expectedReturnDate;
	}

	public void setExpectedReturnDate(String expectedReturnDate) 
	{
		this.expectedReturnDate = expectedReturnDate;
	}

	public String getActualReturnDate() 
	{
		return actualReturnDate;
	}

	public void setActualReturnDate(String actualReturnDate) 
	{
		this.actualReturnDate = actualReturnDate;
	}

	public String getIsReturnedLate() 
	{
		return isReturnedLate;
	}

	public void setIsReturnedLate(String isReturnedLate) 
	{
		this.isReturnedLate = isReturnedLate;
	}

	public String getBookCatalogNumber() 
	{
		return bookCatalogNumber;
	}
	
	public void setBookCatalogNumber(String bookCatalogNumber) 
	{
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	public String getCopyId() 
	{
		return copyId;
	}
	
	public void setCopyId(String copyId) 
	{
		this.copyId = copyId;
	}
	public boolean isFlag() 
	{
		return flag;
	}
	
	public void setFlag(boolean flag) 
	{
		this.flag = flag;
	}
}
