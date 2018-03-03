package Controllers;

import Models.Script;
import Models.WizardView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.beans.property.*;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WizardController {

    public  Stage stage;
    private Scene scene;
    private Parent parent;
    private Thread thread;
    private boolean flag;
    public int current = 0;
    public Script script = new Script();
    public ListProperty<String> selectedAzizFiles;
    private ArrayList<URL> wizard;


    public WizardController() {
        wizard = new ArrayList<>();
        wizard.add(getClass().getResource("/Views/TestsSelectionView.fxml"));
        wizard.add(getClass().getResource("/Views/DataSelectionView.fxml"));
        wizard.add(getClass().getResource("/Views/PBSView.fxml"));
        wizard.add(getClass().getResource("/Views/MotifDiscoveryView.fxml"));
        wizard.add(getClass().getResource("/Views/ScriptPreview.fxml"));
        selectedAzizFiles = new SimpleListProperty<>();
 

    }

    public void launch(Stage stage) {
        this.stage = stage;
        loadScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public void back(ActionEvent event) {
        current--;
        loadScreen();
    }

    public void next(ActionEvent event) {
        current++;
        loadScreen();
    }


    private void loadScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(wizard.get(current));
        try {
            parent = (Parent) fxmlLoader.load(wizard.get(current).openStream());
            WizardView wizardSubView = (WizardView) fxmlLoader.getController();
            wizardSubView.setWizard(this);
            scene = new Scene(parent);

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }
        
        stage.setScene(scene);

    }

}
