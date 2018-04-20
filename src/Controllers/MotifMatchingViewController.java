package Controllers;

import Models.Database;
import Models.MotifMatchingScript;
import Models.SSHListener;
import Models.SSHWrapper;
import Models.WizardView;
import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class MotifMatchingViewController extends WizardView implements Initializable, SSHListener {

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
    private ChoiceBox<String> comparison;
    @FXML
    private ChoiceBox<String> thresh;
    @FXML
    private Slider slider;
    @FXML
    private Spinner<Integer> overlap;
    @FXML
    private CheckBox completeRows;
    @FXML
    private ComboBox<Database> database;
    @FXML
    private ComboBox<Database> category;

    private List<Database> Clist;
    private List<Database> Dlist;
    private HashMap<Integer, List<Database>> items;
    private String response;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Clist = new ArrayList<>();
        Dlist = new ArrayList<>();
        items = new HashMap<>();
        database.getSelectionModel().select(0);
        comparison.getSelectionModel().select(0);
        initSlider();
        try {
            showDB();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MotifMatchingViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        category.getItems().setAll(FXCollections.observableArrayList(Clist));
        category.valueProperty().addListener((obsv, oldVal, newVal) -> {
            database.getSelectionModel().clearSelection();
            database.getItems().setAll(items.get(category.getSelectionModel().selectedIndexProperty().getValue()));
        });

    }

    private void initSlider() {
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



    private synchronized void showDB() throws FileNotFoundException {
        Scanner in = new Scanner(new File(SSHWrapper.GetLocalHomeFolder() + "/ABG/motif_db.csv"));
        for (int i = 0; i < 10; i++) {
            in.nextLine();
        }
        int count = -1;
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.contains("----")) {
                Clist.add(new Database(line.replaceAll("(-|,)", ""), ""));
                if (++count > 0) {
                    items.put(count - 1, Dlist);
                    Dlist = new LinkedList<>();
                }
            } else if (!line.isEmpty()) {
                Dlist.add(new Database(line.split(",")[4], line.split(",")[0]));
            }
        }
        items.put(count, Dlist);
    }

    @FXML
    private void back(ActionEvent event) {
        super.wizard.back(event);
    }

    @FXML
    private void next(ActionEvent event) {
         super.wizard.script.setType('T');
        ((MotifMatchingScript) super.wizard.script).getOutputName().bindBidirectional(outputName.textProperty());
        ((MotifMatchingScript) super.wizard.script).getOverWrite().bindBidirectional(overWrite.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getOutputType().bindBidirectional(textOut.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getAlignedCols().bindBidirectional(completeRows.selectedProperty());
        ((MotifMatchingScript) super.wizard.script).getComparisonFunc().bind(comparison.getSelectionModel().selectedIndexProperty());
        ((MotifMatchingScript) super.wizard.script).getDb().bind(new SimpleStringProperty(database.getSelectionModel().getSelectedItem().getPath()));
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

    @Override
    public void sshResponse(String strCommand, String strResponse) {
        System.out.println(strResponse+"!");
        response = strResponse;
    }

    @Override
    public void FileDownloadResponse(String strFilePath, Boolean bStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FileUploadResponse(String strFilePath, Boolean bStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
