package gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.jfoenix.controls.JFXTextField;

import client.ClientController;
import client.IClientUI;
import entities.ActivityLog;
import entities.DBMessage;
import entities.User;
import entities.DBMessage.DBAction;
import entities.Subscriber;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * A static class, handling all the GUI's controllers. This is the main Controller between the
 * communication with the server (aka "ClientController") and the GUI (all of
 * the FXML controllers).
 */
public class GuiManager
{
	/**
	 * our singleton of the client instance.
	 *
	 */
	public static ClientController client;
	/**
	 * Will be updated in order to now what screen is shown for the user.
	 */
	public static IClientUI CurrentGuiController;
	public static boolean dbConnected = false;
	public static ViewSubscriberCardController subscriberCardController = null;
	public static int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	private static String serverIp;

	/**
	 * Map from string to a type of user screen
	 */
	public static Map<String, SCREENS> userTypeFromString = new HashMap<String, SCREENS>()
	{
		{
			put("librarian", SCREENS.librarian);
			put("subscriber", SCREENS.subscriber);
			put("library manager", SCREENS.librarianManager);

		}
	};
	/**
	 * Map between user screen type to FXML path. this not all the paths available -
	 * only the necessary here. You can add as much as you want.
	 */
	public static Map<SCREENS, String> availableFXML = new HashMap<SCREENS, String>()
	{
		{
			put(SCREENS.login, "/gui/LoginScreen.fxml");
			put(SCREENS.librarian, "/gui/LibrarianScreen.fxml");
			put(SCREENS.searchBook, "/gui/SearchBookScreen.fxml");
			put(SCREENS.bookInformation, "/gui/BookInformationScreen.fxml");
			put(SCREENS.subscriber, "/gui/SubscriberScreen.fxml");
			put(SCREENS.librarianManager, "/gui/LibrarianManagerScreen.fxml");
			put(SCREENS.addNewBook, "/gui/AddNewBookScreen.fxml");

		}
	};

