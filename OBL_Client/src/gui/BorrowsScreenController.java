package gui;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import client.IClientUI;
import entities.Book;
import entities.BorrowACopyOfBook;
import entities.BorrowExtension;
import entities.DBMessage;
import entities.Subscriber;
import entities.User;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import observableEntities.ObservableBook;
import observableEntities.ObservableBorrow;
import observableEntities.ObservableEmployee;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BorrowsScreenController implements IClientUI
{
	User userLoggedIn;
	@FXML
	private TableView<ObservableBorrow> borrowsTable;

	@FXML
	private TableColumn<ObservableBorrow, String> borrowNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> borrowDateColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> returnDateColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> copyNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> catalogNumberColumn;

	@FXML
	private TableColumn<ObservableBorrow, String> subscriberIDColumn;

	private ObservableList<ObservableBorrow> observableBorrowsList;// for table view...
	
	@FXML
	private AnchorPane spinnerAnchorPane;

	@FXML
	private JFXSpinner spinner;
	
	protected Stage borrowExtensionDialog = null;
	
	protected JFXProgressBar borrowExtensionDialogProgressBar = null;
	
	@FXML
	private ImageView refreshBtn;
	@FXML
	private JFXTextField searchTextField;
	// this is the search function, it is in listener for text inside the textfield
	private InvalidationListener onSearchStart = new InvalidationListener()
	{
		
		@Override
		public void invalidated(Observable arg0)
		{

            if(searchTextField.textProperty().get().isEmpty()) 
            {
            	borrowsTable.setItems(observableBorrowsList);
                return;
            }

            ObservableList<ObservableBorrow> tableItems = FXCollections.observableArrayList();

            ObservableList<TableColumn<ObservableBorrow, ?>> cols = borrowsTable.getColumns();

            for (int i = 0; i < observableBorrowsList.size(); i++)
			{

				for (int j = 0; j < cols.size(); j++)
				{

					TableColumn col = cols.get(j);
					String cellValue = null;
                    try
					{
						cellValue = col.getCellData(observableBorrowsList.get(i)).toString();
					} 
                    catch (NullPointerException ex)
					{
						break;
					}

                    cellValue = cellValue.toLowerCase();

                    if(cellValue.contains(searchTextField.textProperty().get().toLowerCase())) 
                    {

                        tableItems.add(observableBorrowsList.get(i));

                        break;
                    }                        
                }
            }
            borrowsTable.setItems(tableItems);	
		}
	};
	
	@Override
	public User getUserLogedIn()
	{
		return userLoggedIn;
	}

	@Override
	public void setUserLogedIn(User userLoged)
	{
		userLoggedIn = userLoged;
		searchTextField.textProperty().addListener(onSearchStart);
		switch (userLoggedIn.getType())
		{
		case "subscriber":
			initialSubscriberView();
			break;
		case "librarian":
		case "library manager":
			 initialLibrarianView();
			break;
		}
	}


	private void initialLibrarianView()
	{
		borrowNumberColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
		borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		copyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		catalogNumberColumn.setCellValueFactory(new PropertyValueFactory<>("catalogNumber"));
		subscriberIDColumn.setCellValueFactory(new PropertyValueFactory<>("subscriberId"));
		//updateReturnDatesColors(returnDateColumn);
		observableBorrowsList = FXCollections.observableArrayList();
		spinnerAnchorPane.setLayoutX(280);
		spinnerAnchorPane.setLayoutY(170);
		 
		/*borrowsTable.setRowFactory(tv -> new TableRow<ObservableBorrow>()
		{
		    @Override
		    public void updateItem(ObservableBorrow item, boolean empty)
		    {
		        super.updateItem(item, empty) ;
		        if (item == null) 
		        {
					setTextFill(Color.BLACK);
		            setStyle("");
		            return;
		        }
		        String expectedRetunDate = item.getReturnDate();
		        if ((LocalDate.parse(getCurrentDateAsString()).isAfter(LocalDate.parse(expectedRetunDate)))) 
				{
					setText("-fx-text-fill: tomato;");
					setStyle("-fx-font-weight: bold");
				} 
				else if ((LocalDate.parse(getCurrentDateAsString())
						.isEqual(LocalDate.parse(expectedRetunDate))) // return day
						|| (LocalDate.parse(getCurrentDateAsString()).plusDays(1))
								.isEqual(LocalDate.parse(expectedRetunDate))) // one day before return date
				{
					setTextFill(Color.GREEN);
					setStyle("-fx-font-weight: bold");
				} 
				else 
				{
					setTextFill(Color.BLACK);
					setStyle("-fx-font-weight: lighter");
				}
		    }
		});
		*/
		
		borrowsTable.setRowFactory(tv -> 
		{
			TableRow<ObservableBorrow> row = new TableRow<ObservableBorrow>();// {
				//@Override
				/* public void updateItem(ObservableBorrow item, boolean empty)
			    {
					super.updateItem(item, empty) ;
			        if (item == null) 
			        {
						setTextFill(Color.BLACK);
			            setStyle("");
			            return;
			        }
			        String expectedRetunDate = item.getReturnDate();
			        if ((LocalDate.parse(getCurrentDateAsString()).isAfter(LocalDate.parse(expectedRetunDate)))) 
					{
						setText("-fx-text-fill: tomato;");
						setStyle("-fx-font-weight: bold");
					} 
					else if ((LocalDate.parse(getCurrentDateAsString())
							.isEqual(LocalDate.parse(expectedRetunDate))) // return day
							|| (LocalDate.parse(getCurrentDateAsString()).plusDays(1))
									.isEqual(LocalDate.parse(expectedRetunDate))) // one day before return date
					{
						setTextFill(Color.GREEN);
						setStyle("-fx-font-weight: bold");
					} 
					else 
					{
						setTextFill(Color.BLACK);
						setStyle("-fx-font-weight: lighter");
					}
			    }
				};*/
					
			
			row.setOnMouseClicked(event -> 
			{
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableBorrow rowData = row.getItem();
					borrowExtensionDialog = new Stage();
					borrowExtensionDialog.initModality(Modality.APPLICATION_MODAL);
					borrowExtensionDialog.setHeight(250);
					borrowExtensionDialog.setWidth(400);
					borrowExtensionDialog.setTitle("Borrow extension");
					borrowExtensionDialog.getIcons().add(new Image("/resources/Braude.png"));
					Label headline = new Label("Enter new expected return date:");
					headline.setStyle("-fx-text-fill: #a0a2ab");
					headline.setFont(new Font(16));
					VBox borrowExtensionDialogVbox = new VBox(15);
					Label newReturnDateLabel = new Label("New return date: ");
					newReturnDateLabel.setStyle("-fx-text-fill: #a0a2ab");
					JFXDatePicker newReturnDate = new JFXDatePicker();
					newReturnDate.setStyle("-fx-text-inner-color: #a0a2ab");
					newReturnDate.setPromptText("dd.mm.yyyy or dd.mm.yy");
					newReturnDate.setDayCellFactory(picker -> new DateCell()
					{
						public void updateItem(LocalDate date, boolean empty)
						{
							super.updateItem(date, empty);
							setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY 
									|| date.compareTo(LocalDate.parse(rowData.getReturnDate())) < 0
									|| date.compareTo(LocalDate.parse(rowData.getReturnDate()).plusDays(13)) > 0);
						}
					});
					GridPane grid = new GridPane();
					grid.add(newReturnDateLabel, 1, 1);
					grid.add(newReturnDate, 2, 1);
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setAlignment(Pos.CENTER);
					borrowExtensionDialogVbox.setAlignment(Pos.CENTER);
					Label warningMessageLabel = new Label("");
					warningMessageLabel.setStyle("-fx-text-fill: RED; -fx-font-weight: BOLD");
					JFXButton extendBorrowButton = new JFXButton("Extend borrow");
					extendBorrowButton.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
					borrowExtensionDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;");
					borrowExtensionDialogProgressBar = new JFXProgressBar();
					borrowExtensionDialogProgressBar.setVisible(false);
					extendBorrowButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle(ActionEvent e)
						{
							String warningMessage = "";
							warningMessageLabel.setText(warningMessage);
							if (newReturnDate.getValue() == null)
							{
								warningMessage = "Please enter return date";
							} 
							else
							{
								try
								{
									String newExpectedReturnDate = newReturnDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
									BorrowACopyOfBook borrow = new BorrowACopyOfBook(rowData.getBorrowId());
									BorrowExtension borrowToExtend = new BorrowExtension(borrow, newExpectedReturnDate, "manual", userLoggedIn.getId());
									borrowExtensionDialogProgressBar.setVisible(true);
									GuiManager.client.borrowExtension(borrowToExtend);
								} 
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
							}
							if (!warningMessage.isEmpty())
								warningMessageLabel.setText(warningMessage);
						}
					});
					borrowExtensionDialogVbox.getChildren().addAll(headline, grid, warningMessageLabel, extendBorrowButton, borrowExtensionDialogProgressBar);
					Scene borrowDialogScene = new Scene(borrowExtensionDialogVbox, 300, 200);
					borrowExtensionDialog.setScene(borrowDialogScene);
					borrowExtensionDialog.showAndWait();
				}
				
			});
			return row;
		});
		

		
		GuiManager.client.getAllCurrentBorrows();
	}

	private void initialSubscriberView()
	{
		borrowNumberColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
		borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		copyNumberColumn.setCellValueFactory(new PropertyValueFactory<>("copyId"));
		catalogNumberColumn.setCellValueFactory(new PropertyValueFactory<>("catalogNumber"));
		subscriberIDColumn.setVisible(false);
		spinnerAnchorPane.setLayoutX(280); 
		spinnerAnchorPane.setLayoutY(230);
		borrowsTable.setRowFactory(tv -> { // press on row in borrow table to do what ever we want
			TableRow<ObservableBorrow> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableBorrow rowData = row.getItem();
					borrowExtensionDialog = new Stage();
					borrowExtensionDialog.initModality(Modality.APPLICATION_MODAL);
					borrowExtensionDialog.setHeight(250);
					borrowExtensionDialog.setWidth(400);
					borrowExtensionDialog.setTitle("Borrow extension");
					borrowExtensionDialog.getIcons().add(new Image("/resources/Braude.png"));
					Label headline = new Label("Enter new expected return date:");
					headline.setStyle("-fx-text-fill: #a0a2ab");
					headline.setFont(new Font(16));
					VBox borrowExtensionDialogVbox = new VBox(15);
					Label newReturnDateLabel = new Label("New return date: ");
					newReturnDateLabel.setStyle("-fx-text-fill: #a0a2ab");
					JFXDatePicker newReturnDate = new JFXDatePicker();
					newReturnDate.setStyle("-fx-text-inner-color: #a0a2ab");
					newReturnDate.setPromptText("dd.mm.yyyy or dd.mm.yy");
					newReturnDate.setDayCellFactory(picker -> new DateCell()
					{
						public void updateItem(LocalDate date, boolean empty)
						{
							super.updateItem(date, empty);
							setDisable(empty || date.getDayOfWeek() == DayOfWeek.SATURDAY 
									|| date.compareTo(LocalDate.parse(rowData.getReturnDate())) < 0
									|| date.compareTo(LocalDate.parse(rowData.getReturnDate()).plusDays(13)) > 0);
						}
					});
					GridPane grid = new GridPane();
					grid.add(newReturnDateLabel, 1, 1);
					grid.add(newReturnDate, 2, 1);
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setAlignment(Pos.CENTER);
					borrowExtensionDialogVbox.setAlignment(Pos.CENTER);
					Label warningMessageLabel = new Label("");
					warningMessageLabel.setStyle("-fx-text-fill: RED; -fx-font-weight: BOLD");
					JFXButton extendBorrowButton = new JFXButton("Extend borrow");
					extendBorrowButton.setStyle("-fx-background-color: #3C58FA; -fx-text-fill: white;");
					borrowExtensionDialogVbox.setStyle("-fx-background-color: #203447; -fx-text-fill: #a0a2ab;");
					borrowExtensionDialogProgressBar = new JFXProgressBar();
					borrowExtensionDialogProgressBar.setVisible(false);
					extendBorrowButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle(ActionEvent e)
						{
							String warningMessage = "";
							warningMessageLabel.setText(warningMessage);
							if (newReturnDate.getValue() == null)
							{
								warningMessage = "Please enter return date";
							} 
							else
							{
								try
								{
									String newExpectedReturnDate = newReturnDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
									BorrowACopyOfBook borrow = new BorrowACopyOfBook(rowData.getBorrowId());
									BorrowExtension borrowToExtend = new BorrowExtension(borrow, newExpectedReturnDate, "automatic", userLoggedIn.getId());
									borrowExtensionDialogProgressBar.setVisible(true);
									GuiManager.client.borrowExtension(borrowToExtend);
								} 
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
							}
							if (!warningMessage.isEmpty())
								warningMessageLabel.setText(warningMessage);
						}
					});
					borrowExtensionDialogVbox.getChildren().addAll(headline, grid, warningMessageLabel, extendBorrowButton, borrowExtensionDialogProgressBar);
					Scene borrowDialogScene = new Scene(borrowExtensionDialogVbox, 300, 200);
					borrowExtensionDialog.setScene(borrowDialogScene);
					borrowExtensionDialog.showAndWait();
				}
			});
			return row;
		});
		//updateReturnDatesColors(returnDateColumn);
		observableBorrowsList = FXCollections.observableArrayList();
		GuiManager.client.getCurrentBorrowsForSubscriberID(userLoggedIn.getId());
		
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetCurrentBorrowsForSubID:
		case GetCurrentBorrows:
		{
			updateBorrowTable((List<BorrowACopyOfBook>)msg.Data);
			spinnerAnchorPane.setVisible(false);
			spinner.setVisible(false);
			break;
		}
		case BorrowExtension:
		{
			BorrowExtension newBorrowExtension = (BorrowExtension) msg.Data;
			if (newBorrowExtension.getBorrow().getActualReturnDate().equals("1"))
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Borrow extension is available only one week\n(or less) before expected return date!");
				});
			}
			else if (newBorrowExtension.getBorrow().getBookCatalogNumber().equals("0"))
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Another subscriber has ordered this book,\nborrow extension is unavailable!");
				});
			} 
			else if (newBorrowExtension.getBorrow().getSubscriberId().equals("0"))
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					if(newBorrowExtension.getExtensionType().equals("manual"))
						GuiManager.ShowMessagePopup("The subscriber card status is not active,\nborrow extension is unavailable!");
					else if(newBorrowExtension.getExtensionType().equals("automatic"))
						GuiManager.ShowMessagePopup("Your card status is not active,\nborrow extension is unavailable!");
				});
			}
			else if (newBorrowExtension.getBorrow().getBookCatalogNumber().equals("-1"))
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("The book has been archived,\nborrow extension is unavailable!");
				});
			}
			else if (newBorrowExtension.getBorrow().getBookCatalogNumber().equals("-2"))
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("This book is wanted, borrow extension is unavailable!");
				});
			}
			else
			{
				Platform.runLater(() -> {
					borrowExtensionDialogProgressBar.setVisible(false);
					GuiManager.ShowMessagePopup("Borrow extasion executed Successfully!");
					borrowExtensionDialog.close();
				});
			}
			break;
		}
		default:
			break;
		}
	}

	private void updateBorrowTable(List<BorrowACopyOfBook> borrowList)
	{
		observableBorrowsList.clear();
		for (BorrowACopyOfBook borrow : borrowList)
		{
			ObservableBorrow temp = new ObservableBorrow(borrow.getId(), borrow.getBorrowDate(),
					borrow.getExpectedReturnDate(), borrow.getCopyId(), borrow.getBookCatalogNumber(),
					borrow.getSubscriberId());
			observableBorrowsList.add(temp);
		}
		borrowsTable.setItems(observableBorrowsList);
	}
	
	@FXML
	void refreshBtnClicked(MouseEvent event)
	{
		if(userLoggedIn.getType().equals("subscriber"))
			GuiManager.client.getCurrentBorrowsForSubscriberID(userLoggedIn.getId());
		else
			GuiManager.client.getAllCurrentBorrows();	
	}
	
	@FXML
	void pressRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(0.5);
	}

	@FXML
	void releasedRefresh(MouseEvent event)
	{
		refreshBtn.setOpacity(1);
	}
	
	/*
	private void updateReturnDatesColors(TableColumn<ObservableBorrow, String> column) 
	{
		returnDateColumn.setCellFactory(
				new Callback<TableColumn<ObservableBorrow, String>, TableCell<ObservableBorrow, String>>() 
				{
					public TableCell call(TableColumn param) 
					{
						return new TableCell<ObservableBorrow, String>() 
						{
							@Override
							public void updateItem(String item, boolean empty) 
							{
								super.updateItem(item, empty);

								if (item != null) 
								{

									if (item.length() == 10) // expected return date column
									{
										if ((LocalDate.parse(getCurrentDateAsString())
												.isAfter(LocalDate.parse(item)))) 
										{
											setTextFill(Color.RED);
											setStyle("-fx-font-weight: bold");
											setText(item);
										} 
										else if ((LocalDate.parse(getCurrentDateAsString())
												.isEqual(LocalDate.parse(item))) // return day
												|| (LocalDate.parse(getCurrentDateAsString()).plusDays(1))
														.isEqual(LocalDate.parse(item))) // one day before return date
										{
											setTextFill(Color.GREEN);
											setStyle("-fx-font-weight: bold");
											setText(item);
										} 
										else 
										{
											setTextFill(Color.BLACK);
											setStyle("-fx-font-weight: lighter");
											setText(item);
										}

									}
								}
							}
						};
					}
				});
	}*/
	
	public static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}	
}
