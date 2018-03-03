/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author OBAI
 */
public class MotifDiscoveryViewController extends WizardView implements Initializable {

    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TextField outputName;
    @FXML
    private CheckBox overWrite;
    @FXML
    private CheckBox textOut;
    @FXML
    private RadioButton DNA;
    @FXML
    private ToggleGroup inputType;
    @FXML
    private RadioButton RNA;
    @FXML
    private RadioButton Protein;
    @FXML
    private ChoiceBox<String> occurrence;
    @FXML
    private Spinner<Integer> motifNumber;
    @FXML
    private TextField motifMax;
    @FXML
    private TextField motifMin;
    @FXML
    private CheckBox motifExact;
    @FXML
    private Slider bias;
    @FXML
    private Spinner<Integer> motifLength;
    @FXML
    private TextField motifLengthMax;
    @FXML
    private TextField motifLengthMin;
    @FXML
    private TextField gapOpen;
    @FXML
    private TextField gapExtend;
    @FXML
    private CheckBox trimming;
    @FXML
    private CheckBox endGaps;

    private WizardController wizard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DNA.setUserData("dna");
        RNA.setUserData("rna");
        Protein.setUserData("protein");
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {

        super.wizard.script.getOutputName().bindBidirectional(outputName.textProperty());
        super.wizard.script.getOverWrite().bindBidirectional(overWrite.selectedProperty());
        super.wizard.script.getOutputType().bindBidirectional(textOut.selectedProperty());
        super.wizard.script.getInputType().bind(new SimpleStringProperty((String) inputType.getSelectedToggle().getUserData()));
        super.wizard.script.getOcurrence().bindBidirectional(occurrence.valueProperty());
        super.wizard.script.getMotifNumber().bind(motifNumber.valueProperty());
        super.wizard.script.getMaxMotifSites().bindBidirectional(motifMax.textProperty());
        super.wizard.script.getMinMotifSites().bindBidirectional(motifMin.textProperty());
        super.wizard.script.getExactMotifSites().bindBidirectional(motifExact.selectedProperty());
        super.wizard.script.getBias().bindBidirectional(bias.valueProperty());
        super.wizard.script.getMotifLength().bind(motifLength.valueProperty());
        super.wizard.script.getMaxMotifLength().bindBidirectional(motifLengthMax.textProperty());
        super.wizard.script.getMinMotifLength().bindBidirectional(motifLengthMin.textProperty());
        super.wizard.script.getGapOpeningCost().bindBidirectional(gapOpen.textProperty());
        super.wizard.script.getGapExtensionCost().bindBidirectional(gapExtend.textProperty());
        super.wizard.script.getTrimming().bindBidirectional(trimming.selectedProperty());
        super.wizard.script.getNoEndGaps().bindBidirectional(endGaps.selectedProperty());

        super.wizard.next(event);

    }
}