	/**
	 * Pop up a error message
	 * 
	 * @param msg - what to show in the error message
	 */
	public static void ShowErrorPopup(String msg)
	{
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected Error");
			alert.setHeaderText("");
			alert.setContentText(msg);
			alert.showAndWait();
		});
	}

	/**
	 * Pop up a information message
	 * 
	 * @param msg - what to show in the message
	 */
	public static void ShowMessagePopup(String msg)
	{
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setHeaderText("");
			alert.setContentText(msg);
			alert.showAndWait();
		});
	}

	/**
	 * Wherever you need to switch scene (e.g. from login to user screen) this
	 * function initial all the needed data and update the "CurrentGuiController"
	 * 
	 * @param screenEnum - the screen you want to switch to from the enum list.
	 */
	public static void SwitchScene(SCREENS screenEnum)
	{
		try
		{
			if (screenEnum == SCREENS.login && !(CurrentGuiController instanceof SearchBookController))
				client.updateUserLogOut(CurrentGuiController.getUserLogedIn());
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(availableFXML.get(screenEnum)));
			Parent root = loader.load();
			CurrentGuiController = loader.getController();
			Scene scene = new Scene(root);
			SeondStage.setTitle("Ort Braude Library");
			SeondStage.setOnCloseRequest(e -> shutDown());// make sure safe shutdown
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * create the client singleton type. initial server communication
	 * 
	 * @param fxmlPath
	 * @param primaryStage
	 */
	public static void InitialPrimeryStage(SCREENS fxmlPath, Stage primaryStage)
	{
		//getServerIp();
		try
		{
			client = ClientController.createClientIfNotExist("localhost"/*serverIp*/, ClientController.DEFAULT_PORT);// get
																											// connection
			client.sendToServer(new DBMessage(DBAction.isDBRuning, null));// check DB
		} catch (Exception e)
		{
			client = null;
		} finally
		{
			try
			{
				FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource(availableFXML.get(fxmlPath)));
				Parent root = loader.load();
				CurrentGuiController = loader.getController();
				Scene Scene = new Scene(root);
				primaryStage.setScene(Scene);
				primaryStage.setOnCloseRequest(e -> shutDown());// make sure safe shutdown
				primaryStage.setTitle("Ort Braude Library");
				primaryStage.getIcons().add(new Image("/resources/Braude.png"));
				primaryStage.show();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * The initial window for the connection to server
	 */
	private static void getServerIp()
	{
		final Stage dialog = new Stage();
		dialog.setOnCloseRequest(new EventHandler<WindowEvent>()
		{

			@Override
			public void handle(WindowEvent arg0)
			{
				System.exit(0);
			}
		});
		dialog.initStyle(StageStyle.DECORATED);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle("Connect to Server");
		VBox dialogVbox = new VBox(10);
		Label headline = new Label("Enter Server IP");
		headline.setFont(new Font(25));
		Label warning = new Label("(Make sure you are on the same lan)");
		warning.setFont(new Font(12));
		dialogVbox.setAlignment(Pos.CENTER);
		javafx.scene.control.TextField ipTextField = new javafx.scene.control.TextField("Example: 162.123.1.206");
		ipTextField.setMaxWidth(140);
		Button button = new Button("OK");
		button.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event e)
			{
				if (ipTextField.getText().isEmpty() || ipTextField.getText().contains("Example: 162.123.1.206"))
					return;
				serverIp = ipTextField.getText();
				dialog.close();
			}
		});
		dialogVbox.getChildren().addAll(headline, warning, ipTextField, button);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}
	
	/**
	 * A "safe shutdown" function doing logout and closing the connection to the
	 * server.
	 */
	private static void shutDown()
	{
		if (client == null)
			return;
		try
		{
			if (CurrentGuiController instanceof LibrarianManagerController
					|| CurrentGuiController instanceof LibrarianScreenController
					|| CurrentGuiController instanceof SubscriberScreenController)
			{
				client.updateUserLogOut(CurrentGuiController.getUserLogedIn());
			}
			client.closeConnection();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static enum SCREENS
	{
		login, librarian, searchBook, bookInformation, subscriber, librarianManager, viewSubscriberCard, addNewBook;
	}

	/**
	 * useful function to prevent letters in number textfield
	 * 
	 * @param textField
	 */
	public static void preventLettersTypeInTextField(JFXTextField textField)
	{
		Platform.runLater(() -> {

			textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent e)
				{
					if (!"0123456789".contains(e.getCharacter()))
					{
						e.consume();
					}
				}
			});
		});
	}

	/**
	 * useful function to make a maxLength in textfield string
	 * 
	 * @param textField
	 * @param maxLength
	 */
	public static void limitTextFieldMaxCharacters(JFXTextField textField, int maxLength)
	{
		Platform.runLater(() -> {

			textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent e)
				{
					if (textField.getText().length() >= maxLength)
					{
						e.consume();
					}
				}
			});
		});
	}

	/**
	 * opening a subscriber card
	 * 
	 * @param newSub
	 */
	public static void openSubscriberCard(Subscriber newSub, User user)
	{
		try
		{
			Stage SeondStage = new Stage();
			FXMLLoader loader = new FXMLLoader(GuiManager.class.getResource("/gui/ViewSubscriberCardScreen.fxml"));
			Parent root = loader.load();
			subscriberCardController = loader.getController();
			if (subscriberCardController != null)
				subscriberCardController.setSubscriberToShow(newSub);
			else
				ShowErrorPopup("Somthing get wrong , please restart the system");
			subscriberCardController.setUserLogedIn(user);
			Scene scene = new Scene(root);
			SeondStage.setResizable(false);
			SeondStage.setTitle("Subscriber Card");
			SeondStage.getIcons().add(new Image("/resources/Braude.png"));
			SeondStage.setScene(scene);
			SeondStage.showAndWait();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * setting an activity log to a subscriber card for librarian or subscriber
	 * view.
	 * 
	 * @param activityList
	 */
	public static void openActvityLog(ArrayList<ActivityLog> activityList)
	{
		try
		{
			if (subscriberCardController != null)
				subscriberCardController.setActivityLogList(activityList);
			else
				ShowErrorPopup("Somthing get wrong , please restart the system");

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * checking if email adress is a valid email adress.
	 * 
	 * @param email
	 * @return true or false
	 */
	public static boolean isValidEmailAddress(String email)
	{
		boolean result = true;
		try
		{
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex)
		{
			result = false;
		}
		return result;
	}
}
