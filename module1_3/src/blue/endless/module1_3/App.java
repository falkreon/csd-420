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
	
	public static void main(String... args) {
		Application.launch(App.class, args);
	}

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
	
	public void reroll() {
		for(ImageView card : cards) {
			int cardNum = random.nextInt(cardImages.size());
			card.setImage(cardImages.get(cardNum));
		}
	}
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage stage) {
		// Preload the card images
		preloadCards();
		
		reroll();
		
		root.setStyle("-fx-spacing: 6px;");
		
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
		dealButton.setMaxHeight(32);
		dealButton.setPrefHeight(24);
		
		Scene scene = new Scene(root, 423, 200);
		
		stage.setTitle("CSD420: Module 1.3");
		stage.setScene(scene);
		stage.show();
	}

}
