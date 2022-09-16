package application;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private FXController guiController;
	
	public Main() {
		guiController = new FXController();
	}
	
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("white-scene.fxml"));
		fxmlLoader.setController(guiController);
		Parent root;
		try {
			root = fxmlLoader.load();
			guiController.loadFisrtScene();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Connected minimal automata");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
