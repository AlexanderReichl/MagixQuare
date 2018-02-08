package magixquare.controller;

import java.util.Observable;
import java.util.Observer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import magixquare.model.Model;
import magixquare.view.View;

public class Controller implements Observer {

	private Model model;
	private View view;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;

		// Beim Observer anmelden, damit das Model weiß, dass es dieses Objekt informieren muss
		model.addObserver(this);

		for(ChoiceBox<Integer> choiceButtonItem : view.getChoiceBoxes()) {
			choiceButtonItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
					int d1 = Integer.valueOf(choiceButtonItem.getId().substring(0, 1));
					int d2 = Integer.valueOf(choiceButtonItem.getId().substring(1, 2));
					model.setSquareValues(d1, d2, newValue);
				}

			});
		}
		view.getReset().setOnAction(event -> restart());
		view.getStatus().setText("Viel Erfolg!");
		view.getStage().show();
	}

	/**
	 * Neustart des Spiels.
	 */
	void restart() {
		model.reset2Defaults();
		model.doLabelSums();
		view.reset2Defaults();
		view.getStatus().setText("Zurückgesetzt!");
	}

	/**
	 * Wird bei jeder Aenderung am Modell aufgerufen.
	 */
	@Override
	public void update(Observable o, Object arg) {
		model.createDuplicates(); // Ermittelt mehrfach verwendete Werte
		// und visualisiert bei Vorhandensein entsprechende Felder
		if(model.duplicateExists()) {
			view.setChoiceBoxHighlighting();
		}
		// Prüft, ob das Rätsel erfolgreich gelöst wurde
		if (model.checkWin()) {
			view.getStatus().setText("Gut gemacht!");
		} 
	}
}