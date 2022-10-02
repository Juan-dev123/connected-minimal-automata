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
import model.Mealy;
import model.Moore;

/**
 * @author Juan-dev123
 *
 */

public class FXController {
	@FXML
	private ChoiceBox<String> automatonSelected;
	
    @FXML
    private BorderPane mainPanel;

    @FXML
    private TextField txStatus;
    
    @FXML
    private Button btnToMealy;

    @FXML
    private Button btnToMoore;
    
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
	
	/**
	 * The constructor method of the FXController. <br>
	 */
	public FXController() {
		List<InputSymbol> listIS = new ArrayList<InputSymbol>();
		inputSymbols = FXCollections.observableArrayList(listIS); 
		List<OutputSymbol> listOS = new ArrayList<OutputSymbol>();
		outputSymbols = FXCollections.observableArrayList(listOS); 
		List<State> listStates = new ArrayList<State>();
		states = FXCollections.observableArrayList(listStates); 
	}

	/**
	 * Creates a mealy table <br>
	 * <b>pre: </b> The table.fxml file is loaded
	 */
	@SuppressWarnings("unchecked")
	public void createMealyTable() {
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
	
	/**
	 * Changes the label of final-table.fxml file <br>
	 * <b>pre: </b> The final-table.fxml file is loaded
	 * @param text The text to which the label is to be changed
	 */
	private void setLabelOfFinalState(String text) {
		lbFinalTable.setText(text);
		lbFinalTable.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(lbFinalTable, 0.0);
		AnchorPane.setRightAnchor(lbFinalTable, 0.0);
		lbFinalTable.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Creates a mealy table with data <br>
	 * <b>pre: </b> There is at least one state, one input symbol, one output symbol and one output state in the list, this means that the size of the list is greater than or equal to 4. The final-table.fxml file is loaded <br>
	 * @param list The data of the mealy table
	 */
	@SuppressWarnings("unchecked")
	public void createMealyTableWithData(List<List<String>> list) {
		setLabelOfFinalState("Mealy Automaton");
		List<TableColumn<State, String>> tables = new ArrayList<>();
		TableColumn<State, String> tempColumn = new TableColumn<State, String>("State");
		tempColumn.setCellValueFactory(createArrayValueFactory(State::getData, 0));
		tables.add(tempColumn);
		
		for(int cont = 0, i = 1; cont < inputSymbols.size(); cont++, i += 2) {
			tables.add(new TableColumn<State, String>(inputSymbols.get(cont).getSymbol()));
			TableColumn<State, String> tempState = new TableColumn<State, String>("State");
			tempState.setCellValueFactory(createArrayValueFactory(State::getData, i));
			TableColumn<State, String> tempOutput = new TableColumn<State, String>("Output");
			tempOutput.setCellValueFactory(createArrayValueFactory(State::getData, i+1));
			tables.get(cont+1).getColumns().addAll(tempState, tempOutput);
		}
		
		for(int i = 0; i < list.size(); i++) {
			for(int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j));
			}
			System.out.println();
		}
		System.out.println();
		
		
		List<State> listStates = new ArrayList<State>();
		states = FXCollections.observableArrayList(listStates);
		for(int i = 0; i < list.size(); i++) {
			String[] row = new String[(inputSymbols.size()*3)+1];
			row[0] = list.get(i).get(0);
			for(int j = 3, k = 1; j < list.get(i).size(); j += 3, k += 2) {
				row[k] = list.get(i).get(j);
				row[k+1] = list.get(i).get(j-1);
			}
			states.add(new State(inputSymbols.size(), row));
		}
		
		ObservableList<TableColumn<State, String>> observableList = FXCollections.observableArrayList(tables);
		tvFinalTable.getColumns().addAll(observableList);
		tvFinalTable.setItems(states);
		
	}
	
	/**
	 * Creates a moore table with data <br>
	 * <b>pre: </b> There is at least one state, one input symbol, one output state and one output symbol in the list, this means that the size of the list is greater than or equal to 4. The final-table.fxml file is loaded <br>
	 * @param list The data of the moore table
	 */
	@SuppressWarnings("unchecked")
	public void createMooreTableWithData(List<List<String>> list) {
		setLabelOfFinalState("Moore Automaton");
		
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
	
	/**
	 * Creates a moore table <br>
	 * <b>pre: </b> The table.fxml file is loaded
	 */
	@SuppressWarnings("unchecked")
	public void createMooreTable() {
		btnAddMooreState.setDisable(false);
		btnAddMooreState.setVisible(true);
		btnFindMooreAutomata.setDisable(false);
		btnFindMooreAutomata.setVisible(true);
		lbTableTitle.setText("Moore Automaton");
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
	
	/**
	 * Loads the table.fxml file <br>
	 * @param event The event
	 * @throws IOException if the fxml file can't be loaded
	 */
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
	
	/**
	 * Loads the final.table.fxml file <br>
	 * @param typeAutomaton The type of automaton
	 * @param data The data of the automaton
	 * @throws IOException if the fxml file can't be loaded
	 */
	public void loadFinalTable(int typeAutomaton, List<List<String>> data) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("final-table.fxml"));
		fxmlLoader.setController(this);
		Parent managerPane = fxmlLoader.load();
		switch(typeAutomaton) {
		case 1:
			createMealyTableWithData(data);
			break;
		case 2:
			createMooreTableWithData(data);
			break;
		}
		mainPanel.setCenter(managerPane);
	}
	
