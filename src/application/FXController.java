package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

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
    private TextField txStatus;
    
    @FXML
    private Button btnToMealy;

    @FXML
    private Button btnToMoore;
    
    @FXML
    private Button btnBackFirstScene;
    
    @FXML
    private ChoiceBox<String> cbInputData;
    
    @FXML
    private TableColumn<InputSymbol, String> colInputSymbols;

    @FXML
    private TableColumn<OutputSymbol, String> colOutputSymbols;

    @FXML
    private TextField tfInputSymbol;

    @FXML
    private TextField tfOutputSymbol;

    @FXML
    private TableView<InputSymbol> tvInputSymbols;

    @FXML
    private TableView<OutputSymbol> tvOutputSymbols;
    
    @FXML
    private Label lbTableTitle;

    @FXML
    private TableView<State> tvAutomatonTable;
    
    @FXML
    private TextField txStateToAdd;
    
    private ObservableList<InputSymbol> inputSymbols;
    
    private ObservableList<OutputSymbol> outputSymbols;
    
    private ObservableList<State> states;
	
	public FXController() {
		List<InputSymbol> listIS = new ArrayList<InputSymbol>();
		inputSymbols = FXCollections.observableArrayList(listIS); 
		List<OutputSymbol> listOS = new ArrayList<OutputSymbol>();
		outputSymbols = FXCollections.observableArrayList(listOS); 
		List<State> listStates = new ArrayList<State>();
		states = FXCollections.observableArrayList(listStates); 
	}
	
    @FXML
    void nextScene(ActionEvent event) throws IOException {
    	switch(cbInputData.getValue()) {
		case "Manually":
			loadCreateTable();
			break;
		case "From txt":
			
			break;
		default:
			showErrorAlert(null, null, "Something happened, please try it again");
			break;
    	}
    }

	@FXML
	public void createMealyTable() throws IOException {
		List<TableColumn<State, String>> tables = new ArrayList<>();
		tables.add(new TableColumn<State, String>("State"));
		tables.get(0).setCellValueFactory(new PropertyValueFactory<State,String>("name"));
		for(int i = 0; i < inputSymbols.size(); i++) {
			tables.add(new TableColumn<State, String>(inputSymbols.get(i).getSymbol()));
			TableColumn<State, TextField> tempState = new TableColumn<State, TextField>("State");
			tempState.setCellValueFactory(createArrayValueFactory(State::getOutputStates, i));
			TableColumn<State, ChoiceBox<String>> tempOutput = new TableColumn<State, ChoiceBox<String>>("Output");
			tempOutput.setCellValueFactory(createArrayValueFactory(State::getOutputSymbols, i));
			tables.get(i+1).getColumns().addAll(tempState, tempOutput);
		}
		
		//colInputSymbols.setCellValueFactory(new PropertyValueFactory<InputSymbol,String>("symbol"));
		//tvInputSymbols.setItems(inputSymbols);
		
		ObservableList<TableColumn<State, String>> observableList = FXCollections.observableArrayList(tables);
        
        tvAutomatonTable.getColumns().addAll(observableList);
        tvAutomatonTable.setItems(states);
	}
	
	@FXML
	public void loadTable(ActionEvent event) throws IOException {
		if(inputSymbols.size() == 0) {
			showWarningAlert(null, null, "Enter at least one input symbol");
		}else if(outputSymbols.size() == 0) {
			showWarningAlert(null, null, "Enter at least one output symbol");
		}else {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("table.fxml"));
			fxmlLoader.setController(this);
			Parent managerPane = fxmlLoader.load();
			createMealyTable();
			mainPanel.setCenter(managerPane);
		}
	}
	
	public void loadFirstScene() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("first-scene.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		cbInputData.getItems().add("Manually");
		cbInputData.getItems().add("From txt");
		cbInputData.setValue("Manually");
		mainPanel.setCenter(managerPane);
	}
	
	public void loadFromText() {
		
	}
	
	public void loadCreateTable() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-table.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		automatonSelected.getItems().add("Mealy");
		automatonSelected.getItems().add("Moore");
		automatonSelected.setValue("Mealy");
		
		colInputSymbols.setCellValueFactory(new PropertyValueFactory<InputSymbol,String>("symbol"));
		tvInputSymbols.setItems(inputSymbols);
		
		colOutputSymbols.setCellValueFactory(new PropertyValueFactory<OutputSymbol,String>("symbol"));
		tvOutputSymbols.setItems(outputSymbols);
		
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
    void backToCreateTable(ActionEvent event) throws IOException {
    	loadCreateTable();
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
    void backToFirstScene(ActionEvent event) throws IOException {
    	loadFirstScene();
    }

    @FXML
    void openFile(ActionEvent event) {

    }
    
    @FXML
    void addStatus(ActionEvent event) {
    	states.add(new State(txStateToAdd.getText(), outputSymbols));
    	txStateToAdd.clear();
    	
		/*
		 * for(int i = 0; i < states.size(); i++) { for(int j = 0; j <
		 * states.get(i).getOutputStates().length; j++) {
		 * System.out.println(states.get(i).getOutputStates()[j].getText()); }
		 * 
		 * }
		 */
    }
    
    @FXML
    void addInputSymbol(ActionEvent event) {
    	String tempSymbol = tfInputSymbol.getText();
    	if(checkSymbolExists(1, tempSymbol)) {
    		showWarningAlert(null, null, "The input symbol " + tempSymbol + " already exists");
    	}else {
    		inputSymbols.add(new InputSymbol(tempSymbol));
    	}
    	tfInputSymbol.clear();
    }

    @FXML
    void addOutputSymbol(ActionEvent event) {
    	String tempSymbol = tfOutputSymbol.getText();
    	if(checkSymbolExists(2, tempSymbol)) {
    		showWarningAlert(null, null, "The input symbol " + tempSymbol + " already exists");
    	}else {
    		outputSymbols.add(new OutputSymbol(tempSymbol));
    	}
    	tfOutputSymbol.clear();
    }
    
    private boolean checkSymbolExists(int typeSymbol, String symbol) {
    	boolean exists = false;
    	switch(typeSymbol) {
	    	case 1:
	    		for(int i = 0; i < inputSymbols.size() && !exists; i++) {
	    			if(inputSymbols.get(i).getSymbol().equals(symbol)) {
	    				exists = true;
	    			}
	    		}
	    		break;
	    	case 2:
	    		for(int i = 0; i < outputSymbols.size() && !exists; i++) {
	    			if(outputSymbols.get(i).getSymbol().equals(symbol)) {
	    				exists = true;
	    			}
	    		}
	    		break;
    	}
    	return exists;
    }
    

    @FXML
    void deleteInputSymbol(ActionEvent event) {
    	if(tvInputSymbols.getSelectionModel().getSelectedItems().size() == 0) {
    		showWarningAlert(null, null, "Select the input symbol you want to delete");
    	}
    	tvInputSymbols.getItems().removeAll(
    			tvInputSymbols.getSelectionModel().getSelectedItems()
        );
    }

    @FXML
    void deleteOutputSymbol(ActionEvent event) {
    	if(tvOutputSymbols.getSelectionModel().getSelectedItems().size() == 0) {
    		showWarningAlert(null, null, "Select the output symbol you want to delete");
    	}
    	tvOutputSymbols.getItems().removeAll(
    			tvOutputSymbols.getSelectionModel().getSelectedItems()
        );
    }
    
    @FXML
    void findMinimalEquivalentAutomata(ActionEvent event) {

    }
    
    public static class InputSymbol{
    	private final SimpleStringProperty symbol;
    	
    	private InputSymbol(String symbol) {
    		this.symbol = new SimpleStringProperty(symbol);
    	}
    	
    	public String getSymbol() {
    		return symbol.get();
    	}
    }
    
    public static class OutputSymbol{
    	private final SimpleStringProperty symbol;
    	
    	private OutputSymbol(String symbol) {
    		this.symbol = new SimpleStringProperty(symbol);
    	}
    	
    	public String getSymbol() {
    		return symbol.get();
    	}
    }
    
    public static class State{
    	private final SimpleStringProperty name;
    	private TextField[] outputStates;
    	private ChoiceBox<String>[] outputSymbols;
    	
    	private State(String name, ObservableList<OutputSymbol> inputSymbols) {
    		int numOfInputSymbols = inputSymbols.size();
    		this.name = new SimpleStringProperty(name);
    		outputStates = new TextField[numOfInputSymbols];
    		outputSymbols = new ChoiceBox[numOfInputSymbols];
			for(int i = 0; i < numOfInputSymbols; i++) { 
				outputStates[i] = new TextField(); 
				ChoiceBox<String> tempOutputCB = new ChoiceBox<>();
				for(int j = 0; j < numOfInputSymbols; j++) {
					tempOutputCB.getItems().add(inputSymbols.get(j).getSymbol());
				}
				outputSymbols[i] = tempOutputCB;
			}
			
			
			
    	}
    	
    	public String getName() {
    		return name.get();
    	}
    	
    	public TextField[] getOutputStates() {
    		return outputStates;
    	}
    	
    	public ChoiceBox<String>[] getOutputSymbols() {
    		return outputSymbols;
    	}
    }
    
    //Retrieve from https://stackoverflow.com/questions/52244810/how-to-fill-tableviews-column-with-a-values-from-an-array
    static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
        if (index < 0) {
            return cd -> null;
        }
        return cd -> {
            T[] array = arrayExtractor.apply(cd.getValue());
            return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
        };
    }
}
