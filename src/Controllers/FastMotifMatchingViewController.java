package Controllers;

import Models.FastMotifMatchingScript;
import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class FastMotifMatchingViewController extends WizardView implements Initializable {

    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TextField outputName;
    @FXML
    private Spinner<Integer> sequenceNumber;
    @FXML
    private Spinner<Integer> sequenceLength;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {
        ((FastMotifMatchingScript) super.wizard.script).getOutputName().bindBidirectional(outputName.textProperty());
        ((FastMotifMatchingScript) super.wizard.script).getSequenceLength().bind(sequenceLength.valueProperty());
        ((FastMotifMatchingScript) super.wizard.script).getSequenceNumber().bind(sequenceNumber.valueProperty());
        super.wizard.script.setScriptVal(new SimpleStringProperty(((FastMotifMatchingScript) super.wizard.script).toString()));
        if (Validate()) {
            super.wizard.next(event);
        }
    }

    private boolean Validate() {
        return !outputName.getText().trim().isEmpty();
    }

}
