package gui;

import entities.Book;
import entities.BookOrder;
import entities.BorrowACopyOfBook;
import entities.CopyOfBook;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.mail.imap.CopyUID;

import client.IClientUI;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BookInformationController implements IClientUI
{
	private User userLoggedIn;

	private Subscriber subscriberLoggedIn;

	@FXML
	private JFXTextArea bookNameTextArea;

	@FXML
	private Label wantedBookLabel;

	@FXML
	private TextArea descreptionPane;

	@FXML
	private ImageView wantedLogo;

	@FXML
	private JFXButton editDetailsBtn;

	@FXML
	private JFXButton deleteBookBtn;

	@FXML
	private JFXButton orderBookBtn;

	@FXML
	private JFXTextArea authorTextArea;

	@FXML
	private JFXTextArea categoriesTextArea;

	@FXML
	private JFXTextField catNumTextField;

	@FXML
	private JFXTextField publicationYearTextField;

	@FXML
	private JFXTextField editionNumTextField;

	@FXML
	private JFXTextField locationTextField;

	@FXML
	private JFXTextField returnDateTextField;

	private Book bookToShow;

	@FXML
	private TitledPane copiesTitlePane;

	@FXML
	private TextArea copiesTextArea;

	@FXML
	private Label availableLabel;

	@FXML
	private Label returnDateLabel;

	@FXML
	private Label locationLabel;

	@FXML
	private JFXCheckBox wantedBookCheckBox;

	@FXML
	private JFXButton viewTOC_btn;

	@FXML
	private Pane onEditShowPane;

	@FXML
	private ComboBox<String> copiesComboBox;
	
	@FXML
	private Spinner<Integer> copiesSpinner;
	
	protected static final String INITAL_VALUE = "0";

    @FXML
    private JFXButton remove_btn;

	private List<String> copiesFromComboBox = new ArrayList<String>();
	
	private byte[] uploadedFileByteArray = null;

    @FXML
    private JFXButton uploadFileBtn;
    
    @FXML
    private JFXButton cancelOrderBookBtn;


	public void setBookInformation(Book book)
	{
		onEditShowPane.setVisible(false);
		bookToShow = book;
		descreptionPane.setText(book.getDescription());
		bookNameTextArea.setText(book.getName());
		String authors = book.getAuthorNameList().toString().replace("[", "").replace("]", "");
		authorTextArea.setText(authors);
		String categories = book.getCategories().toString().replace("[", "").replace("]", "");
		categoriesTextArea.setText(categories);
		GuiManager.preventLettersTypeInTextField(editionNumTextField);
		GuiManager.limitTextFieldMaxCharacters(editionNumTextField, 11);
		GuiManager.preventLettersTypeInTextField(publicationYearTextField);
		GuiManager.limitTextFieldMaxCharacters(publicationYearTextField, 4);
		GuiManager.limitTextFieldMaxCharacters(locationTextField, 6);
		catNumTextField.setText(book.getCatalogNumber());
		cancelOrderBookBtn.setVisible(false);
		availableLabel.setText("The book available for borrow");
		availableLabel.setTextFill(Color.web("#12d318"));
		if (book.getClassification().equals("wanted"))
		{
			wantedBookLabel.setVisible(true);
			wantedLogo.setVisible(true);
		} else
		{
			wantedBookLabel.setVisible(false);
			wantedLogo.setVisible(false);
		}
		//setOrderButton(book);
		if (book.getCurrentNumOfBorrows() < book.getMaxCopies()) // book is available for borrow
		{
			
			isSubscriberLoggedIn(book);
			orderBookBtn.setDisable(true);
			returnDateLabel.setVisible(false);
			returnDateTextField.setVisible(false);
			uploadFileBtn.setVisible(false);
			viewTOC_btn.setVisible(true);

		} 
		else if (book.getCurrentNumOfBorrows() == book.getMaxCopies()) // book is not available for borrow
		{
			availableLabel.setText("Not available for borrow"); // means that the book is available for order
			availableLabel.setTextFill(Color.RED);
			if(userLoggedIn.getType().equals("subscriber"))
			{
				locationLabel.setVisible(false);
				locationTextField.setVisible(false);
			}
			if(!userLoggedIn.getType().equals("subscriber"))
			{
				returnDateLabel.setVisible(false);
				returnDateTextField.setVisible(false);
			}
			if (book.getCurrentNumOfOrders() == book.getMaxCopies()) // if orders queue is full
			{
				if (subscriberLoggedIn != null)
				{
					availableLabel.setText("Orders queue is full");
				} else
				{
					availableLabel.setText("Not available for order");
				}
				availableLabel.setTextFill(Color.RED);
				orderBookBtn.setDisable(true);
			}
			isSubscriberLoggedIn(book);
			
			// check what is the closest expected return date
			String closestReturnDate = book.getBorrows().get(1).getExpectedReturnDate();
			for (BorrowACopyOfBook borrow : book.getBorrows())
			{
				// if the current expected return date is before the previous date
				if (LocalDate.parse(borrow.getExpectedReturnDate()).isBefore((LocalDate.parse(closestReturnDate))))
				{
					closestReturnDate = borrow.getExpectedReturnDate();
				}
			}
			returnDateTextField.setText(closestReturnDate);
		}
		if (subscriberLoggedIn != null && !subscriberLoggedIn.getStatus().equals("active"))
		{
			availableLabel.setText("Card status is not active");
			availableLabel.setTextFill(Color.RED);
			orderBookBtn.setDisable(true);
		}
		publicationYearTextField.setText(book.getPublicationYear());
		editionNumTextField.setText(book.getEditionNumber());
		locationTextField.setText(book.getLocation());
		if (book.getCopies() != null)
		{
			String copies = "";
			for (CopyOfBook copy : book.getCopies())
			{
				copies = copies + System.lineSeparator() + copy.getId() + "              " + copy.getStatus();
			}
			copiesTextArea.setText("The book's copies are:\n" + "Copy ID   " + "Status" + copies);
		}
		copiesFromComboBox.clear();
		copiesFromComboBox.add("copy ID");
		for (CopyOfBook copy : book.getCopies())
		{
			copiesFromComboBox.add(copy.getId());
		}
	}

	@FXML
	void btn_orderBookClick(ActionEvent event)
	{
		BookOrder newOrder = new BookOrder(userLoggedIn.getId(), catNumTextField.getText());

		GuiManager.client.createNewOrder(newOrder);
	}
	
	@FXML
	void btn_cancelOrderClick(ActionEvent event)
	{
		BookOrder orderToCancel = new BookOrder(userLoggedIn.getId(), catNumTextField.getText());

		GuiManager.client.cancelOrder(orderToCancel);
	}
	
	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case CreateNewOrder:
		{
			BookOrder order = (BookOrder) msg.Data;
			if (order.getSubscriberId().equals("0"))
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Yoy have an active order of this book. Please close book page and refresh the books table at search book page.");
				});
			} 
			else
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Order executed Successfully!");
					orderBookBtn.setDisable(true);
				});
			}
			break;
		}
		case CancelOrder:
		{
			BookOrder order = (BookOrder) msg.Data;
			if (order.getSubscriberId().equals("0"))
			{
				Platform.runLater(() -> {
					GuiManager.ShowMessagePopup("Yoy don't have an active order of this book. Please close book page and refresh the books table at search book page.");
				});
			} 
			else
			{
				Platform.runLater(() -> {
					cancelOrderBookBtn.setDisable(true);
					GuiManager.ShowMessagePopup("Order canceled Successfully!");
				});
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		this.userLoggedIn = userLoged;
		switchWindowPermission();
	}

	@Override
	public User getUserLogedIn()
	{
		return userLoggedIn;
	}

	private void switchWindowPermission()
	{
		if (userLoggedIn == null)
		{
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
			uploadFileBtn.setVisible(false);
		}
		switch (userLoggedIn.getType())
		{
		case "subscriber":
			orderBookBtn.setVisible(true);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
			copiesTitlePane.setVisible(false);
			uploadFileBtn.setVisible(false);
			break;
		case "library manager":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(true);
			editDetailsBtn.setVisible(true);
			copiesTitlePane.setVisible(true);
			uploadFileBtn.setVisible(false);
			break;
		case "librarian":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(true);
			editDetailsBtn.setVisible(true);
			copiesTitlePane.setVisible(true);
			uploadFileBtn.setVisible(false);
			break;
		case "guest":
			orderBookBtn.setVisible(false);
			deleteBookBtn.setVisible(false);
			editDetailsBtn.setVisible(false);
			copiesTitlePane.setVisible(false);
			uploadFileBtn.setVisible(false);
			break;
		}

	}

	public void setSubscriber(Subscriber subscriberLogged)
	{
		subscriberLoggedIn = subscriberLogged;
		if (!subscriberLoggedIn.getStatus().equals("active"))
		{
			orderBookBtn.setDisable(true);
		} else
			orderBookBtn.setDisable(false);
	}

	@FXML
	void moveToArchiveClick(ActionEvent event)
	{
		String bookID = catNumTextField.getText();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Warning");
		alert.setHeaderText("Are you sure you want to delete this book?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK)
		{
			GuiManager.client.moveBookToArchive(bookID);
			GuiManager.ShowMessagePopup("The book with catalog number :" + bookID + "  moved to the archive");

		} else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();
		}
		Stage stage = (Stage) deleteBookBtn.getScene().getWindow();// we want to close the stage where the delete button
																	// is
		// do what you have to do
		stage.close();
	}

	@FXML
	void viewTableOfContentClick(ActionEvent event)
	{
		GuiManager.client.viewTableOfContent(bookToShow);
	}

	@FXML
	void editDetailsClick(ActionEvent event)
	{
		bookNameTextArea.setEditable(true);
		authorTextArea.setEditable(true);
		categoriesTextArea.setEditable(true);
		publicationYearTextField.setEditable(true);
		editionNumTextField.setEditable(true);
		locationTextField.setEditable(true);
		descreptionPane.setEditable(true);
		wantedBookLabel.setVisible(false);
		wantedLogo.setVisible(false);
		onEditShowPane.setVisible(true);
		editDetailsBtn.setDisable(true);
		uploadFileBtn.setVisible(true);
		viewTOC_btn.setVisible(false);
		if (bookToShow.getClassification().equals("wanted"))
		{
			wantedBookCheckBox.setSelected(true);
		}
		copiesComboBox.getItems().clear();
		copiesComboBox.setValue("copy ID");
		copiesComboBox.getItems().addAll(copiesFromComboBox);
		copiesSpinner.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, Integer.parseInt(INITAL_VALUE)));
		copiesSpinner.setEditable(false);
	}

	@FXML
	void saveChangesClick(ActionEvent event) // need to add function to remove '
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Warning");
		alert.setHeaderText("Are you sure you want to change those detailes?");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK)
		{
			if (categoriesTextArea.getText().isEmpty() || authorTextArea.getText().isEmpty())
			{
				GuiManager.ShowErrorPopup("Please fill all fields!");
				return;
			}
			ArrayList<String> authorsList = new ArrayList<String>(Arrays.asList(authorTextArea.getText().split(",")));
			ArrayList<String> categoriesList = new ArrayList<String>(
					Arrays.asList(categoriesTextArea.getText().split(",")));
			String bookName = removeTag(bookNameTextArea.getText());
			String publicationYear = removeTag(publicationYearTextField.getText());
			String location = removeTag(locationTextField.getText());
			String description = removeTag(descreptionPane.getText());
			String bookClassification;
			if (wantedBookCheckBox.isSelected())
				bookClassification = "wanted";
			else
				bookClassification = "ordinary";

			if (bookName.isEmpty() || location.isEmpty() || publicationYear.isEmpty())
			{
				GuiManager.ShowErrorPopup("Please fill all fields!");
				return;
			}
			try
			{
				int year = Integer.parseInt(publicationYear);
				if (year > GuiManager.currentYear || year < 1902)
				{
					GuiManager.ShowErrorPopup("Please enter a valid year only.");
					return;
				}
			} catch (Exception e)
			{
				GuiManager.ShowErrorPopup("Please enter a valid year!");
			}
			Book newBook = new Book(catNumTextField.getText(), bookName, authorsList, categoriesList, publicationYear,
					editionNumTextField.getText(), location, description, bookClassification);
			int maxCopies=copiesSpinner.getValue(); //taking the copies that added by the spinner
			newBook.setMaxCopies(maxCopies); 
			// copies
			//newBook.setCopies(bookToShow.getCopies());
			newBook.setCopies(new ArrayList<>());
			for(CopyOfBook copy:bookToShow.getCopies())
			{
				if ((copiesFromComboBox.contains(copy.getId())))
				{
					newBook.getCopies().add(copy);
				}
			}
			
			/*List<CopyOfBook> copiesArray = newBook.getCopies();
			int sizeOfOriginalCopies = copiesArray.size();
			copiesFromComboBox.remove("copy ID");
			for (int i =0 ; i < sizeOfOriginalCopies ; i++)
			{
				if (!(copiesFromComboBox.contains(copiesArray.get(i).getId())))
				{
					newBook.getCopies().remove(copiesArray.get(i));
				}
			}*/
			newBook.setTocArraybyte(uploadedFileByteArray);
			
			
			GuiManager.client.editBookDetails(newBook);
			editDetailsBtn.setDisable(false);
			bookNameTextArea.setEditable(false);
			authorTextArea.setEditable(false);
			categoriesTextArea.setEditable(false);
			publicationYearTextField.setEditable(false);
			editionNumTextField.setEditable(false);
			locationTextField.setEditable(false);
			descreptionPane.setEditable(false);
			copiesTextArea.setEditable(false);
			wantedBookCheckBox.setVisible(true);
			onEditShowPane.setVisible(false);

		} else if (option.get() == ButtonType.CANCEL)
		{
			alert.close();
		}
	}

	@FXML
	void cancelClick(ActionEvent event)
	{
		onEditShowPane.setVisible(false);
		editDetailsBtn.setDisable(false);
		uploadFileBtn.setVisible(false);
		setBookInformation(bookToShow);
	}

	/**
	 * this function get a string and remove all tag's that can break the program
	 */
	public static String removeTag(String str)
	{
		String s = str.replace("'", " ");
		return s;
	}

    @FXML
    void removeCopiesClick(ActionEvent event) 
    {
    	if(copiesComboBox.getSelectionModel().getSelectedItem().equals("copy ID"))
    	{
    		GuiManager.ShowErrorPopup("Please choose a copy");
    		return;
    	}
    	int size= copiesComboBox.getItems().size();
    	if(size==2)
    	{
    		GuiManager.ShowErrorPopup("Cannot delete last copy.\nPlease add copies instead or move book to archive." );
    		copiesComboBox.getSelectionModel().selectFirst();
    		return;
    	}
 
    	String copyID = copiesComboBox.getSelectionModel().getSelectedItem();
    	ArrayList<CopyOfBook> copyToCheck= bookToShow.getCopies();
    	for(CopyOfBook copy: copyToCheck )
    	{
    		String id=copy.getId();
    		if(id.equals(copyID))
    		{
    			String status=copy.getStatus();
    			if(status.equals("unavailable"))
    			{
    				GuiManager.ShowErrorPopup("This copy is currently borrowed");
    				copiesComboBox.getSelectionModel().selectFirst();
    			}
    			
    			else
    			{
    				copiesComboBox.getItems().remove(copiesComboBox.getSelectionModel().getSelectedItem());
    				copiesFromComboBox.remove(copy.getId());
    				copiesComboBox.getSelectionModel().clearSelection();
    				copiesComboBox.getSelectionModel().selectFirst();
    			}
    		}
    	}
    }
    public Stage getStage()
    {
    	return (Stage) editDetailsBtn.getScene().getWindow();
    }
    
	@FXML
	void btnUploadTableOfcontentClick(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog((Stage) editDetailsBtn.getScene().getWindow());
		// file.getName();
		if (file != null)
		{
			try
			{
				if (!getFileExtension(file.getName()).equals("pdf"))
					GuiManager.ShowErrorPopup("Error - file must be a pdf file");
				else
				{
					uploadedFileByteArray = Files.readAllBytes(file.toPath());
				}
			} catch (IOException e)
			{

				uploadedFileByteArray = null;
			}
		}

	}
	
	public static String getFileExtension(String fullName)
	{
		if (fullName == null)
			return null;
		String fileName = new File(fullName).getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
	
	private void isSubscriberLoggedIn(Book book)
	{
		boolean isOrderExist = false;
		if (subscriberLoggedIn != null)
		{
			/* if the subscriber is currently borrow one of the copies of this book, than we
			 * need to prevent the option to order this book */
			for (BorrowACopyOfBook borrow : book.getBorrows())
			{
				if (borrow.getSubscriberId().equals(subscriberLoggedIn.getId()))
				{
					availableLabel.setText("You cant order a book that\nyou are currently borrowing");
					availableLabel.setTextFill(Color.RED);
					orderBookBtn.setDisable(true);
				}
			}
			
			/* if the subscriber is currently order the book, and the book arrived to the library, (available for borrow) 
			 * than we need to show the location in orders queue, and able the cancel order option*/
			int i = 1, positionInQueue = 0;
			for (BookOrder order : book.getOrders())
			{
				// check if the subscriber already ordered this book
				if (order.getSubscriberId().equals(subscriberLoggedIn.getId()))
				{
					isOrderExist = true;
					positionInQueue = i;
				}
				i++;
			}
			if (isOrderExist)
			{
				availableLabel.setText("Your position in orders queue: " + positionInQueue);
				availableLabel.setTextFill(Color.web("#12d318"));
				orderBookBtn.setVisible(false);
				cancelOrderBookBtn.setVisible(true);
			}
		}
	}
}

