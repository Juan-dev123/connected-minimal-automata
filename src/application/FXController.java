package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class FXController {
	@FXML
	private ChoiceBox<String> automatonSelected;
	
    @FXML
    private BorderPane mainPanel;
    
	@FXML
    private ChoiceBox<?> cbResponse;

    @FXML
    private ChoiceBox<?> cbState;

    @FXML
    private TextField tfStimulus;

    @FXML
    private TextField txStatus;
    
    @FXML
    private Button btnToMealy;

    @FXML
    private Button btnToMoore;

    @FXML
    private TextField tfStimulusToAdd;
    
    @FXML
    private Button btnBackFirstScene;
    
    @FXML
    private TableView<Stimulus> tvStimulus;
    
    @FXML
    private TableColumn<Stimulus, String> stimulusColumn;
    
    private ObservableList<Stimulus> stimulus;
	
	public FXController() {
		List<Stimulus> listS = new ArrayList<Stimulus>();
		stimulus = FXCollections.observableArrayList(listS); 
	}

	// Event Listener on Button.onAction
	@FXML
	public void goToSelectedAutomaton(ActionEvent event) throws IOException {
		showInformationAlert(null, null, "First create the stimulus set");
		int origin = 1; 
		int destiny;
		switch(automatonSelected.getValue()) {
			case "Mealy":
				destiny = 1;
				break;
			case "Moore":
				destiny = 2;
				break;
			default:
				destiny = 0;
				break;
		}
		loadStimulus(origin, destiny);
		
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
	
	public void loadStimulus(int origin, int destiny) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stimulus.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		switch(origin) {
			//The origin is the first scene
			case 1:
				btnBackFirstScene.setDisable(false);
				btnBackFirstScene.setVisible(true);
				break;
		}
		switch(destiny) {
			//The destiny is the mealy scene
			case 1:
				btnToMealy.setDisable(false);
				btnToMealy.setVisible(true);
				break;
		}
		stimulusColumn.setCellValueFactory(
				new PropertyValueFactory<Stimulus,String>("stimulus") 
		);
		tvStimulus.setItems(stimulus);
		mainPanel.setCenter(managerPane);
	}
	
	/**
	 * 
	 * @param title
	 * @param header
	 * @param content
	 */
	public void showErrorAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * 
	 * @param title
	 * @param header
	 * @param content
	 */
	public void showWarningAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void showInformationAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
    @FXML
    void addStatus(ActionEvent event) {

    }

    @FXML
    void backToFisrtScene(ActionEvent event) throws IOException {
    	loadFisrtScene();
    }

    @FXML
    void continueToMealy(ActionEvent event) {

    }

    @FXML
    void continueToMoore(ActionEvent event) {

    }
    
    @FXML
    void deleteStimulus(ActionEvent event) {

    }
    

    @FXML
    void addStimulus(ActionEvent event) {
    	
    	stimulus.add(new Stimulus(tfStimulusToAdd.getText()));
    	tfStimulusToAdd.clear();
    	 /**data.add(new Person(
                 addFirstName.getText(),
                 addLastName.getText(),
                 addEmail.getText()));
         addFirstName.clear();**/
    }
    
    public static class Stimulus{
    	private final SimpleStringProperty stimulus;
    	
    	private Stimulus(String stimulus) {
    		this.stimulus = new SimpleStringProperty(stimulus);
    	}
    	
    	public String getStimulus() {
    		return stimulus.get();
    	}
    	
    }
}
