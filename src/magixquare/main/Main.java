package magixquare.main;

import javafx.application.Application;
import javafx.stage.Stage;
import magixquare.controller.Controller;
import magixquare.model.Model;
import magixquare.view.View;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Model model = new Model();
		View view = new View(model, primaryStage);
		new Controller(model, view);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
