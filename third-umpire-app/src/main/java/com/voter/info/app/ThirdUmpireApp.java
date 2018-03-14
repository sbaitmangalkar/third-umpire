package com.voter.info.app;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;
import com.voter.info.service.VoterService;
import com.voter.info.service.VoterServiceImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ThirdUmpireApp extends Application {
	private VoterService voterService;
	private List<String> allDistricts;
	private Map<String, List<String>> districtsWithAssemblyConstituencies;
	
	private TableView<Voter> table;
	private TextField firstNameTextField;
	private TextField middleNameTextField;
	private TextField lastNameTextField;
	private ComboBox<String> districtComboBox;
	private ComboBox<String> assemblyConstituencyComboBox;
	private ProgressIndicator progressIndicator;
	
	private TableColumn<Voter, String> fullName;
	private TableColumn<Voter, String> dependent;
	private TableColumn<Voter, String> dependentName;
	private TableColumn<Voter, String> address;
	private TableColumn<Voter, Integer> age;
	private TableColumn<Voter, String> sex;
	
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
		primaryStage.getIcons().add(new Image(ThirdUmpireApp.class.getResourceAsStream("/third-umpire.jpg")));
		GridPane grid = new GridPane();
		
		initialize();
		
		table = new TableView<>();
		Scene scene = new Scene(new Group(), 720, 650);
		
		Label firstNameLabel = new Label("First Name*:");
		firstNameLabel.setTextFill(Color.FIREBRICK);
		firstNameTextField = new TextField();
		firstNameTextField.setPromptText("First Name");
		
		middleNameTextField = new TextField();
		middleNameTextField.setPromptText("Middle Name");
		
		Label lastNameLabel = new Label("Last Name*:");
		lastNameLabel.setTextFill(Color.FIREBRICK);
		lastNameTextField = new TextField();
		lastNameTextField.setPromptText("Last Name");
		
		Button searchButton = new Button("Search");
		searchButton.setDisable(true);
		
		ObservableList<String> districtOptions = FXCollections.observableArrayList(allDistricts);
		districtOptions.sort(new DistrictAndAssemblyConstituencySorter());
		districtComboBox = new ComboBox<>(districtOptions);
		assemblyConstituencyComboBox = new ComboBox();
		progressIndicator = new ProgressIndicator();
		
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
		
		grid.add(firstNameLabel, 0, 0);
		grid.add(firstNameTextField, 1, 0);
		grid.add(new Label("Middle Name:"), 0, 1);
		grid.add(middleNameTextField, 1, 1);
		grid.add(lastNameLabel, 0, 2);
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
		fullName = new TableColumn("Full Name");
		dependent = new TableColumn("Dependent");
		dependentName = new TableColumn("Dependent Name");
		address = new TableColumn("Address");
		age = new TableColumn("Age");
		sex = new TableColumn("Sex");
		
		table.getColumns().addAll(fullName, age, sex, dependent, dependentName, address);
		grid.add(table, 1, 7);
		
		Text mandatoryFieldsText = new Text("* Mandatory Fields");
		mandatoryFieldsText.setFill(Color.FIREBRICK);
		
		grid.add(mandatoryFieldsText, 0, 8);
		
		Group root = (Group)scene.getRoot();
		root.getChildren().add(grid);
		
		searchButton.setOnAction(value -> {
			runSearchTask();
		});
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * 
	 */
	private void runSearchTask() {
		Runnable task = () -> {
			taskRunner();
		};
		Thread background = new Thread(task);
		background.setDaemon(true);
		background.start();
	}
	
	/**
	 * 
	 */
	private void taskRunner() {
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
		
		Platform.runLater(() -> {
			table.setItems(data);
		});
		progressIndicator.setVisible(false);
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
