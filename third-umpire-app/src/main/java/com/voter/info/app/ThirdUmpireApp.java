package com.voter.info.app;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class ThirdUmpireApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Third Umpire");
		TextField firstNameTextField = new TextField();
		firstNameTextField.setPromptText("First Name");
		
		
		TextField middleNameTextField = new TextField();
		middleNameTextField.setPromptText("Middle Name");
		
		TextField lastNameTextField = new TextField();
		lastNameTextField.setPromptText("Last Name");
		
		TextField districtNameTextField = new TextField();
		districtNameTextField.setPromptText("District Name");
		
		TextField assemblyConstituencyNameTextField = new TextField();
		assemblyConstituencyNameTextField.setPromptText("Assembly Constituency Name");
		
		GridPane root = new GridPane();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setVgap(5);
		root.setHgap(5);
		GridPane.setConstraints(firstNameTextField, 0, 0);
		root.getChildren().add(firstNameTextField);
		GridPane.setConstraints(middleNameTextField, 0, 1);
		root.getChildren().add(middleNameTextField);
		GridPane.setConstraints(lastNameTextField, 0, 2);
		root.getChildren().add(lastNameTextField);
		GridPane.setConstraints(districtNameTextField, 0, 3);
		root.getChildren().add(districtNameTextField);
		GridPane.setConstraints(assemblyConstituencyNameTextField, 0, 4);
		root.getChildren().add(assemblyConstituencyNameTextField);
		
		primaryStage.setScene(new Scene(root, 300, 300));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
