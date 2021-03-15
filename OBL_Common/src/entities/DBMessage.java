package entities;
import java.io.Serializable;

/**
 * This class is the message tranfering from client to server and the other way.
 * @author eyalv
 *
 */
public class DBMessage implements Serializable
{
	public DBAction Action;
	public Object Data;
	/**
	 * The constructor of the message
	 * @param action - enum of actions - how to classify the operation.
	 * @param data - an object of data, if no data is needed - send null.
	 */
	public DBMessage(DBAction action,Object data)
	{
		this.Action = action;
		this.Data = data;
	}
	
	public enum DBAction
	{
		UpdateUserLogout,CheckUser,isDBRuning,CreateSubscriber,
		GetAllBooksList,CreateNewBorrow, ViewSubscriberCard,UpdateSubscriberCard,
		ShutDown, ReturnBook, GetEmployeeList,GetCurrentBorrowsForSubID,GetCurrentBorrows, 
		CreateNewOrder,GetActivityLog,Reports_getAvarageBorrows,ViewTableOfContent,MoveBookToArchive
		,Reports_Activity,Reports_getList,Reports_Add,Reports_LateReturns, AddBook, 
		EditBookDetails, BorrowExtension, CancelOrder; 
	}
}
