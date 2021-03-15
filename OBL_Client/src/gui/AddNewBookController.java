package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import entities.Book;
import entities.DBMessage;
import entities.User;
import entities.DBMessage.DBAction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddNewBookController implements Initializable
{
	@FXML
	private TextArea descreptionPane;

	@FXML
	private JFXTextField bookNameTextArea;

	@FXML
	private JFXTextField authorTextArea;

	@FXML
	private JFXTextField categoriesTextArea;

	@FXML
	private JFXTextField publicationYearTextField;

	@FXML
	private JFXTextField editionNumTextField;

	@FXML
	private JFXTextField locationTextField;

	@FXML
	private JFXButton btnAddBook;

	@FXML
	private JFXButton btnTOC;

	@FXML
	private JFXCheckBox wantedBook;

	@FXML
	private Spinner<Integer> copiesSpinner;

	@FXML
	private Label warningLabel;

	protected static final String INITAL_VALUE = "0";

	private byte[] tocArraybyte = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

		copiesSpinner.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, Integer.parseInt(INITAL_VALUE)));
		copiesSpinner.setEditable(false);
		
		GuiManager.preventLettersTypeInTextField(editionNumTextField);
		GuiManager.limitTextFieldMaxCharacters(editionNumTextField, 11);

		GuiManager.limitTextFieldMaxCharacters(locationTextField, 6);
		
		GuiManager.limitTextFieldMaxCharacters(publicationYearTextField, 4);
		GuiManager.preventLettersTypeInTextField(publicationYearTextField);


	}

	public static String getCurrentDateAsString()
	{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String string = format.format(calendar.getTime());
		return string;
	}

	@FXML
	void btnAddBookClick(ActionEvent event)
	{

		warningLabel.setText("");

		if (bookNameTextArea.getText().isEmpty() || categoriesTextArea.getText().isEmpty()
				|| copiesSpinner.getValue().equals(0) || authorTextArea.getText().isEmpty()
				|| publicationYearTextField.getText().isEmpty() || editionNumTextField.getText().isEmpty()
				|| locationTextField.getText().isEmpty())

		{
			warningLabel.setTextFill(Color.RED);
			warningLabel.setText("Please fill all the requierd field.");
			return;
		} else
		{
			Calendar now = Calendar.getInstance();
			String purchaseDate = getCurrentDateAsString();

			ArrayList<String> authorNameList = new ArrayList<String>(
					Arrays.asList(authorTextArea.getText().split(",")));

			ArrayList<String> categories = new ArrayList<String>(
					Arrays.asList(categoriesTextArea.getText().split(",")));

			String classification;
			if (wantedBook.isSelected())
				classification = "wanted";
			else
				classification = "ordinary";
			int publicitionYear = Integer.parseInt(publicationYearTextField.getText());
			if (publicitionYear > GuiManager.currentYear || publicitionYear < 1902)
			{
				warningLabel.setTextFill(Color.RED);
				warningLabel.setText("Enter correct year please");
				return;
			}

			Book tempbook = new Book(bookNameTextArea.getText(), purchaseDate, authorNameList, categories,
					publicationYearTextField.getText(), editionNumTextField.getText(), locationTextField.getText(),
					descreptionPane.getText(), copiesSpinner.getValue(), classification, tocArraybyte);
			GuiManager.client.AddNewBook(tempbook);
		}

	}

	@FXML
	void btnTableOfcontentClick(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog((Stage) btnTOC.getScene().getWindow());
		// file.getName();
		if (file != null)
		{
			try
			{
				if (!getFileExtension(file.getName()).equals("pdf"))
					GuiManager.ShowErrorPopup("Error - file must be a pdf file");
				else
				{
					tocArraybyte = Files.readAllBytes(file.toPath());
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				// e.printStackTrace();
				tocArraybyte = null;
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

}