	/**
	 * Loads the create-table.fxml file <br>
	 * @throws IOException if the fxml file can't be loaded
	 */
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
	 * Shows an error alert <br>
	 * @param title The title of the alert
	 * @param header The header of the alert
	 * @param content The content of the alert
	 */
	public void showErrorAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Shows a warning alert <br>
	 * @param title The title of the warning
	 * @param header The header of the warning
	 * @param content The content of the warning
	 */
	public void showWarningAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Shows an information alert
	 * @param title The title of the information alert
	 * @param header The header of the information alert
	 * @param content The content of the information alert
	 */
	public void showInformationAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Deletes the states and loads the create-table.fxml file <br>
	 * @param event The event
	 * @throws IOException if the fxml file can't be loaded
	 */
    @FXML
    void backToCreateTable(ActionEvent event) throws IOException {
    	List<State> listStates = new ArrayList<State>();
    	states = FXCollections.observableArrayList(listStates); 
    	loadCreateTable();
    }
    
    /**
     * Adds a mealy state <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param event The event
     */
    @FXML
    void addMealyState(ActionEvent event) {
    	addState(1);
    }

    /**
     * Adds a moore state <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param event The event
     */
    @FXML
    void addMoreState(ActionEvent event) {
    	addState(2);
    }
    
    /**
     * Adds a state <br>
     * @param typeOfAutomaton The type of automaton
     */
    private void addState(int typeOfAutomaton) {
    	String tempState = txStateToAdd.getText().toUpperCase();
    	if(isEmpty(tempState)) {
    		showWarningAlert(null, null, "The text field is empty. Enter one state");
    	}else if(checkIfStateExists(tempState) != -1) {
    		showWarningAlert(null, null, "The state with name " + tempState + " already exists");
    	}else {
    		switch(typeOfAutomaton) {
    			case 1: //Mealy automaton
    				states.add(new State(true, tempState, inputSymbols.size(), outputSymbols));
    				break;
    			case 2: //Moore automaton
    				states.add(new State(false, tempState, inputSymbols.size(), outputSymbols));
    				break;
    		}
    		
    		cbInitialState.getItems().add(tempState);
    	}
    	txStateToAdd.clear();
    }
    
    /**
     * Checks if one state exists in the list of states <br>
     * @param nameOfState The name of the state
     * @return -1 if the state does not exists. The index of the state if the state exists 
     */
    private int checkIfStateExists(String nameOfState) {
    	int index = -1;
    	for(int i = 0; i < states.size() && index == -1; i++) {
    		if(states.get(i).getName().equalsIgnoreCase(nameOfState)) {
    			index = i;
    		}
    	}
    	return index;
    }
    
    /**
     * Adds an input symbol <br>
     * <b>pre: </b> The create-table.fxml file is loaded <br>
     * @param event The event
     */
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

    /**
     * Adds an output symbol <br>
     * <b>pre: </b> The create-table.fxml file is loaded <br>
     * @param event The event
     */
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
    
    /**
     * Checks if a symbol exists in the list of symbols <br>
     * @param typeSymbol The type of symbol
     * @param symbol The symbol
     * @return True if it exists. False if it does not
     */
    private boolean checkIfSymbolExists(int typeSymbol, String symbol) {
    	boolean exists = false;
    	switch(typeSymbol) {
	    	case 1: //input symbol
	    		for(int i = 0; i < inputSymbols.size() && !exists; i++) {
	    			if(inputSymbols.get(i).getSymbol().equals(symbol)) {
	    				exists = true;
	    			}
	    		}
	    		break;
	    	case 2: //output symbol
	    		for(int i = 0; i < outputSymbols.size() && !exists; i++) {
	    			if(outputSymbols.get(i).getSymbol().equals(symbol)) {
	    				exists = true;
	    			}
	    		}
	    		break;
    	}
    	return exists;
    }
    
    /**
     * Checks if a string is empty <br>
     * @param tf The string
     * @return True if it is empty. False if it does not
     */
    private boolean isEmpty(String tf) {
    	boolean isEmpty = false;
    	if(tf.equals("")) {
    		isEmpty = true;
    	}
    	return isEmpty;
    }
    
