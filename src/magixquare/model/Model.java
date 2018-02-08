package magixquare.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model extends Observable {

	/*
	 * Die IDs aller ChoiceBox Felder */
	public List<String> choiceButtonIds = new ArrayList<>(Arrays.asList("00", "01", "02", "10", "11", "12", "20", "21", "22"));

	/*
	 * Alle Kombinationen der waagerechten, 
	 * senkrechten und diagonalen ChoiceBoxen 
	 * (Reihe 1, Reihe 2, ..., Säule 1, Säule 2..., Diagonale 1...) */
	public Integer[][][] choiceButtonCombinations = { { {0, 0}, {0, 1}, {0, 2} }, { {1, 0}, {1, 1}, {1, 2} }, { {2, 0}, {2, 1}, {2, 2} }, { {0, 0}, {1, 0}, {2, 0} }, { {0, 1}, {1, 1}, {2, 1} }, { {0, 2}, {1, 2}, {2, 2} }, { {0, 2}, {1, 1}, {2, 0} }, { {0, 0}, {1, 1}, {2, 2} } };

	/*
	 * Die IDs aller Label für die Ausgabe
	 * der Summen */
	public String[] sumLabelIds = { "r1", "r2", "r3", "c1", "c2", "c3", "dup", "ddown" };

	/*
	 * Werte für die ChoicBoxes festlegen (1-9) */
	public ObservableList<Integer> choiceBoxValues = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);

	/*
	 * Speicher für die Summen der Labels */
	public Map<String, Integer> labelSumValues = new HashMap<>();

	private Set<Integer> usedValues = new HashSet<>();
	private Set<Integer> duplicateValues = new HashSet<>();
	
	/*
	 * Speicher für die Werte im magischen Quadrat */
	private int[][] squareValues = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

	public Model() {
	}

	public void setSquareValues(int d1, int d2, Object newValue) {
		if (newValue != null) {
			squareValues[d1][d2] = (int) newValue;
			setUsedValues((int) newValue);
		}
		setChanged(); 		// Oberservable Änderung festhalten 
		notifyObservers();	// Observer informieren
	}


	public void createDuplicates() {
		getDuplicateValues().clear();
		int elm = 0;
		for(Integer item : getUsedValues()) {
			for(int i = 0; i < squareValues.length; i++) {
				for(int j = 0; j < squareValues[i].length; j++) {
					if(item.equals(squareValues[i][j])) {
						elm++;
						if(elm > 1) {
							setDuplicateValues(item);
							break;
						}
					}
				}
			}
			elm = 0;
		}
		System.out.println("Duplicates");
		for(Integer item : getDuplicateValues()) {
			System.out.println(item);
		}
	}
	
	public boolean duplicateExists() {
		boolean check = false;
		if(getDuplicateValues().size() > 0) {
			check = true;
		}
		return check;
	}

	public void doLabelSums() {
		int labelCounter = 0;
		for (Integer[][] item : choiceButtonCombinations) {
			int sum = 0;
			for (Integer[] subitem : item) {
				sum += squareValues[subitem[0]][subitem[1]];
			}
			getLabelSumValues().put(sumLabelIds[labelCounter], sum);
			labelCounter++;
		}
	}

	public boolean checkWin() {
		doLabelSums();
		boolean win = true;
		for (String labelValues : getLabelSumValues().keySet()) {
			if (!getLabelSumValues().get(labelValues).equals(15)) {
				win = false;
			}
		}
		return win;
	}

	public void reset2Defaults() {
		for (int i = 0; i < squareValues.length; i++) {
			for (int j = 0; j < squareValues[i].length; j++) {
				squareValues[i][j] = 0;
			}
		}
	}
	public Map<String, Integer> getLabelSumValues() {
		return labelSumValues;
	}

	public Set<Integer> getUsedValues() {
		return usedValues;
	}

	public void setUsedValues(Integer value) {
		usedValues.add(value);
	}

	public Set<Integer> getDuplicateValues() {
		return duplicateValues;
	}

	public void setDuplicateValues(Integer value) {
		duplicateValues.add(value);
	}
}