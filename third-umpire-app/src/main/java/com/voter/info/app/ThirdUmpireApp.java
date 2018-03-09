package com.voter.info.app;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.voter.info.service.VoterService;
import com.voter.info.service.VoterServiceImpl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Third Umpire");
		GridPane grid = new GridPane();
		/*try {
			initialize();
		} catch(Exception e) {
			grid.add(new Label("Oops!! Remote server is unreachable. Please try after some time."), 0, 0);
			Button closeButton = new Button("Close");
			closeButton.setOnAction(value -> System.exit(0));
			grid.add(closeButton, 0, 1);
			Scene scene = new Scene(new Group(), 450,250);
			
			Group root = (Group)scene.getRoot();
			root.getChildren().add(grid);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		}*/
		Scene scene = new Scene(new Group(), 450,250);
		
		TextField firstNameTextField = new TextField();
		firstNameTextField.setPromptText("First Name");
		
		
		TextField middleNameTextField = new TextField();
		middleNameTextField.setPromptText("Middle Name");
		
		TextField lastNameTextField = new TextField();
		lastNameTextField.setPromptText("Last Name");
		
		ObservableList<String> districtOptions = FXCollections.observableArrayList(allDistricts);
		ComboBox<?> districtComboBox = new ComboBox<>(districtOptions);
		
		districtComboBox.setOnAction(value -> {
			String selectedDistrict = (String)districtComboBox.getValue();
			ComboBox<?> assemblyConstituencyComboBox = null;
			if(selectedDistrict != null && selectedDistrict.length() > 1) {
				if(grid.getChildren().contains(assemblyConstituencyComboBox))
					grid.getChildren().remove(assemblyConstituencyComboBox);
				ObservableList<String> assemblyConstituencyOptions = FXCollections.observableArrayList(getAllAssemblyConstituencies(selectedDistrict));
				assemblyConstituencyComboBox = new ComboBox<>(assemblyConstituencyOptions);
				
				
				grid.add(assemblyConstituencyComboBox, 1, 4);
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
		
		Group root = (Group)scene.getRoot();
		root.getChildren().add(grid);
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	private List<String> getAllAssemblyConstituencies(String districtName) {
		return districtsWithAssemblyConstituencies.get(districtName);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
