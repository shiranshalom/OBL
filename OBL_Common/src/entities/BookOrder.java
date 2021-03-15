package entities;

import java.io.Serializable;

public class BookOrder implements Serializable
{
	private String id;
	private String subscriberId;
	private String orderDate;
	private String status;
	private String bookArriveDate;
	private String bookCatalogNumber;
	private boolean flag;
	
	public enum orderQueueCheckOptions
	{
		OrdersQueueIsEmpty, OrdersQueueIsNotEmpty, SubscriberIsFirstInOrdersQueue, SubscriberIsNotFirstInOrdersQueue, Error;
	}
	
	public BookOrder() {}
	
	public BookOrder(String id, String subscriberId, String orderDate, String status, String bookArriveDate,
			String bookCatalogNumber) 
	{
		this.id = id;
		this.subscriberId = subscriberId;
		this.orderDate = orderDate;
		this.status = status;
		this.bookArriveDate = bookArriveDate;
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	public BookOrder(String subscriberId, String bookCatalogNumber)
	{
		this.subscriberId = subscriberId;
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	public BookOrder(String subscriberId) 
	{
		this.subscriberId = subscriberId;
	}
	
	public BookOrder(String bookCatalogNumber, boolean flag) 
	{
		this.bookCatalogNumber = bookCatalogNumber;
		this.flag = flag;
	}
	
	public BookOrder(String subscriberId, String status, boolean flag) 
	{
		this.subscriberId = subscriberId;
		this.status = status;
		this.flag = flag;
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

	public String getOrderDate() 
	{
		return orderDate;
	}

	public void setOrderDate(String orderDate) 
	{
		this.orderDate = orderDate;
	}

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getBookArriveDate() 
	{
		return bookArriveDate;
	}

	public void setBookArriveDate(String bookArriveDate) 
	{
		this.bookArriveDate = bookArriveDate;
	}

	public String getBookCatalogNumber() 
	{
		return bookCatalogNumber;
	}

	public void setBookCatalogNumber(String bookCatalogNumber) 
	{
		this.bookCatalogNumber = bookCatalogNumber;
	}
	
	
}
