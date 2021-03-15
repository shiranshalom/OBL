package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Book implements Serializable
{
	private String catalogNumber;
	private String name;
	private String purchaseDate;
	private String classification;
	private String description;
	private String location;

	private String editionNumber;
	private String publicationYear;
	private String tableOfContenPath;
	private String isArchived;
	private ArrayList<CopyOfBook> copies;
	private ArrayList<String> authorNameList;
	private ArrayList<String> categories;
	private ArrayList<BorrowACopyOfBook> borrows;
	private ArrayBlockingQueue<BookOrder> orders;
	
	private byte[] tocArraybyte;

	private int maxCopies;
	private int currentNumOfBorrows;
	private int currentNumOfOrders;
	
	
	public Book(String catalogNumber, String name, String purchaseDate,
				String classification, String description, String location,
				String editionNumber,String publicationYear,
				String tableOfContenPath, String isArchived)
	{
		this.catalogNumber =catalogNumber;
		this.name = name;
		this.purchaseDate= purchaseDate;
		this.classification = classification;
		this.description = description;
		this.location = location;
		this.editionNumber = editionNumber;
		this.publicationYear = publicationYear;
		this.tableOfContenPath = tableOfContenPath;
		this.isArchived = isArchived;
	}
	
	public Book( String name,String purchaseDate, ArrayList<String> authorNameList,
			ArrayList<String> categories, String publicationYear, String editionNumber ,
			String location,String description,
			int maxCopies,  String classification,byte[] tocArraybyte)       ////tal
{
	
	this.name = name;
	this.purchaseDate=purchaseDate;
	this.classification = classification;
	this.description = description;
	this.location = location;
	this.editionNumber = editionNumber;
	this.publicationYear = publicationYear;
	this.tocArraybyte = tocArraybyte;
	this.authorNameList= authorNameList;
	this.categories = categories;
	this.maxCopies = maxCopies;
	
}
	
	
	public Book(String catalogNumber, String name, String purchaseDate, String classification,
				String description, int maxCopies, int currentNumOfBorrows, int currentNumOfOrders,
				String tableOfContenPath, String isArchived, ArrayList<CopyOfBook> copies) 
	{
		this.catalogNumber = catalogNumber;
		this.name = name;
		this.purchaseDate = purchaseDate;
		this.classification = classification;
		this.description = description;
		this.maxCopies = maxCopies;
		this.currentNumOfBorrows = currentNumOfBorrows;
		this.currentNumOfOrders = currentNumOfOrders;
		this.tableOfContenPath = tableOfContenPath;
		this.isArchived = isArchived;
		this.copies = copies;
	}
	
	public Book(String catalogNumber) 
	{
		this.catalogNumber = catalogNumber;
	}
	//shiran
	public Book(String catalogNumber, String name,ArrayList<String> authorNameList,ArrayList<String> categories, 
			String publicationYear, String editionNumber,String location, String description,String classification)
	{
		this.catalogNumber=catalogNumber;
		this.name=name;
		this.authorNameList=authorNameList;
		this.categories=categories;
		this.publicationYear=publicationYear;
		this.editionNumber=editionNumber;
		this.location=location;
		this.description=description;
		this.classification = classification;
		
	}
	public String getCatalogNumber() 
	{
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) 
	{
		this.catalogNumber = catalogNumber;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getPurchaseDate() 
	{
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) 
	{
		this.purchaseDate = purchaseDate;
	}
	public String getClassification() 
	{
		return classification;
	}
	public void setClassification(String classification) 
	{
		this.classification = classification;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public int getMaxCopies() 
	{
		return maxCopies;
	}
	public void setMaxCopies(int maxCopies) 
	{
		this.maxCopies = maxCopies;
	}
	public int getCurrentNumOfBorrows() 
	{
		return currentNumOfBorrows;
	}
	public void setCurrentNumOfBorrows(int currentNumOfBorrows) 
	{
		this.currentNumOfBorrows = currentNumOfBorrows;
	}
	public int getCurrentNumOfOrders() 
	{
		return currentNumOfOrders;
	}
	public void setCurrentNumOfOrders(int currentNumOfOrders) 
	{
		this.currentNumOfOrders = currentNumOfOrders;
	}
	public String getEditionNumber() 
	{
		return editionNumber;
	}
	public void setEditionNumber(String editionNumber) 
	{
		this.editionNumber = editionNumber;
	}
	public String getPublicationYear() 
	{
		return publicationYear;
	}
	public void setPublicationYear(String publicationYear) 
	{
		this.publicationYear = publicationYear;
	}
	public String getTableOfContenPath() 
	{
		return tableOfContenPath;
	}
	public void setTableOfContenPath(String tableOfContenPath) 
	{
		this.tableOfContenPath = tableOfContenPath;
	}
	public String getIsArchived() 
	{
		return isArchived;
	}
	public void setIsArchived(String isArchived) 
	{
		this.isArchived = isArchived;
	}

	public ArrayList<CopyOfBook> getCopies() 
	{
		return copies;
	}

	public void setCopies(ArrayList<CopyOfBook> copies) 
	{
		this.copies = copies;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public ArrayList<String> getAuthorNameList()
	{
		return authorNameList;
	}

	public void setAuthorNameList(ArrayList<String> authorNameList)
	{
		this.authorNameList = authorNameList;
	}

	public ArrayList<String> getCategories()
	{
		return categories;
	}

	public void setCategories(ArrayList<String> categories)
	{
		this.categories = categories;
	}

	public ArrayBlockingQueue<BookOrder> getOrders() 
	{
		return orders;
	}

	public void setOrders(ArrayBlockingQueue<BookOrder> orders) 
	{
		this.orders = orders;
	}

	public ArrayList<BorrowACopyOfBook> getBorrows() 
	{
		return borrows;
	}

	public void setBorrows(ArrayList<BorrowACopyOfBook> borrows) 
	{
		this.borrows = borrows;
	}
	
	public byte[] getTocArraybyte()
	{
		return tocArraybyte;
	}

	public void setTocArraybyte(byte[] tocArraybyte)
	{
		this.tocArraybyte = tocArraybyte;
	}
	
}
