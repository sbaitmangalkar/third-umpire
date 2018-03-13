package com.voter.info.app;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;
import com.voter.info.service.VoterService;
import com.voter.info.service.VoterServiceImpl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ThirdUmpireApp extends Application {
	private VoterService voterService;
	private List<String> allDistricts;
	private Map<String, List<String>> districtsWithAssemblyConstituencies;
	
	private void initialize() {
		voterService = new VoterServiceImpl();
		districtsWithAssemblyConstituencies = voterService.getDistrictsWithAssemblyConstituencies();
		allDistricts = districtsWithAssemblyConstituencies.entrySet()
				                                          .stream()
				                                          .map(entry -> entry.getKey())
				                                          .collect(Collectors.toList());       
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Third Umpire");
		GridPane grid = new GridPane();
		initialize();
		TableView<Voter> table = new TableView<>();
		Scene scene = new Scene(new Group(), 730, 630);
		
		TextField firstNameTextField = new TextField();
		firstNameTextField.setPromptText("First Name");
		
		
		TextField middleNameTextField = new TextField();
		middleNameTextField.setPromptText("Middle Name");
		
		TextField lastNameTextField = new TextField();
		lastNameTextField.setPromptText("Last Name");
		
		Button searchButton = new Button("Search");
		searchButton.setDisable(true);
		
		ObservableList<String> districtOptions = FXCollections.observableArrayList(allDistricts);
		districtOptions.sort(new DistrictAndAssemblyConstituencySorter());
		ComboBox<String> districtComboBox = new ComboBox<>(districtOptions);
		ComboBox<String> assemblyConstituencyComboBox = new ComboBox();
		ProgressIndicator progressIndicator = new ProgressIndicator();
		
		districtComboBox.setOnAction(value -> {
			String selectedDistrict = (String)districtComboBox.getValue();
			
			if(selectedDistrict != null && selectedDistrict.length() > 1) {
				ObservableList<String> assemblyConstituencyOptions = FXCollections.observableArrayList(getAllAssemblyConstituencies(selectedDistrict));
				
				assemblyConstituencyOptions.add("All");
				assemblyConstituencyOptions.sort(new DistrictAndAssemblyConstituencySorter());
				
				assemblyConstituencyComboBox.setItems(assemblyConstituencyOptions);
				assemblyConstituencyComboBox.setVisible(true);
				searchButton.setDisable(false);
			}
		});
		
		
		
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		grid.add(new Label("First Name:"), 0, 0);
		grid.add(firstNameTextField, 1, 0);
		grid.add(new Label("Middle Name:"), 0, 1);
		grid.add(middleNameTextField, 1, 1);
		grid.add(new Label("Last Name:"), 0, 2);
		grid.add(lastNameTextField, 1, 2);
		
		grid.add(new Label("Districts:"), 0, 3);
		grid.add(districtComboBox, 1, 3);
		grid.add(new Label("Assembly Constituencies:"), 0, 4);
		grid.add(assemblyConstituencyComboBox, 1, 4);
		assemblyConstituencyComboBox.setVisible(false);
		
		grid.add(searchButton, 2, 4);
		grid.add(progressIndicator, 2, 5);
		progressIndicator.setVisible(false);
		
		table.setEditable(false);
		TableColumn fullName = new TableColumn("Full Name");
		TableColumn dependent = new TableColumn("Dependent");
		TableColumn dependentName = new TableColumn("Dependent Name");
		TableColumn address = new TableColumn("Address");
		TableColumn age = new TableColumn("Age");
		TableColumn sex = new TableColumn("Sex");
		
		table.getColumns().addAll(fullName, dependent, dependentName, address, age, sex);
		grid.add(table, 1, 7);
		
		Group root = (Group)scene.getRoot();
		root.getChildren().add(grid);
		
		searchButton.setOnAction(value -> {
			progressIndicator.setVisible(true);
			String firstName = firstNameTextField.getText();
			String middleName = middleNameTextField.getText();
			String lastName = lastNameTextField.getText();
			String districtName = districtComboBox.getValue();
			String assemblyConstituencyName = assemblyConstituencyComboBox.getValue();
			
			UserRequest request = new UserRequest();
			request.setFirstName(firstName);
			request.setMiddleName(middleName);
			request.setLastName(lastName);
			request.setDistrict(districtName);
			request.setAssemblyConstituencyName(assemblyConstituencyName);
			
			List<Voter> voters = voterService.findVoter(request);
			
			ObservableList<Voter> data = FXCollections.observableArrayList(voters);
			fullName.setCellValueFactory(new PropertyValueFactory<Voter, String>("fullName"));
			dependent.setCellValueFactory(new PropertyValueFactory<Voter, String>("dependent"));
			dependentName.setCellValueFactory(new PropertyValueFactory<Voter, String>("dependentName"));
			address.setCellValueFactory(new PropertyValueFactory<Voter, String>("address"));
			age.setCellValueFactory(new PropertyValueFactory<Voter, Integer>("age"));
			sex.setCellValueFactory(new PropertyValueFactory<Voter, String>("sex"));
			
			table.setItems(data);
			progressIndicator.setVisible(false);
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * @param districtName
	 * @return
	 */
	private List<String> getAllAssemblyConstituencies(String districtName) {
		return districtsWithAssemblyConstituencies.get(districtName);
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
