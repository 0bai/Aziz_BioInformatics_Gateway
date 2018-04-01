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

public class PBSViewController extends WizardView implements Initializable {

    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TextField name;
    private SimpleStringProperty walltime;
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
    @FXML
    private Spinner<Integer> hours;
    @FXML
    private Spinner<Integer> minutes;
    @FXML
    private Spinner<Integer> seconds;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        walltime = new SimpleStringProperty();
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (name.getText().trim().isEmpty()) {
                name.getStyleClass().add("error");
            } else {
                name.getStyleClass().remove("error");
            }
        });
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {

        walltime.setValue(hours.getValue() + ":" + minutes.getValue() + ":" + seconds.getValue());
        super.wizard.script.getName().bind(name.textProperty());
        super.wizard.script.getWallTime().bind(walltime);
        super.wizard.script.getQueue().bind(new SimpleStringProperty(thin.isSelected() ? "thin" : "fat"));
        super.wizard.script.getMonth().bind(month.selectedProperty());
        super.wizard.script.getNodes().bind(nodes.valueProperty());
        super.wizard.script.getThreads().bind(threads.valueProperty());
        if (validate()) {
            super.wizard.next(event);
        }

    }

    private boolean validate() {
        return !name.getText().trim().isEmpty();
    }

}
