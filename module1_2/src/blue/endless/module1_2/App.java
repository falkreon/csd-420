package blue.endless.module1_2;

import java.net.MalformedURLException;
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

	public void reroll() {
		for(ImageView card : cards) {
			int cardNum = random.nextInt(cardImages.size());
			card.setImage(cardImages.get(cardNum));
		}
	}
	
	@Override
	public void start(Stage stage) {
		// Preload the card images
		Path cardsFolder = Path.of("cards");
		for(int i=1; i<=52; i++) {
			Path p = cardsFolder.resolve("" + i + ".png");
			try {
				Image im = new Image(p.toUri().toURL().toString());
				cardImages.add(im);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		card1.getStyleClass().add("card");
		card2.getStyleClass().add("card");
		card3.getStyleClass().add("card");
		card4.getStyleClass().add("card");
		
		reroll();
		
		for(ImageView card : cards) {
			card.setFitWidth(100);
			card.setPreserveRatio(true);
		}
		
		root.setStyle("-fx-spacing: 6px;");
		
		root.getChildren().add(cardsBox);
		cardsBox.setStyle("-fx-min-width: 100%; -fx-spacing: 6px; -fx-padding: 3px; -fx-background-color: #777;");
		cardsBox.getChildren().add(card1);
		cardsBox.getChildren().add(card2);
		cardsBox.getChildren().add(card3);
		cardsBox.getChildren().add(card4);
		
		root.getChildren().add(dealButton);
		
		// The lambda is here to curry the 1-arg event handler to the no-arg reroll method.
		// Otherwise, this could be a method reference instead.
		dealButton.setOnAction((it) -> reroll());
		dealButton.setMaxWidth(423);
		dealButton.setMaxHeight(32);
		dealButton.setPrefHeight(24);
		
		Scene scene = new Scene(root, 423, 200);
		
		stage.setTitle("CSD420: Module 1.2");
		stage.setScene(scene);
		stage.show();
	}

}
