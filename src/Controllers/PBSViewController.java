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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import Models.Script;
import javafx.beans.property.SimpleStringProperty;

/**
 * FXML Controller class
 *
 * @author OBAI
 */
public class PBSViewController extends WizardView implements Initializable {
     @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TextField name;
    @FXML
    private TextField walltime;
    @FXML
    private RadioButton thin;
    @FXML
    private ToggleGroup queue;
    @FXML
    private RadioButton fat;
    @FXML
    private CheckBox month;
    @FXML
    private Spinner<Integer> nodes;
    @FXML
    private Spinner<Integer> threads;

    
    private WizardController wizard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        
    }   



    @FXML
    private void back(ActionEvent event) {
      super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {  
        validate();
        super.wizard.script.getName().bind(name.textProperty());
        super.wizard.script.getWallTime().bind(walltime.textProperty());
        super.wizard.script.getQueue().bind(new SimpleStringProperty(thin.isSelected()?"thin":"fat"));
        super.wizard.script.getMonth().bind(month.selectedProperty());
        super.wizard.script.getNodes().bind(nodes.valueProperty());
        super.wizard.script.getThreads().bind(threads.valueProperty());
        super.wizard.next(event);
    }
    
    private boolean validate(){
        if (name.getText().isEmpty()) {
   
        }
    
    return true;}
    
}
