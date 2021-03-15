package gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import entities.Book;
import entities.BorrowACopyOfBook;
import entities.BorrowExtension;
import entities.DBMessage;
import entities.Employee;
import entities.Report_Activity;
import entities.Report_BorrowDurationInfo;
import entities.Report_LateReturns;
import entities.Subscriber;
import entities.User;
import gui.GuiManager.SCREENS;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observableEntities.ObservableBook;
import observableEntities.ObservableBorrow;
import observableEntities.ObservableEmployee;
import observableEntities.ObservableMessage;

public class LibrarianManagerController extends LibrarianScreenController
{
	private Reports_BorrowsController borrowReportControler;
	private Reports_ActivityController activityReportController;
	private Reports_LateReturnsController lateReturnsController;

	@FXML
	private TableView<ObservableEmployee> emplyeeTableView;

	@FXML
	private TableColumn<ObservableEmployee, String> empNumColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empIDColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empFirstNameColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empLastNameColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empEmailColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empRoleColumn;

	@FXML
	private TableColumn<ObservableEmployee, String> empDepartmentColumn;

	@FXML
	private AnchorPane employeesSpinnerAnchorPane;

	@FXML
	private JFXSpinner employeesSpinner;

	@FXML
	private JFXSpinner thinkSpinner;

	private ObservableList<ObservableEmployee> empList;// for table view...

	@FXML
	private Pane pane_employees, pane_reports;

	@FXML
	private ImageView btn_employees, btn_reports;

	@FXML
	private JFXTextField searchTextField;// Search employee

	@FXML
	private ImageView refreshBtn;

	@FXML
	private JFXButton generateReportBtn;

	@FXML
	private ToggleGroup reportToggleGroup;

	@FXML
	private JFXListView<String> reportsListView;

	private ObservableList<String> reportsList;// for table view...

	@FXML
	private ProgressIndicator loadListSpinner;

	@FXML
	private Label instructionLabelActivityReport;

	private Map<String, Report_Activity> oldActivityReports;

	@FXML
	private ImageView instructionImageView;

	// this is the search function, it is in listener for text inside the textfield
	private InvalidationListener onSearchStart = new InvalidationListener()
	{

		@Override
		public void invalidated(Observable arg0)
		{

			if (searchTextField.textProperty().get().isEmpty())
			{

				emplyeeTableView.setItems(empList);

				return;

			}

			ObservableList<ObservableEmployee> tableItems = FXCollections.observableArrayList();

			ObservableList<TableColumn<ObservableEmployee, ?>> cols = emplyeeTableView.getColumns();

			for (int i = 0; i < empList.size(); i++)
			{

				for (int j = 0; j < cols.size(); j++)
				{

					TableColumn col = cols.get(j);
					String cellValue = null;
					try
					{
						cellValue = col.getCellData(empList.get(i)).toString();
					} catch (NullPointerException ex)
					{
						break;
					}

					cellValue = cellValue.toLowerCase();

					if (cellValue.contains(searchTextField.textProperty().get().toLowerCase()))
					{

						tableItems.add(empList.get(i));

						break;

					}

				}

			}

			emplyeeTableView.setItems(tableItems);
		}
	};
	// private SearchBookController searchBookWindowController = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		super.initialize(arg0, arg1);
		GuiManager.client.getEmployeeList();

		searchTextField.textProperty().addListener(onSearchStart);

