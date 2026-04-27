package blue.endless.module7_2;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {
	
	public static void main(String... args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Ellingson - Module 7.2 - CSD420");
		
		// Just enough flex to show that Circle positioning is not reactive within its container.
		primaryStage.setMaxHeight(600);
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(400);
		primaryStage.setMaxWidth(800);
		
		// Since we didn't get an image, this code borrows generously from the example in Listing 31.2 of the text
		// provided, so that the visuals match up with Figure 31.1
		// Elements have been rearranged to the order I prefer to create, configure, and stitch components.
		
		HBox root = new HBox();
		
		Pane leftArea = new Pane();
		leftArea.getStyleClass().add("border");
		
		Circle leftCircle = new Circle(50, 50, 30);
		leftCircle.getStyleClass().add("plaincircle");
		leftArea.getChildren().add(leftCircle);
		
		Circle rightCircle = new Circle(150, 50, 30);
		rightCircle.getStyleClass().add("plaincircle");
		leftArea.getChildren().add(rightCircle);
		
		Circle bottomCircle = new Circle(100, 100, 30);
		bottomCircle.setId("redcircle");
		leftArea.getChildren().add(bottomCircle);
		
		root.getChildren().add(leftArea);
		
		
		Pane rightArea = new Pane();
		Circle circle4 = new Circle(100, 100, 30);
		circle4.getStyleClass().addAll("circleborder", "plaincircle"); // Not sure why we do this when id has higher specificity
		// unless it's just to demonstrate css specificity
		circle4.setId("greencircle");
		rightArea.getChildren().add(circle4);
		
		root.getChildren().add(rightArea);
		
		
		
		// Typically I'd use tooling to include the css inside the jar. But we can't do that here.
		// Thanks to https://blog.idrsolutions.com/use-external-css-files-javafx/ for this wonderful workaround
		File stylesheet = new File("mystyle.css");
		String stylesheetString = "file:///" + stylesheet.getAbsolutePath().replace("\\", "/");
		root.getStylesheets().clear();
		root.getStylesheets().add(stylesheetString);
		
		// Create the scene and show it on the stage
		Scene scene = new Scene(root, 500, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
