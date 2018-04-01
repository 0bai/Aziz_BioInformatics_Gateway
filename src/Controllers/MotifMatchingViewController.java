package Controllers;

import Models.MotifMatchingScript;
import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class MotifMatchingViewController extends WizardView implements Initializable {

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
    private ChoiceBox<String> dataBase;
    @FXML
    private ChoiceBox<String> comparison;
    @FXML
    private ChoiceBox<String> thresh;
    @FXML
    private Slider slider;
    @FXML
    private Spinner<Integer> overlap;
    @FXML
    private CheckBox completeRows;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataBase.getSelectionModel().select(0);
        comparison.getSelectionModel().select(0);
        thresh.getSelectionModel().select(0);
        slider.setBlockIncrement(5);
        slider.setMax(100.0);
        slider.setMin(0);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setValue(10);
        thresh.valueProperty().addListener((obsv, oldVal, newVal) -> {
            Platform.runLater(() -> {
                slider.setValueChanging(true);
                if (newVal.equalsIgnoreCase("Q")) {
                    slider.setBlockIncrement(5);
                    slider.setMax(100.0);
                    slider.setMin(0);
                    slider.setMajorTickUnit(10);
                    slider.setMinorTickCount(1);
                      slider.setValue(10);
                } else {
                    slider.setBlockIncrement(0.05);
                    slider.setMax(1.0);
                    slider.setMin(0);
                    slider.setMajorTickUnit(0.1);
                    slider.setMinorTickCount(1);
                      slider.setValue(0.5);
                }
                slider.setValueChanging(false);
            });
        });
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {
        ((MotifMatchingScript) super.wizard.script).getOutputName().bindBidirectional(outputName.textProperty());
        ((MotifMatchingScript) super.wizard.script).getOverWrite().bindBidirectional(overWrite.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getOutputType().bindBidirectional(textOut.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getAlignedCols().bindBidirectional(completeRows.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getComparisonFunc().bind(comparison.getSelectionModel().selectedIndexProperty());
        ((MotifMatchingScript) super.wizard.script).getDb().bind(dataBase.valueProperty());
        ((MotifMatchingScript) super.wizard.script).getOverlap().bind(overlap.valueProperty());
        ((MotifMatchingScript) super.wizard.script).getSignificance().bindBidirectional(thresh.valueProperty());
        ((MotifMatchingScript) super.wizard.script).getThreshold().bindBidirectional(slider.valueProperty());

        super.wizard.script.setScriptVal(new SimpleStringProperty(((MotifMatchingScript) super.wizard.script).toString()));
        if (Validate()) {
            super.wizard.next(event);
        }
    }

    private boolean Validate() {
        return !outputName.getText().trim().isEmpty();
    }

}
