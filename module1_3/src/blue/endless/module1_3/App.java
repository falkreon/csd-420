/*
 * CSD420: Advanced Java
 * Module 1: JavaFX Controls and Event-Driven Programming Item Options
 *   Assignment 3: Programming Assignment
 * Isaac Ellingson
 * 3/25/2026
 * 
 * GUI program that deals 4 random cards from a standard 52-card deck on demand.
 * 
 * NOTE: This program is modular, since javaFX is modular, so you may run into additional hurdles running this code that
 * you did not run into last time.
 * Your environment variables, especially the modulepath additions, should resolve the declared module dependencies and
 * everything SHOULD work on your setup, but I can't make guarantees on systems I can't test with.
 * 
 * 
 * If anything breaks, I know it's distasteful to you, but `./gradlew clean build run` in the csd-420 repository version
 * will also just get everything you need, build it, and run it for you.
 */

package blue.endless.module1_3;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {
	private Random random = new Random();
	
	private VBox root = new VBox();
	private HBox cardsBox = new HBox();
	private ImageView card1 = new ImageView();
	private ImageView card2 = new ImageView();
	private ImageView card3 = new ImageView();
	private ImageView card4 = new ImageView();
	private ImageView[] cards = { card1, card2, card3, card4 };
	private List<Image> cardImages = new ArrayList<>();
	private Button dealButton = new Button("Deal");
	
	/** Starts the application for non-javafx-aware systems */
	public static void main(String... args) {
		Application.launch(App.class, args);
	}

	/**
	 * With only 52 resources, it's virtually instant to preload the cards, which may be frequently reused during the
	 * program execution. Therefore, this method MUST be called before card images are used, and will populate the list
	 * of images ahead of time.
	 * 
	 * <p>This method also does an initial check to ensure the "cards" folder is available at runtime, and will display
	 * helpful information to the user if the check fails. It will fail gracefully with error messages if some cards are
	 * malformed, but at least one card is required to load successfully, otherwise this method will halt operation.
	 */
	public void preloadCards() {
		Path cardsFolder = Path.of("cards");
		if (!Files.exists(cardsFolder)) {
			System.out.println("Could not find the cards folder. Please make sure that the cards folder is sitting in the same working directory you run java from.");
			System.out.println("Location checked: "+cardsFolder.toAbsolutePath().toString());
			System.exit(-1);
		}
		
		for(int i=1; i<=52; i++) {
			Path p = cardsFolder.resolve("" + i + ".png");
			try {
				Image im = new Image(p.toUri().toURL().toString());
				cardImages.add(im);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		if (cardImages.isEmpty()) {
			throw new IllegalStateException("Did not successfully load ANY cards. The program cannot proceed.");
		}
	}
	
	/**
	 * Deals a new set of random cards.
	 */
	public void reroll() {
		for(ImageView card : cards) {
			int cardNum = random.nextInt(cardImages.size());
			card.setImage(cardImages.get(cardNum));
		}
	}
	
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage stage) {
		preloadCards();
		
		reroll();
		
		
		// From here on: Stitch UI together with styles, hierarchy, and events
		
		root.setStyle("-fx-spacing: 8px;");
		
		root.getChildren().add(cardsBox);
		cardsBox.setStyle("-fx-min-width: 100%; -fx-spacing: 6px; -fx-padding: 3px; -fx-background-color: #777;");
		for(ImageView card : cards) {
			cardsBox.getChildren().add(card);
			card.setFitWidth(100);
			card.setPreserveRatio(true);
		}
		
		root.getChildren().add(dealButton);
		
		// The lambda is here to curry the 1-arg event handler to the no-arg reroll method.
		// Otherwise, this could be a method reference instead.
		dealButton.setOnAction((it) -> reroll());
		dealButton.setMaxWidth(423);
		// The button's ignoring height hints. Rude!
		dealButton.setMaxHeight(32);
		dealButton.setPrefHeight(24);
		dealButton.setMinHeight(24);
		
		Scene scene = new Scene(root, 423, 200);
		
		stage.setTitle("CSD420: Module 1.3");
		stage.setScene(scene);
		stage.show();
	}

}
