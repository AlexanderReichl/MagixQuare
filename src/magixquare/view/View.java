package magixquare.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import magixquare.model.Model;

public class View implements Observer {

	private Stage stage;
	private Model model;

	private Parent root;
	private Button reset;
	private Label status;
	private List<ChoiceBox<Integer>> choiceBoxes = new ArrayList<>();
	private List<Label> sumLabelList = new ArrayList<>();

	public View(Model model, Stage stage) {
		this.model = model;
		this.stage = stage;

		// Beim Observer anmelden, damit das Model weiß, dass es dieses Objekt informieren muss
		model.addObserver(this);
		
		try {
			/*
			 * Laden derXML Vorlage
			 */
			root = FXMLLoader.load(getClass().getResource("/magixquare/fxml/MainScene.fxml"));

			/*
			 * Laden der ChoiceBox Felder an Hand der ID
			 */
			for(String choiceBoxItem : model.choiceButtonIds) {
				@SuppressWarnings("unchecked")
				ChoiceBox<Integer> tmp = (ChoiceBox<Integer>) root.lookup("#" + choiceBoxItem);
				tmp.getItems().addAll(model.choiceBoxValues);
				choiceBoxes.add(tmp);
			}
			
			/*
			 * Laden der verfügbaren Labels für die Summenangabe
			 */
			for(String sumLabelItem : model.sumLabelIds) {
				Label tmp = (Label) root.lookup("#" + sumLabelItem);
				sumLabelList.add(tmp);
			}

			/*
			 * Laden des Reset Buttons und des Status Labels
			 */
			reset = (Button) root.lookup("#reset");
			status = (Label) root.lookup("#status");

			getStage().setTitle("Das Magische Quadrat V3.0");
	        getStage().setScene(new Scene(root));
	        getStage().show();
			update(model, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wird bei jeder Aenderung am Modell aufgerufen.
	 */
	@Override
	public void update(Observable o, Object arg) {
		model.doLabelSums();
		for(Label labelItem : sumLabelList) {
			Integer tmp = model.getLabelSumValues().get(labelItem.getId());
			if(tmp == null) {
				tmp = 0;
			}
			labelItem.setText(String.valueOf(tmp));;
		}
	}

	/**
	 * Wenn der letzte gesetzte Wert mehrfach existiert, 
	 * dem Benutzer die entsprechenden Felder durch
	 * ein Blinken anzeigen lassen
	 */
	public void setChoiceBoxHighlighting() {
		for(ChoiceBox<Integer> cb : choiceBoxes) {
			for(Integer item : model.getDuplicateValues()) {
				if(cb.getValue() != null && cb.getValue().equals(item)) {
					FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), cb);
					fadeTransition.setToValue(1.0);
					fadeTransition.setFromValue(0.0);
					fadeTransition.setCycleCount(5);
					fadeTransition.play();
				}
			}
		}
	}

	/**
	 * ChoiceBox Auswahl leeren
	 */
	public void reset2Defaults() {
		for(ChoiceBox<Integer> choiceBoxItem : choiceBoxes) {
			choiceBoxItem.setValue(null);;
		}
	}
	
	public List<ChoiceBox<Integer>> getChoiceBoxes() {
		return choiceBoxes;
	}
	
	public List<Label> getSumLabelList() {
		return sumLabelList;
	}
	
	public Button getReset() {
		return reset;
	}

	public Label getStatus() {
		return status;
	}

	public Stage getStage() {
		return stage;
	}

}
