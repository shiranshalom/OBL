package application;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import entities.Book;
import entities.BookOrder;
import entities.BorrowACopyOfBook;
import entities.OblMessage;
import entities.Subscriber;
import entitiesQueries.BooksQueries;
import entitiesQueries.BorrowsQueries;
import entitiesQueries.OblMessagesQueries;
import entitiesQueries.OrdersQueries;
import entitiesQueries.SendEmail;
import entitiesQueries.SubscribersQueries;

/**
 * All of our automatic function will be here.
 * Make sure you call shutDown() when exiting the program unless you want a background executers.  
 *
 */

public class AutomaticExecutors 
{
	private static MySQLConnection oblDB; 
	ScheduledThreadPoolExecutor executor = null;
	
	public AutomaticExecutors(MySQLConnection oblDb)
	{	
		if(oblDb == null)
			return;
		AutomaticExecutors.oblDB = oblDb;
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(() -> checkAndUpdateLateReturns(), 0, 15, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(() -> reminderBeforeReturnDate(), 0, 15, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(() -> ordersFulfillmentCheck(), 0, 15, TimeUnit.MINUTES);
	}
	public void shutDown()
	{
		executor.shutdown();
	}
	/**checkAndUpdateLateReturns:
	 * This method suppose to run automatically every 24 hours. If subscriber is
	 * late at return a copy of a book, his status will change to "frozen", and the
	 * borrow "isReturnedLate" flag will change to "yes".
	 * If it's the third late of the same subscriber - than message with the subscriber
	 * details is send to the library manager in order to change subscriber card
	 * status to "deep freeze" */	
	static void checkAndUpdateLateReturns()
	{
		String query = BorrowsQueries.getBorrowsTableForCheckAndUpdateLateReturns();
		ResultSet rsCurrentBorrowsTable = oblDB.executeQuery(query); // get current borrows table
		BorrowACopyOfBook borrowFromBorrowsTable = new BorrowACopyOfBook();
		ArrayList<Subscriber> subscribersLateReturnsThreeTimes = new ArrayList<Subscriber>();
		OblMessage message;
		String messageContent;
		String fullName = "";
		try
		{
			while (rsCurrentBorrowsTable.next())
			{
				borrowFromBorrowsTable.setId(rsCurrentBorrowsTable.getString(1));
				borrowFromBorrowsTable.setSubscriberId(rsCurrentBorrowsTable.getString(2));
				borrowFromBorrowsTable.setExpectedReturnDate(rsCurrentBorrowsTable.getString(4));
				borrowFromBorrowsTable.setActualReturnDate(rsCurrentBorrowsTable.getString(5));
				borrowFromBorrowsTable.setIsReturnedLate(rsCurrentBorrowsTable.getString(6));
				borrowFromBorrowsTable.setBookCatalogNumber(rsCurrentBorrowsTable.getString(7));

				String expectedReturnDate = borrowFromBorrowsTable.getExpectedReturnDate();
				String currentDate = getCurrentDateAsString();
				
				Subscriber subscriberToUpdate = new Subscriber(borrowFromBorrowsTable.getSubscriberId());
				fullName = getSubscriberFullName(subscriberToUpdate);
				int lateReturnsCount = getSubscriberNumOfLateReturns(subscriberToUpdate);
				if(lateReturnsCount % 3 == 0)
				{
					lateReturnsCount = 0;
				}
				else
				{
					lateReturnsCount = lateReturnsCount % 3;
				}
				boolean existInArrayList = false;
				// check if the subscriber is late at return
				if (LocalDate.parse(currentDate).isAfter(LocalDate.parse(expectedReturnDate)) 
						&& borrowFromBorrowsTable.getActualReturnDate() == null) 
				{
					query = SubscribersQueries.getSubscriberStatus(subscriberToUpdate);
					ResultSet rsSubscriberStatus = oblDB.executeQuery(query); // get subscriber's status
					try
					{
						rsSubscriberStatus.next();
						subscriberToUpdate.setStatus(rsSubscriberStatus.getString(1));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					query = BorrowsQueries.getIsReturnedLate(borrowFromBorrowsTable);
					// get the value of "isReturnedLate" flag
					ResultSet rsIsReturnedLate = oblDB.executeQuery(query); 
					try
					{
						rsIsReturnedLate.next();
						if (rsIsReturnedLate.getString(1).equals("no"))
						{
							lateReturnsCount++;
							borrowFromBorrowsTable.setIsReturnedLate("yes");
							query = BorrowsQueries.updateIsReturnedLateToYes(borrowFromBorrowsTable);
							// update the flag at the borrow if the subscriber returned the copy late
							oblDB.executeUpdate(query);
							/*send a message to the subscriber about a late at return*/
							messageContent = "Dear " + fullName + ", you are late at return of book number: " + borrowFromBorrowsTable.getBookCatalogNumber()
						  	   + ".\nPlease return the book to the library as soon as possible.\n"
						  	   + "Meanwhile, your subscriber card status has changed to frozen, until you will return the book.";
							message = new OblMessage(messageContent, "subscriber", subscriberToUpdate.getId());
							query = OblMessagesQueries.sendMessageToSubscriber(message);
							oblDB.executeUpdate(query); // add a new message to messages table			
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					if (lateReturnsCount % 3 == 0 && lateReturnsCount != 0 
							&& !subscriberToUpdate.getStatus().equals("deep freeze"))
					{
						for (Subscriber subscriberToCheck : subscribersLateReturnsThreeTimes)
						{
							if(subscriberToCheck.getId().equals(subscriberToUpdate.getId()))
								existInArrayList = true;
						}
						if(!existInArrayList)
							subscribersLateReturnsThreeTimes.add(subscriberToUpdate);
					}
					if (borrowFromBorrowsTable.getActualReturnDate() == null)
					{
						if (subscriberToUpdate.getStatus().equals("active"))
						{
							subscriberToUpdate.setStatus("frozen");
							query = SubscribersQueries.updateSubscriberStatus(subscriberToUpdate);
							oblDB.executeUpdate(query); // update subscriber's status to frozen			
						}	
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		/* Send message to the library manager in order to approve deep freeze of the
		 * subscriber card. the message will include the subscriber id and borrow id, 
		 * and it will send to the library manager only if the subscriber status is not already "deep freeze"*/
		
		/*send a message to the subscriber about 3 late at returns*/
		for (Subscriber subscriber : subscribersLateReturnsThreeTimes)
		{
			fullName = getSubscriberFullName(subscriber);
			messageContent = "The subscriber: " + subscriber.getId()
		  	   + " is late at return of 3 books.\n"
		  	   + "Please double click on this message in order to change the subscriber card status to deep freeze";
			message = new OblMessage(messageContent, "library manager");
			query = OblMessagesQueries.sendMessageToLibraryManager(message);
			oblDB.executeUpdate(query); // add a new message to messages table
			
			messageContent = "Dear " + fullName + ", you are late at return of 3 books."
		  	   + "\nPlease return the books to the library as soon as possible.\n"
		  	   + "Meanwhile, your subscriber card status has changed to deep freeze, until Disciplinary inquiry.";
			message = new OblMessage(messageContent, "subscriber", subscriber.getId());
			query = OblMessagesQueries.sendMessageToSubscriber(message);
			oblDB.executeUpdate(query); // add a new message to messages table	
		}
	}
	
	/**reminderBeforeReturnDate:
	 * This method suppose to run automatically every 24 hours.
	 * If the current date is one day before return date,
	 * than a remainder email send to the subscriber who borrowed the book*/	
	static void reminderBeforeReturnDate()
	{
		String query = BorrowsQueries.getCurrentBorrowsTable();
		ResultSet rsCurrentBorrowsTable = oblDB.executeQuery(query); // get current borrows table
		BorrowACopyOfBook borrowFromBorrowsTable = null;
		// in order to send mails to all of the subscribers with the book information
		ArrayList<BorrowACopyOfBook> subscribersToInform = new ArrayList<BorrowACopyOfBook>();

		ArrayList<Book> books = new ArrayList<Book>();
		Book book = null;
		try
		{
			while (rsCurrentBorrowsTable.next())
			{
				borrowFromBorrowsTable = new BorrowACopyOfBook();
				borrowFromBorrowsTable.setId(rsCurrentBorrowsTable.getString(1));
				borrowFromBorrowsTable.setSubscriberId(rsCurrentBorrowsTable.getString(2));
				borrowFromBorrowsTable.setExpectedReturnDate(rsCurrentBorrowsTable.getString(4));
				borrowFromBorrowsTable.setBookCatalogNumber(rsCurrentBorrowsTable.getString(7));
				borrowFromBorrowsTable.setCopyId(rsCurrentBorrowsTable.getString(8));
				String expectedReturnDate = borrowFromBorrowsTable.getExpectedReturnDate();
				// check if current date is one day before return date
				if (LocalDate.parse(getCurrentDateAsString()).plusDays(1).isEqual(LocalDate.parse(expectedReturnDate))) 
				{
					subscribersToInform.add(borrowFromBorrowsTable);
					book = new Book(borrowFromBorrowsTable.getBookCatalogNumber());
					books.add(book);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// add the name of the book to each book
		for (Book bookToUpdate : books)
		{
			query = BooksQueries.getBookName(bookToUpdate);
			ResultSet rsBookName = oblDB.executeQuery(query); // get book name
			try
			{
				rsBookName.next();
				bookToUpdate.setName(rsBookName.getString(1));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		int i = 0;
		// Send email to all of the subscribers one day before return date 
		for (BorrowACopyOfBook borrow: subscribersToInform)
		{
			Subscriber subscriberToInform = new Subscriber(borrow.getSubscriberId());
			String fullName = getSubscriberFullName(subscriberToInform);
			/* send a message to the subscriber */
			OblMessage message;
			String messageContent;
			messageContent = "Dear " + fullName + ",\n"
							+ "Return date of the book: \"" + books.get(i).getName() + "\" "
							+ "that you have borrowed is tomrrow.\n"
							+ "Please return the book until tomrrow, or excute borrow extension (if extension is possible).";
			message = new OblMessage(messageContent, "subscriber", borrow.getSubscriberId());
			query = OblMessagesQueries.sendMessageToSubscriber(message);
			oblDB.executeUpdate(query); // add a new message to messages table
			
			/* send an email to the subscriber */
			String emailSubject = "Reminder: One day before return day";
			String emailMessage = "Dear " + fullName + ",\n"
								+ "Return date of the book: \"" + books.get(i).getName() + "\" "
								+ "that you have borrowed is tomrrow.\n"
								+ "Please return the book until tomrrow, or excute borrow extension (if extension is possible).";
			try
			{
				SendEmail email = new SendEmail();
				email.sendEmail(subscriberToInform.getEmail(), emailSubject, emailMessage);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			}	
			i++;
		}
	}
	
	
	/**ordersFulfillmentCheck:
	 * This method suppose to run automatically every 24 hours.
	 * If subscriber ordered a book, and the book arrived to the library,
	 * the order will stay in active status for only two days.
	 * after two days: if the subscriber did  n't borrow the book,
	 * the order will be canceled.*/
	static void ordersFulfillmentCheck()
	{
		String query = OrdersQueries.getCurrentOrdersTable();
		ResultSet rsCurrentOrdersTable = oblDB.executeQuery(query); // get current borrows table
		BookOrder orderFromOrdersTable = null;
		try
		{
			while (rsCurrentOrdersTable.next())
			{
				orderFromOrdersTable = new BookOrder();
				orderFromOrdersTable.setId(rsCurrentOrdersTable.getString(1));
				orderFromOrdersTable.setSubscriberId(rsCurrentOrdersTable.getString(2));
				orderFromOrdersTable.setOrderDate(rsCurrentOrdersTable.getString(3));
				orderFromOrdersTable.setStatus(rsCurrentOrdersTable.getString(4));
				orderFromOrdersTable.setBookArriveDate(rsCurrentOrdersTable.getString(5));
				orderFromOrdersTable.setBookCatalogNumber(rsCurrentOrdersTable.getString(6));
				String bookArriveDate;
				// check if 
				if (orderFromOrdersTable.getBookArriveDate() != null) 
				{
					bookArriveDate = orderFromOrdersTable.getBookArriveDate().substring(0, 10);
					if (LocalDate.parse(getCurrentDateAsString())
							.isEqual(LocalDate.parse(bookArriveDate).plusDays(2))) 
					{
						orderFromOrdersTable.setStatus("canceled");
						query = OrdersQueries.updateOrderStatus(orderFromOrdersTable);
						oblDB.executeUpdate(query); // update order status to closed
						
						Subscriber subscriber = new Subscriber(orderFromOrdersTable.getSubscriberId());
						String fullName = getSubscriberFullName(subscriber);
						/* send a message to the subscriber */
						OblMessage message;
						String messageContent;
						messageContent = "Dear " + fullName + ",\n"
										+ "You did not fulfilled your order for book number: " + orderFromOrdersTable.getBookCatalogNumber()
										+ ".\nTherefore, order number: " + orderFromOrdersTable.getId() + " is canceled.";
						message = new OblMessage(messageContent, "subscriber", orderFromOrdersTable.getSubscriberId());
						query = OblMessagesQueries.sendMessageToSubscriber(message);
						oblDB.executeUpdate(query); // add a new message to messages table 
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}
	
	private static int getSubscriberNumOfLateReturns(Subscriber subscriberToUpdate)
	{
		String query = SubscribersQueries.getNumOfLateReturns(subscriberToUpdate);// search by subscriber ID
		ResultSet rsCurrentNumOfLateReturns = oblDB.executeQuery(query);
		try
		{
			rsCurrentNumOfLateReturns.next();
			return rsCurrentNumOfLateReturns.getInt(1);
		} catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	private static String getSubscriberFullName(Subscriber subscriber)
	{
		String fullName = null;
		String query = SubscribersQueries.getSubscriberFullInformationByID(subscriber.getId());
		ResultSet rsSubscriberDetails = oblDB.executeQuery(query); // get subscriber details
		try
		{
			rsSubscriberDetails.next();
			fullName = rsSubscriberDetails.getString(4).substring(0, 1).toUpperCase()
					+ rsSubscriberDetails.getString(4).substring(1) + " "
					+ rsSubscriberDetails.getString(5).substring(0, 1).toUpperCase()
					+ rsSubscriberDetails.getString(5).substring(1);
			subscriber.setEmail(rsSubscriberDetails.getString(7));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fullName;
	}
	
	public void setOblDb(MySQLConnection oblDb)
	{
		this.oblDB = oblDb;
	}

}
