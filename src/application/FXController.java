package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.FiniteStateAutomaton;
import model.Moore;

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

    @FXML
    private ChoiceBox<String> cbInitialState;
    
    @FXML
    private Button btnAddMealyState;

    @FXML
    private Button btnAddMooreState;
    
    @FXML
    private Button btnFindMealyAutomata;

    @FXML
    private Button btnFindMooreAutomata;
    
    @FXML
    private Label lbFinalTable;
    
    @FXML
    private TableView<State> tvFinalTable;
    
    private ObservableList<InputSymbol> inputSymbols;
    
    private ObservableList<OutputSymbol> outputSymbols;
    
    private ObservableList<State> states;
    
    private FiniteStateAutomaton automaton;
	
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

	public void createMealyTable() throws IOException {
		btnAddMealyState.setDisable(false);
		btnAddMealyState.setVisible(true);
		btnFindMealyAutomata.setDisable(false);
		btnFindMealyAutomata.setVisible(true);
		lbTableTitle.setText("Mealy Automaton");
		lbTableTitle.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(lbTableTitle, 0.0);
		AnchorPane.setRightAnchor(lbTableTitle, 0.0);
		lbTableTitle.setAlignment(Pos.CENTER);
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
		
		ObservableList<TableColumn<State, String>> observableList = FXCollections.observableArrayList(tables);
        
        tvAutomatonTable.getColumns().addAll(observableList);
        tvAutomatonTable.setItems(states);
	}
	
	public void createMooreTableWithData(List<List<String>> list) {
		lbFinalTable.setText("Moore Automaton");
		lbFinalTable.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(lbFinalTable, 0.0);
		AnchorPane.setRightAnchor(lbFinalTable, 0.0);
		lbFinalTable.setAlignment(Pos.CENTER);
		
		List<TableColumn<State, String>> tables = new ArrayList<>();
		TableColumn<State, String> tempColumn = new TableColumn<State, String>("State");
		tempColumn.setCellValueFactory(createArrayValueFactory(State::getData, 0));
		tables.add(tempColumn);
		
		int cont;
		for(cont = 0; cont < inputSymbols.size(); cont++) {
			tables.add(new TableColumn<State, String>(inputSymbols.get(cont).getSymbol()));
			TableColumn<State, String> tempState = new TableColumn<State, String>("State");
			tempState.setCellValueFactory(createArrayValueFactory(State::getData, cont+1));
			tables.get(cont+1).getColumns().addAll(tempState);
		}
		
		tables.add(new TableColumn<State, String>(""));
		TableColumn<State, String> tempOutput = new TableColumn<State, String>("Output");
		tempOutput.setCellValueFactory(createArrayValueFactory(State::getData, cont+1));
		tables.get(tables.size()-1).getColumns().addAll(tempOutput);
		
		List<State> listStates = new ArrayList<State>();
		states = FXCollections.observableArrayList(listStates);
		for(int i = 0; i < list.size(); i++) {
			String[] row = new String[inputSymbols.size()+2];
			for(int j = 0, k = 0; j < list.get(0).size(); j+=2, k++) {
				row[k] = list.get(i).get(j);
			}	
			row[row.length-1] = list.get(i).get(list.get(i).size()-1);
			states.add(new State(inputSymbols.size(), row));
		}
		
		ObservableList<TableColumn<State, String>> observableList = FXCollections.observableArrayList(tables);
		tvFinalTable.getColumns().addAll(observableList);
		tvFinalTable.setItems(states);
		
	}
	
	private void organizeSceneForMoore() {
		btnAddMooreState.setDisable(false);
		btnAddMooreState.setVisible(true);
		btnFindMooreAutomata.setDisable(false);
		btnFindMooreAutomata.setVisible(true);
		lbTableTitle.setText("Moore Automaton");
		lbTableTitle.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(lbTableTitle, 0.0);
		AnchorPane.setRightAnchor(lbTableTitle, 0.0);
		lbTableTitle.setAlignment(Pos.CENTER);
	}
	
	public void createMooreTable() {
		organizeSceneForMoore();
		List<TableColumn<State, String>> tables = new ArrayList<>();
		tables.add(new TableColumn<State, String>("State"));
		tables.get(0).setCellValueFactory(new PropertyValueFactory<State,String>("name"));
		
		for(int i = 0; i < inputSymbols.size(); i++) {
			tables.add(new TableColumn<State, String>(inputSymbols.get(i).getSymbol()));
			TableColumn<State, TextField> tempState = new TableColumn<State, TextField>("State");
			tempState.setCellValueFactory(createArrayValueFactory(State::getOutputStates, i));
			tables.get(i+1).getColumns().addAll(tempState);
		}
		
		tables.add(new TableColumn<State, String>(""));
		TableColumn<State, ChoiceBox<String>> tempOutput = new TableColumn<State, ChoiceBox<String>>("Output");
		tempOutput.setCellValueFactory(createArrayValueFactory(State::getOutputSymbols, 0));
		tables.get(tables.size()-1).getColumns().addAll(tempOutput);
		
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
			switch(automatonSelected.getValue()) {
				case "Mealy":
					createMealyTable();
					break;
				case "Moore":
					createMooreTable();
					break;
			}
			mainPanel.setCenter(managerPane);
		}
	}
	
	public void loadFinalTable(int typeAutomaton, List<List<String>> data) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("final-table.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		switch(typeAutomaton) {
		case 1:
			//createMealyTable();
			break;
		case 2:
			createMooreTableWithData(data);
			break;
		}
		mainPanel.setCenter(managerPane);
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
    	List<State> listStates = new ArrayList<State>();
    	states = FXCollections.observableArrayList(listStates); 
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
    void addMealyState(ActionEvent event) {
    	addState(1);
    }

    @FXML
    void addMoreState(ActionEvent event) {
    	addState(2);
    }
    
    private void addState(int typeOfAutomaton) {
    	String tempState = txStateToAdd.getText().toUpperCase();
    	if(isEmpty(tempState)) {
    		showWarningAlert(null, null, "The text field is empty. Enter one state");
    	}else if(checkIfStateExists(tempState) != -1) {
    		showWarningAlert(null, null, "The state with name " + tempState + " already exists");
    	}else {
    		switch(typeOfAutomaton) {
    			case 1:
    				states.add(new State(true, tempState, inputSymbols.size(), outputSymbols));
    				break;
    			case 2:
    				states.add(new State(false, tempState, inputSymbols.size(), outputSymbols));
    				break;
    		}
    		
    		cbInitialState.getItems().add(tempState);
    	}
    	txStateToAdd.clear();
    }
    
    private int checkIfStateExists(String nameOfState) {
    	int index = -1;
    	for(int i = 0; i < states.size() && index == -1; i++) {
    		if(states.get(i).getName().equalsIgnoreCase(nameOfState)) {
    			index = i;
    		}
    	}
    	return index;
    }
    
    @FXML
    void addInputSymbol(ActionEvent event) {
    	String tempSymbol = tfInputSymbol.getText();
    	if(isEmpty(tempSymbol)) {
    		showWarningAlert(null, null, "The text field is empty. Enter one input symbol");
    	}else if(checkIfSymbolExists(1, tempSymbol)) {
    		showWarningAlert(null, null, "The input symbol " + tempSymbol + " already exists");
    	}else {
    		inputSymbols.add(new InputSymbol(tempSymbol));
    	}
    	tfInputSymbol.clear();
    }

    @FXML
    void addOutputSymbol(ActionEvent event) {
    	String tempSymbol = tfOutputSymbol.getText();
    	if(isEmpty(tempSymbol)) {
    		showWarningAlert(null, null, "The text field is empty. Enter one output symbol");
    	}else if(checkIfSymbolExists(2, tempSymbol)) {
    		showWarningAlert(null, null, "The input symbol " + tempSymbol + " already exists");
    	}else {
    		outputSymbols.add(new OutputSymbol(tempSymbol));
    	}
    	tfOutputSymbol.clear();
    }
    
    private boolean checkIfSymbolExists(int typeSymbol, String symbol) {
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
    
    private boolean isEmpty(String tf) {
    	boolean isEmpty = false;
    	if(tf.equals("")) {
    		isEmpty = true;
    	}
    	return isEmpty;
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
    void findMealyAutomata(ActionEvent event) throws IOException {
    	findMinimalEquivalentAutomata(1);
    }

    @FXML
    void findMooreAutomata(ActionEvent event) throws IOException {
    	findMinimalEquivalentAutomata(2);
    }
    
    void findMinimalEquivalentAutomata(int typeAutomaton) throws IOException {
    	if(states.size() == 0) {
    		showWarningAlert(null, null, "Enter at least one state");
    	}else if(cbInitialState.getValue() == null) {
    		showWarningAlert(null, null, "Enter the initial state");
    	}else {
    		String msg = checkOutputStates();
        	if(msg != null) {
        		showWarningAlert(null, null, msg);
        	}else {
        		reorganizeState(cbInitialState.getValue());
        		String[] allStates = getStates();
        		String[] allInputSymbols = getInputSymbols();
        		String[] allOutputSymbols = getOutputSymbols();
        		switch(typeAutomaton) {
        			case 1:
        				break;
        			case 2:
        				String[][] dataForMoore = createDataForMoore();
        				automaton = new Moore(allStates, allInputSymbols, allOutputSymbols, dataForMoore);
        				List<List<String>> reducedAutomaton = automaton.reduceAutomaton();
        				loadFinalTable(2, reducedAutomaton);
        				break;
        		}
        	}
    	}
    	
    }
    
    private void reorganizeState(String initialState) {
    	boolean stop = false;
    	for(int i = 0; i < states.size() && !stop; i++) {
    		if(states.get(i).getName().equals(initialState)) {
    			State tempState = states.get(0);
    			states.set(0, states.get(i));
    			states.set(i, tempState);
    		} 
    	}
    }
    
    private String[] getStates() {
    	String[] allStates = new String[states.size()];
    	for(int i = 0; i < allStates.length; i++) {
    		allStates[i] = states.get(i).getName();
    	}
    	return allStates;
    }
    
    private String[] getInputSymbols() {
    	String[] allInputSymbols = new String[inputSymbols.size()];
    	for(int i = 0; i < allInputSymbols.length; i++) {
    		allInputSymbols[i] = inputSymbols.get(i).getSymbol();
    	}
    	return allInputSymbols;
    }
    
    private String[] getOutputSymbols() {
    	String[] allOutputSymbols = new String[outputSymbols.size()];
    	for(int i = 0; i < allOutputSymbols.length; i++) {
    		allOutputSymbols[i] = outputSymbols.get(i).getSymbol();
    	}
    	return allOutputSymbols;
    }
    
    private String[][] createDataForMoore(){
    	String[][] data = new String[states.size()][(inputSymbols.size()*2)+2];
    	for(int i = 0; i < states.size(); i++) {
    		State tempState = states.get(i);
    		String[] inputSymAndSta = new String[inputSymbols.size()*2];
    		
    		//Fill the array with the states and their respective outputs
    		for(int j = 0, k = 0; j < inputSymAndSta.length; j=j+2, k++) {
    			//Input symbol
    			inputSymAndSta[j] = inputSymbols.get(k).getSymbol();
    			//State
    			inputSymAndSta[j+1] = String.valueOf(checkIfStateExists(tempState.getOutputStates()[k].getText()));
    		}
    		String[] row = new String[(inputSymbols.size()*2)+2];
    		row[0] = String.valueOf(i);
    		for(int j = 1; j <= inputSymAndSta.length; j++) {
    			row[j] = inputSymAndSta[j-1];
    		}
    		row[row.length-1] = tempState.getOutputSymbols()[0].getValue();
    		data[i] = row;
    	}
    	return data;
    }

    @FXML
    void showAdditionalInfo(ActionEvent event) {
    	showInformationAlert(null, null, automaton.getInfo());
    }
    
    private String checkOutputStates() {
    	String msg = null;
    	boolean stop = false;
    	for(int i = 0; i < states.size() && !stop; i++) {
    		TextField[] outputStates = states.get(i).getOutputStates();
    		for(int j = 0; j < outputStates.length && !stop; j++) {
    			if(outputStates[j].getText().equals("")) {
    				stop = true;
    				msg = "There is at least one empty text field. Please fill them all";
    			}else {
    				boolean find = false;
        			for(int k = 0; k < states.size() && !find; k++) {
        				if(outputStates[j].getText().toUpperCase().equals(states.get(k).getName())) {
        					find = true;
        				}
        			}
        			if(!find) {
        				msg = "The state " + outputStates[j].getText().toUpperCase() + " does not exist";
        				stop = true;
        			}
    			}
    		}
    	}
    	return msg;
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
    	private SimpleStringProperty name;
    	private TextField[] outputStates;
    	private ChoiceBox<String>[] outputSymbols;
    	private String[] data;
    	
    	private State(boolean isMealy, String name, int numOfInputSymbols, ObservableList<OutputSymbol> outputSymbolsP) {
    		this.name = new SimpleStringProperty(name);
    		outputStates = new TextField[numOfInputSymbols];
    		if(isMealy) {
    			outputSymbols = new ChoiceBox[numOfInputSymbols];
    		}else {
    			outputSymbols = new ChoiceBox[1];
    		}
    		
			for(int i = 0; i < numOfInputSymbols; i++) { 
				outputStates[i] = new TextField(); 
			}
			for(int i = 0; i < outputSymbols.length; i++) {
				ChoiceBox<String> tempOutputCB = new ChoiceBox<>();
				for(int j = 0; j < outputSymbolsP.size(); j++) {
					tempOutputCB.getItems().add(outputSymbolsP.get(j).getSymbol());
				}
				tempOutputCB.setValue(outputSymbolsP.get(0).getSymbol());
				outputSymbols[i] = tempOutputCB;
			}
    	}
    	
    	private State(int numOfInputSymbols, String[] dataP) {
			data = new String[numOfInputSymbols+2];
			data = dataP;
    	}
    	
    	public String[] getData() {
    		return data;
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
    	
    	public void disableAll() {
    		for(int i = 0; i < outputStates.length; i++) { 
				outputStates[i].setEditable(false);
				outputStates[i].setDisable(true);
			}
    		for(int i = 0; i < outputSymbols.length; i++) { 
    			outputSymbols[i].setDisable(true);
			}
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