		reportsListView.setVisible(false);
		loadListSpinner.setVisible(false);
		instructionLabelActivityReport.setVisible(false);
		thinkSpinner.setVisible(false);

		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);

		empNumColumn.setCellValueFactory(new PropertyValueFactory<>("empNumber"));
		empIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		empFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		empLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		empEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		empRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
		empDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

		empList = FXCollections.observableArrayList();
		reportsList = FXCollections.observableArrayList();

		reportsListView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
				{
					openActivityReport(oldActivityReports.get(((JFXListCell) event.getTarget()).getText()), false);

					/*
					 * if (click.getClickCount() == 2) { //Use ListView's getSelected Item
					 * currentItemSelected = playList.getSelectionModel() .getSelectedItem(); //use
					 * this to do whatever you want to. Open Link etc.
					 */
				}
			}
		});

	}

	@FXML
	protected void btn_homeDisplay(MouseEvent event)
	{
		super.btn_homeDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_createNewSubscriberCardDisplay(MouseEvent event)
	{
		super.btn_createNewSubscriberCardDisplay(event);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_booksDisplay(MouseEvent event)
	{
		super.btn_booksDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	protected void btn_searchSubscriberCardDisplay(MouseEvent event)
	{
		super.btn_searchSubscriberCardDisplay(event);
		pane_employees.setVisible(false);
		pane_reports.setVisible(false);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(1);
	}

	@FXML
	void btn_employeesDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		pane_employees.setVisible(true);
		pane_reports.setVisible(false);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);
		btn_employees.setOpacity(0.5);
		btn_reports.setOpacity(1);
	}

	@FXML
	void btn_reportsDisplay(MouseEvent event)
	{
		pane_home.setVisible(false);
		pane_createNewSubscriberCard.setVisible(false);
		pane_searchBook.setVisible(false);
		pane_searchSubscriberCard.setVisible(false);
		pane_employees.setVisible(false);
		pane_reports.setVisible(true);
		btn_home.setOpacity(1);
		btn_createNewSubscriberCard.setOpacity(1);
		btn_books.setOpacity(1);
		btn_searchSubscriberCard.setOpacity(1);
		btn_employees.setOpacity(1);
		btn_reports.setOpacity(0.5);
	}

	@Override
	public void getMessageFromServer(DBMessage msg)
	{
		switch (msg.Action)
		{
		case GetEmployeeList:
		{
			updateEmpList((ArrayList<Employee>) msg.Data);
			employeesSpinnerAnchorPane.setVisible(false);
			employeesSpinner.setVisible(false);
			break;
		}
		case Reports_getAvarageBorrows:
		{
			Platform.runLater(() -> {
				thinkSpinner.setVisible(false);
				openBorrowReport((Report_BorrowDurationInfo) msg.Data);
			});
			break;
		}
		case Reports_Activity:
		{
			Platform.runLater(() -> {
				thinkSpinner.setVisible(false);
				openActivityReport((Report_Activity) msg.Data, true);
			});
			break;
		}
		case Reports_getList:
		{
			updateReportsList((List<Report_Activity>) msg.Data);
			break;
		}
		case Reports_Add:
		{
			Platform.runLater(() -> {
				if (msg.Data == null)
					GuiManager.ShowErrorPopup("Report saving went wrong\nPlease restart the program and try again.");
				else
					GuiManager.ShowMessagePopup(
							"Report " + ((Report_Activity) msg.Data).getReportDate() + " saving succeeded.");
				loadListSpinner.setVisible(true);
				GuiManager.client.getReportsList();
			});
			break;
		}
		case Reports_LateReturns:
		{
			Platform.runLater(() -> {
				thinkSpinner.setVisible(false);
				openLateReturnsReport((Report_LateReturns) msg.Data);
			});
			break;
		}
		default:
			super.getMessageFromServer(msg);
		}
	}

	private void updateEmpList(ArrayList<Employee> data)
	{
		empList.clear();
		for (Employee employee : data)
		{
			empList.add(new ObservableEmployee(employee.getEmpNumber(), employee.getId(), employee.getFirstName(),
					employee.getLastName(), employee.getEmail(), employee.getRole(), employee.getDepartment()));
		}
		emplyeeTableView.setItems(empList);
	}

	@FXML
	void refreshBtnClicked(MouseEvent event)
	{
		GuiManager.client.getEmployeeList();
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

	@FXML
	void radioBtnClicked(ActionEvent event)
	{
		switch (((JFXRadioButton) reportToggleGroup.getSelectedToggle()).getText())
		{
		case "Activity":
			loadListSpinner.setVisible(true);
			GuiManager.client.getReportsList();
			generateReportBtn.setDisable(false);
			reportsListView.setVisible(true);
			instructionLabelActivityReport.setVisible(true);

			break;
		case "Borrows Duration":
			generateReportBtn.setDisable(false);
			reportsListView.setVisible(false);
			instructionLabelActivityReport.setVisible(false);
			loadListSpinner.setVisible(false);
			break;
		case "Late Returns":
			generateReportBtn.setDisable(false);
			reportsListView.setVisible(false);
			instructionLabelActivityReport.setVisible(false);
			loadListSpinner.setVisible(false);
			break;
		}
	}

	@FXML
	void generateReportClicked(ActionEvent event)
	{
		thinkSpinner.setVisible(true);
		switch (((JFXRadioButton) reportToggleGroup.getSelectedToggle()).getText())
		{
		case "Activity":
			GuiManager.client.report_ActivityInfo();
			break;
		case "Borrows Duration":
			GuiManager.client.report_getBorrowDurationInfo();
			break;
		case "Late Returns":
			GuiManager.client.report_getLateReturnsInfo();
			break;
		}

	}

	private void openBorrowReport(Report_BorrowDurationInfo info)
	{
		try
		{
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/Reports_BorrowsDuration.fxml"));
			Parent root = loader.load();
			borrowReportControler = loader.getController();
			borrowReportControler.setReportInformation(info);
			Scene scene = new Scene(root);
			SeondStage.setTitle("Borrows Duration");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.showAndWait();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void openActivityReport(Report_Activity info, boolean isNew)
	{
		try
		{
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/Reports_Activity.fxml"));
			Parent root = loader.load();
			activityReportController = loader.getController();
			activityReportController.setReportInformation(info);
			activityReportController.setSaveVisible(isNew);
			Scene scene = new Scene(root);
			SeondStage.setTitle("Activity Report");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.showAndWait();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void updateReportsList(List<Report_Activity> data)
	{
		oldActivityReports = new HashMap<>();
		Platform.runLater(() -> {
			loadListSpinner.setVisible(false);

			reportsList.clear();
			for (Report_Activity report : data)
			{
				oldActivityReports.put(report.getReportDate(), report);
				reportsList.add(report.getReportDate());
			}
			reportsListView.setItems(reportsList);
		});
	}

	private void openLateReturnsReport(Report_LateReturns data)
	{
		try
		{
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/Reports_LateReturns.fxml"));
			Parent root = loader.load();
			lateReturnsController = loader.getController();
			lateReturnsController.setReportInformation(data);
			Scene scene = new Scene(root);
			SeondStage.setTitle("Late returns Duration");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.showAndWait();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void setMessages()
	{
		super.setMessages();
		this.messagesTableView.setRowFactory(tv -> { // press on row in book table to open book information
			TableRow<ObservableMessage> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty()))
				{
					ObservableMessage rowData = row.getItem();
					String msg = rowData.getMsgContent();
					String id = msg.substring(16, 25);
					GuiManager.client.getSubscriberFromDB(id);
				}
			});
			return row;
		});
	}


}
