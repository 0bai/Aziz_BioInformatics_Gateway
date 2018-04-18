
package Controllers;

import Models.MotifDiscoveryScript;
import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
    private CheckBox motifExact;
    @FXML
    private Slider bias;
    @FXML
    private Spinner<Integer> motifLength;
    @FXML
    private Spinner<Integer> motifLengthMax;
    @FXML
    private Spinner<Integer> motifLengthMin;
    @FXML
    private Spinner<Integer> gapOpen;
    @FXML
    private Spinner<Integer> gapExtend;
    @FXML
    private CheckBox trimming;
    @FXML
    private CheckBox endGaps;
    @FXML
    private Spinner<Integer> minMotifSites;
    @FXML
    private Spinner<Integer> maxMotifSites;

    private WizardController wizard;
    private String ocurr[] = {"zoops", "oops", "anr"};

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DNA.setUserData("dna");
        RNA.setUserData("rna");
        Protein.setUserData("protein");
        outputName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (outputName.getText().trim().isEmpty()) {
                outputName.getStyleClass().add("error");
            } else {
                outputName.getStyleClass().remove("error");
            }
        });
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {

        ((MotifDiscoveryScript) super.wizard.script).getOutputName().bindBidirectional(outputName.textProperty());
        ((MotifDiscoveryScript) super.wizard.script).getOverWrite().bindBidirectional(overWrite.selectedProperty());
        ((MotifDiscoveryScript) super.wizard.script).getOutputType().bindBidirectional(textOut.selectedProperty());
        ((MotifDiscoveryScript) super.wizard.script).getInputType().bind(new SimpleStringProperty((String) inputType.getSelectedToggle().getUserData()));
        ((MotifDiscoveryScript) super.wizard.script).getOcurrence().setValue(ocurr[occurrence.getSelectionModel().selectedIndexProperty().getValue()]);
        ((MotifDiscoveryScript) super.wizard.script).getMotifNumber().bind(motifNumber.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getMaxMotifSites().bind(maxMotifSites.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getMinMotifSites().bind(minMotifSites.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getExactMotifSites().bindBidirectional(motifExact.selectedProperty());
        ((MotifDiscoveryScript) super.wizard.script).getBias().bindBidirectional(bias.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getMotifLength().bind(motifLength.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getMaxMotifLength().bind(motifLengthMax.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getMinMotifLength().bind(motifLengthMin.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getGapOpeningCost().bind(gapOpen.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getGapExtensionCost().bind(gapExtend.valueProperty());
        ((MotifDiscoveryScript) super.wizard.script).getTrimming().bindBidirectional(trimming.selectedProperty());
        ((MotifDiscoveryScript) super.wizard.script).getNoEndGaps().bindBidirectional(endGaps.selectedProperty());
        super.wizard.script.setScriptVal(new SimpleStringProperty(((MotifDiscoveryScript) super.wizard.script).toString()));
        if (Validate()) {
            super.wizard.next(event);
        }

    }

    private boolean Validate() {
        return !outputName.getText().trim().isEmpty();
    }
}
