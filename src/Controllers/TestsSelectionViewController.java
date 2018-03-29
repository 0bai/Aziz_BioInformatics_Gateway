/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.FastMotifMatchingScript;
import Models.MotifDiscoveryScript;
import Models.MotifMatchingScript;
import Models.MotifScanningScript;
import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class TestsSelectionViewController extends WizardView implements Initializable {

    @FXML
    private Button next;
    @FXML
    private RadioButton motifDiscovery;
    @FXML
    private ToggleGroup Tests;
    @FXML
    private RadioButton MotifMatching;
    @FXML
    private RadioButton MotifScanning;
    @FXML
    private RadioButton fastMotif;
    @FXML
    private ProgressIndicator loadingIndicator;

    private WizardController wizard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void next(ActionEvent event) {
        Platform.runLater(() -> loadingIndicator.setVisible(true));
        if (MotifMatching.isSelected()) {
            super.wizard.script = new MotifMatchingScript();
            super.wizard.test = 0;
        } else if (MotifScanning.isSelected()) {
            super.wizard.script = new MotifScanningScript();
            super.wizard.test = 1;
        } else if (motifDiscovery.isSelected()) {
            super.wizard.script = new MotifDiscoveryScript();
            super.wizard.test = 2;
        } else {
            super.wizard.script = new FastMotifMatchingScript();
            super.wizard.test = 3;
        }
        super.wizard.next(event);
    }

}
