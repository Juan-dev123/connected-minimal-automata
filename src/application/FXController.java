package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class FXController {
	@FXML
	private ChoiceBox<String> automatonSelected;
	
    @FXML
    private BorderPane mainPanel;
	
	public FXController() {
		
	}

	// Event Listener on Button.onAction
	@FXML
	public void goToSelectedAutomaton(ActionEvent event) {
		System.out.println(automatonSelected.getValue());
	}
	
	public void loadFisrtScene() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("first-scene.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		automatonSelected.getItems().add("Mealy");
		automatonSelected.getItems().add("Moore");
		automatonSelected.setValue("Mealy");
		mainPanel.setCenter(managerPane);
	}
}
