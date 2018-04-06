/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.WizardView;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author OBAI
 */
public class FastMotifMatchingViewController extends WizardView implements Initializable {
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
    private ChoiceBox<?> occurrence;
    @FXML
    private Spinner<?> motifNumber;
    @FXML
    private Spinner<?> minMotifSites;
    @FXML
    private Spinner<?> maxMotifSites;
    @FXML
    private CheckBox motifExact;
    @FXML
    private Slider bias;
    @FXML
    private Spinner<?> motifLength;
    @FXML
    private Spinner<?> motifLengthMin;
    @FXML
    private Spinner<?> motifLengthMax;
    @FXML
    private Spinner<?> gapOpen;
    @FXML
    private Spinner<?> gapExtend;
    @FXML
    private CheckBox trimming;
    @FXML
    private CheckBox endGaps;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void back(ActionEvent event) {
    }

    @FXML
    private void next(ActionEvent event) {
    }
    
}
