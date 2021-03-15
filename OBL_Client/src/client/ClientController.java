package client;

import ocsf.client.*;

import java.awt.Desktop;
import java.io.*;

import entities.Book;
import entities.BookOrder;
import entities.BorrowACopyOfBook;
import entities.DBMessage;
import entities.User;
import entities.BorrowExtension;
import entities.DBMessage.DBAction;
import entities.Report_Activity;
import entities.Subscriber;
import gui.GuiManager;
import javafx.application.Platform;

/**
 * This is the Controller between the server and the client. Every request from client UI to server will be sent through this. And backwords.
 * This class is a singletone class. You can only create one instance of this
 * class using - "createClientIfNotExist".
 */
public class ClientController extends AbstractClient
{

	final public static int DEFAULT_PORT = 5555;
	private static ClientController singletoneInstance = null;

	/**
	 * private Singletone constructor
	 */
	private ClientController(String host, int port) throws IOException
	{
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	/**
	 * Call this instead of the constructor
	 * 
	 * @param host the host ip
	 * @param port the port to connect
	 */
	public static ClientController createClientIfNotExist(String host, int port) throws IOException
	{
		if (singletoneInstance == null)
			singletoneInstance = new ClientController(host, port);
		return singletoneInstance;
	}

	@Override
	public void handleMessageFromServer(Object msg)
	{
		if (msg == null)
			GuiManager.ShowErrorPopup("Something went wrong.\nPlease restart the program");
		if (!(msg instanceof DBMessage))
			return;// check if the message is DBMessage and not something else
		DBMessage message = (DBMessage) msg;
		DBAction action = message.Action;
		switch (action)
		{
		case isDBRuning:
			GuiManager.dbConnected = (boolean) message.Data;
			break;
		case ShutDown:
			Platform.runLater(() -> {
				GuiManager.ShowErrorPopup("The server was unexpectedly shut down.\n" + "Please restart your progrem.\n"
						+ "Everything you do now will not be saved.");
			});
			break;
		case ViewTableOfContent:
			openTableOfContentPDF(message);
			break;
		default:
			GuiManager.CurrentGuiController.getMessageFromServer(message);
			break;

		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		} catch (IOException e)
		{
		}

		System.exit(0);
	}

	public void CheckValidUser(User user)
	{
		DBMessage message = new DBMessage(DBAction.CheckUser, user);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void updateUserLogOut(User user)
	{
		DBMessage message = new DBMessage(DBAction.UpdateUserLogout, user);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void CreateSubscriber(Subscriber subscriber)
	{
		DBMessage message = new DBMessage(DBAction.CreateSubscriber, subscriber);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void createNewBorrow(BorrowACopyOfBook borrow)
	{
		DBMessage message = new DBMessage(DBAction.CreateNewBorrow, borrow);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void getAllBooks()
	{
		DBMessage message = new DBMessage(DBAction.GetAllBooksList, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getSubscriberFromDB(String id)
	{
		DBMessage message = new DBMessage(DBAction.ViewSubscriberCard, id);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void updateSubscriberDetails(Subscriber subscriberToUpdate)
	{
		DBMessage message = new DBMessage(DBAction.UpdateSubscriberCard, subscriberToUpdate);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void returnBook(BorrowACopyOfBook borrowToClose)
	{
		DBMessage message = new DBMessage(DBAction.ReturnBook, borrowToClose);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getEmployeeList()
	{
		DBMessage message = new DBMessage(DBAction.GetEmployeeList, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void createNewOrder(BookOrder newOrder)
	{
		DBMessage message = new DBMessage(DBAction.CreateNewOrder, newOrder);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void getCurrentBorrowsForSubscriberID(String id)
	{
		DBMessage message = new DBMessage(DBAction.GetCurrentBorrowsForSubID, id);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getAllCurrentBorrows()
	{
		DBMessage message = new DBMessage(DBAction.GetCurrentBorrows, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getActivityLogFromDB(String id)
	{
		DBMessage message = new DBMessage(DBAction.GetActivityLog, id);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void report_getBorrowDurationInfo()
	{
		DBMessage message = new DBMessage(DBAction.Reports_getAvarageBorrows, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void viewTableOfContent(Book bookCatalogNumber)
	{
		DBMessage message = new DBMessage(DBAction.ViewTableOfContent, bookCatalogNumber);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void moveBookToArchive(String catalogNumber)
	{
		DBMessage message = new DBMessage(DBAction.MoveBookToArchive, catalogNumber);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * in this function we get byte array from the server and we open it as pdf file
	 */
	private void openTableOfContentPDF(DBMessage message)
	{
		if(message.Data == null)
		{
			GuiManager.ShowErrorPopup("PDF does not exist.");
			return;
		}
		byte[] myByteArray = (byte[])message.Data;
		if(Desktop.isDesktopSupported()) 
		{
			try
			{
				File outputFile = new File("tableOfContentTempFile.pdf");
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(myByteArray);
				Desktop.getDesktop().open(outputFile);
				fos.close();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void report_ActivityInfo()
	{
		DBMessage message = new DBMessage(DBAction.Reports_Activity, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getReportsList()
	{
		DBMessage message = new DBMessage(DBAction.Reports_getList, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void addReport(Report_Activity reportData)
	{
		DBMessage message = new DBMessage(DBAction.Reports_Add, reportData);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void report_getLateReturnsInfo()
	{
		DBMessage message = new DBMessage(DBAction.Reports_LateReturns, null);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}
	
	public void AddNewBook(Book book)
	{
		DBMessage message = new DBMessage(DBAction.AddBook, book);
		try
		{
			sendToServer(message);  
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void editBookDetails (Book book)
	{

		DBMessage message = new DBMessage(DBAction.EditBookDetails, book);
		try
		{
			sendToServer(message);
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public void borrowExtension(BorrowExtension borrowToExtend)
	{
		DBMessage message = new DBMessage(DBAction.BorrowExtension, borrowToExtend);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void cancelOrder(BookOrder newOrder)
	{
		DBMessage message = new DBMessage(DBAction.CancelOrder, newOrder);
		try
		{
			sendToServer(message);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
//End of ClientController class