    /**
     * Deletes one state <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param event The event
     */
    @FXML
    void deleteState(ActionEvent event) {
    	if(tvAutomatonTable.getSelectionModel().getSelectedItems().size() == 0) {
    		showWarningAlert(null, null, "Select the state you want to delete");
    	}else {
        	State stateToRemove = tvAutomatonTable.getSelectionModel().getSelectedItems().get(0);
        	cbInitialState.setValue(null);
        	cbInitialState.getItems().remove(stateToRemove.getName());
        	
        	tvAutomatonTable.getItems().removeAll(
        			tvAutomatonTable.getSelectionModel().getSelectedItems()
            );
    	}
    }

    /**
     * Deletes one input symbol <br>
     * <b>pre: </b> The create-table.fxml file is loaded <br>
     * @param event The event
     */
    @FXML
    void deleteInputSymbol(ActionEvent event) {
    	if(tvInputSymbols.getSelectionModel().getSelectedItems().size() == 0) {
    		showWarningAlert(null, null, "Select the input symbol you want to delete");
    	}else {
        	tvInputSymbols.getItems().removeAll(
        			tvInputSymbols.getSelectionModel().getSelectedItems()
            );
    	}
    }

    /**
     * Deletes one output symbol <br>
     * <b>pre: </b> The create-table.fxml file is loaded <br>
     * @param event The event
     */
    @FXML
    void deleteOutputSymbol(ActionEvent event) {
    	if(tvOutputSymbols.getSelectionModel().getSelectedItems().size() == 0) {
    		showWarningAlert(null, null, "Select the output symbol you want to delete");
    	}else {
        	tvOutputSymbols.getItems().removeAll(
        			tvOutputSymbols.getSelectionModel().getSelectedItems()
            );
    	}
    }
    

    /**
     * Finds the connected and minimal equivalent mealy automata <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param event The event
     * @throws IOException if the final-table.fxml file can't be loaded
     */
    @FXML
    void findMealyAutomata(ActionEvent event) throws IOException {
    	findMinimalEquivalentAutomata(1);
    }

