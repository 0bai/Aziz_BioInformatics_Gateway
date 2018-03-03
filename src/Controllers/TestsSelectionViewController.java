/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

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

/**
 * FXML Controller class
 *
 * @author OBAI
 */
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
    private ProgressIndicator loadingIndicator;
    
    private WizardController wizard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  



    @FXML
    private void next(ActionEvent event) {
        Platform.runLater(()->loadingIndicator.setVisible(true));
        super.wizard.next(event);
    }
    
}
