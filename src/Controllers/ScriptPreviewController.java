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
import static javafx.application.Platform.runLater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author OBAI
 */
public class ScriptPreviewController extends WizardView implements Initializable {

    @FXML
    private Button submit;
    @FXML
    private TextArea scriptPreviewField;
    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setText() {
        runLater(() -> scriptPreviewField.setText(super.wizard.script.getScriptVal().getValue()));
    }

    @FXML
    private void submit(ActionEvent event) throws InterruptedException {
        runLater(() -> loadingIndicator.setVisible(true));
        super.wizard.script.submit();
        wizard.stage.fireEvent(new WindowEvent(wizard.stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