    /**
     * Finds the connected and minimal equivalent moore automata <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param event The event
     * @throws IOException if the final-table.fxml file can't be loaded
     */
    @FXML
    void findMooreAutomata(ActionEvent event) throws IOException {
    	findMinimalEquivalentAutomata(2);
    }
    
    
    /**
     * Finds the connected and minimal equivalent finite state automata <br>
     * <b>pre: </b> The table.fxml file is loaded <br>
     * @param typeAutomaton The type of automaton
     * @throws IOException if the final-table.fxml file can't be loaded
     */
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
        			case 1: //mealy automaton
        				String[][] dataForMealy = createDataForMealy();
        				automaton = new Mealy(allStates, allInputSymbols, allOutputSymbols, dataForMealy);
        				List<List<String>> reducedAutomatonMe = automaton.reduceAutomaton();
        				loadFinalTable(1, reducedAutomatonMe);
        				break;
        			case 2: //moore automaton
        				String[][] dataForMoore = createDataForMoore();
        				automaton = new Moore(allStates, allInputSymbols, allOutputSymbols, dataForMoore);
        				List<List<String>> reducedAutomatonMo = automaton.reduceAutomaton();
        				loadFinalTable(2, reducedAutomatonMo);
        				break;
        		}
        	}
    	}
    	
    }
    
    /**
     * Reorganizes the states so that the first state in the list is the initial state <br>
     * <b>pre: </b> The initialState has to exist in the state list <br>
     * @param initialState The initial states
     */
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
    
    /**
     * Gets all the names of the states <br>
     * @return An array with the names of the states
     */
    private String[] getStates() {
    	String[] allStates = new String[states.size()];
    	for(int i = 0; i < allStates.length; i++) {
    		allStates[i] = states.get(i).getName();
    	}
    	return allStates;
    }
    
    /**
     * Gets all the input symbols <br>
     * @return An array with the input symbols
     */
    private String[] getInputSymbols() {
    	String[] allInputSymbols = new String[inputSymbols.size()];
    	for(int i = 0; i < allInputSymbols.length; i++) {
    		allInputSymbols[i] = inputSymbols.get(i).getSymbol();
    	}
    	return allInputSymbols;
    }
    
    /**
     * Gets all the output symbols
     * @return An array with the output symbols
     */
    private String[] getOutputSymbols() {
    	String[] allOutputSymbols = new String[outputSymbols.size()];
    	for(int i = 0; i < allOutputSymbols.length; i++) {
    		allOutputSymbols[i] = outputSymbols.get(i).getSymbol();
    	}
    	return allOutputSymbols;
    }
    
    /**
     * Creates a matrix with the data representing a moore automaton <br>
     * <b>pre: </b> All the states in the states parameter are type moore
     * @return A matrix with the data representing a moore automaton
     */
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
    
    /**
     * Creates a matrix with the data representing a mealy automaton <br>
     * <b>pre: </b> All the states in the states parameter are type mealy
     * @return A matrix with the data representing a mealy automato
     */
    private String[][] createDataForMealy(){
    	String[][] data = new String[states.size()][(inputSymbols.size()*3)+1];
    	for(int i = 0; i < states.size(); i++) {
    		State tempState = states.get(i);
    		String[] row = new String[(inputSymbols.size()*3)+1];
    		row[0] = String.valueOf(i);;
    		//Fill the array with the input symbols, output symbols and states 
    		for(int j = 1, k = 0; j < row.length; j = j+3, k++) {
    			//Input symbol
    			row[j] = inputSymbols.get(k).getSymbol();
    			//Output symbol
    			row[j+1] = tempState.getOutputSymbols()[k].getValue();
    			//State
    			row[j+2] = String.valueOf(checkIfStateExists(tempState.getOutputStates()[k].getText()));
    		}
    		data[i] = row;
    	}
    	return data;
    }

    /**
     * Shows the states that are inaccessible and the new names of the partitions
     * <b>pre: </b> The final-table.fxml file is loaded
     * @param event The event
     */
    @FXML
    void showAdditionalInfo(ActionEvent event) {
    	showInformationAlert(null, null, automaton.getInfo());
    }
    
    /**
     * Checks that all text fields are fill and all the states entered in the text fields exist
     * @return A message
     */
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
    
    /**
     * @author Juan-dev123
     *
     */
    public static class InputSymbol{
    	private final SimpleStringProperty symbol;
    	
    	/**
    	 * The constructor method of the InputSymbol. <br>
    	 * @param symbol The input symbol
    	 */
    	private InputSymbol(String symbol) {
    		this.symbol = new SimpleStringProperty(symbol);
    	}
    	
    	/**
    	 * @return The input symbol
    	 */
    	public String getSymbol() {
    		return symbol.get();
    	}
    }
    
    /**
     * @author Juan-dev123
     *
     */
    public static class OutputSymbol{
    	private final SimpleStringProperty symbol;
    	
    	/**
    	 * The constructor method of the OutputSymbol. <br>
    	 * @param symbol The output symbol
    	 */
    	private OutputSymbol(String symbol) {
    		this.symbol = new SimpleStringProperty(symbol);
    	}
    	
    	/**
    	 * @return The output symbol
    	 */
    	public String getSymbol() {
    		return symbol.get();
    	}
    }
    
    /**
     * @author Juan-dev123
     *
     */
    public static class State{
    	private SimpleStringProperty name;
    	private TextField[] outputStates;
    	private ChoiceBox<String>[] outputSymbols;
    	private String[] data;
    	
    	/**
    	 * The constructor method of the State. It is used when states are to be entered manually <br>
    	 * @param isMealy If the state is a mealy state
    	 * @param name The name of the state
    	 * @param numOfInputSymbols The number of input symbols
    	 * @param outputSymbolsP The number of output symbols
    	 */
    	@SuppressWarnings("unchecked")
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
    	
    	/**
    	 * The constructor method of the State. It is used when states are to be entered automatically <br>
    	 * @param numOfInputSymbols The numer of input symbols
    	 * @param dataP The data of the state
    	 */
    	private State(int numOfInputSymbols, String[] dataP) {
			data = new String[numOfInputSymbols+2];
			data = dataP;
    	}
    	
    	/**
    	 * @return The data of the state
    	 */
    	public String[] getData() {
    		return data;
    	}
    	
    	/**
    	 * @return The name of the state
    	 */
    	public String getName() {
    		return name.get();
    	}
    	
    	/**
    	 * @return The output states of the state
    	 */
    	public TextField[] getOutputStates() {
    		return outputStates;
    	}
    	
    	/**
    	 * @return The output symbols of the state
    	 */
    	public ChoiceBox<String>[] getOutputSymbols() {
    		return outputSymbols;
    	}
    	
    	/**
    	 * Disables all the text fields and choice box <br>
    	 */
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
    /**
     * Creates a SimpleObjectProperty for each of the elements of an array <br>
     * @param <S> The class that has the array as an attribute
     * @param <T> The class of the array
     * @param arrayExtractor The method of the class S that returns the array type T
     * @param index the index of the value to which you want to create the SimpleObjectproperty
     * @return Null if the index is greater than the length of the array or the array is null. The SimpleObjectProperty if the index is less than or equal to the length of the array
     */
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
