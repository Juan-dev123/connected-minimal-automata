package application;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


/**
 * @author Juan-dev123
 *
 */
public class Main extends Application {
	
	private FXController guiController;
	
	/**
	 * The constructor method of the Main class <br>
	 */
	public Main() {
		guiController = new FXController();
	}
	
	/**
	 * Starts the GUI
	 */
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("white-scene.fxml"));
		fxmlLoader.setController(guiController);
		Parent root;
		try {
			root = fxmlLoader.load();
			guiController.loadCreateTable();
			Scene scene = new Scene(root);
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("sigma.png")));
			primaryStage.setScene(scene);
			primaryStage.setTitle("Connected minimal automaton");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args The command line parameters
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
