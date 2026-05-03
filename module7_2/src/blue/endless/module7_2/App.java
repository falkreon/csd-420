/*
 * CSD420: Advanced Java Programming
 * Module 7: More JavaFX
 *   Assignment 2: Programming Assignment
 * 
 * Isaac Ellingson
 * 5/3/2026
 * 
 * Replicates the provided image, while demonstrating the use of classes and IDs with external CSS to style elements.
 * Listings 31.1, 31.2, and Figure 31.1 were referenced. I rearranged the code to a more readable form using anonymous
 * blocks to express hierarchy. We could have done FXML again, and I *like* FXML, but I know that's not always going to
 * be the right tool for the job, so I want to keep finding more clean and clear ways to do this.
 */

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
		primaryStage.setMinHeight(320);
		primaryStage.setMinWidth(320);
		primaryStage.setMaxHeight(600);
		primaryStage.setMaxWidth(800);
		
		
		HBox root = new HBox();
		{
			Pane leftArea = new Pane();
			root.getChildren().add(leftArea);
			{
				leftArea.getStyleClass().add("border");
				
				Circle leftAreaCircle = new Circle(35, 150, 30);
				leftAreaCircle.getStyleClass().add("plaincircle");
				leftArea.getChildren().add(leftAreaCircle);
				
			}
			
			Pane rightArea = new Pane();
			root.getChildren().add(rightArea);
			{
				Circle leftCircle = new Circle(35, 150, 30);
				leftCircle.getStyleClass().add("plaincircle");
				rightArea.getChildren().add(leftCircle);
				
				Circle middleCircle = new Circle(100, 150, 30);
				middleCircle.setId("redcircle");
				rightArea.getChildren().add(middleCircle);
				
				Circle rightCircle = new Circle(165, 150, 30);
				// ID has higher precedence than class, so this will be green instead of plain or with a dashed outline.
				rightCircle.getStyleClass().addAll("circleborder", "plaincircle");
				rightCircle.setId("greencircle");
				rightArea.getChildren().add(rightCircle);
			}
			
		}
		
		
		// Typically I'd use tooling to include the css inside the jar. But we can't do that here.
		// Thanks to https://blog.idrsolutions.com/use-external-css-files-javafx/ for this wonderful workaround
		File stylesheet = new File("mystyle.css");
		String stylesheetString = "file:///" + stylesheet.getAbsolutePath().replace("\\", "/");
		root.getStylesheets().clear();
		root.getStylesheets().add(stylesheetString);
		
		// Create the scene and show it on the stage
		Scene scene = new Scene(root, 320, 320);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
