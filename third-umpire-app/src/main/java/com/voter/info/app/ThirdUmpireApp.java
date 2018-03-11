package com.voter.info.app;

import java.net.ConnectException;
import java.util.Collections;
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
		initialize();
		
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
				ObservableList<String> assemblyConstituencyOptions = FXCollections.observableArrayList(getAllAssemblyConstituencies(selectedDistrict));
				assemblyConstituencyOptions.add("All");
				assemblyConstituencyOptions.sort(new AssemblyConstituencySorter());
				assemblyConstituencyComboBox = new ComboBox<>(assemblyConstituencyOptions);
				if(grid.getChildren().size() > 9)
					grid.getChildren().remove(9);
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
