package Controllers;

import Models.Script;
import Models.WizardView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.beans.property.*;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WizardController {

    public Stage stage;
    private Scene scene;
    private Parent parent;
    private Thread thread;
    private boolean flag;
    public int current = 0;
    public Script script = new Script();
    public ListProperty<String> selectedAzizFiles;
    private ArrayList<URL> wizard;
    private ArrayList<Scene> scenes;
    private ArrayList<WizardView> controllers;
    public int test;

    public WizardController() {
        wizard = new ArrayList<>();
        scenes = new ArrayList<>();
        controllers = new ArrayList<>();
        wizard.add(getClass().getResource("/Views/TestsSelectionView.fxml"));
        wizard.add(getClass().getResource("/Views/DataSelectionView.fxml"));
        wizard.add(getClass().getResource("/Views/PBSView.fxml"));
        wizard.add(getClass().getResource("/Views/MotifMatchingView.fxml"));
        wizard.add(getClass().getResource("/Views/MotifScanningView.fxml"));
        wizard.add(getClass().getResource("/Views/MotifDiscoveryView.fxml"));
        wizard.add(getClass().getResource("/Views/FastMotifMatchingView.fxml"));
        wizard.add(getClass().getResource("/Views/ScriptPreview.fxml"));
        selectedAzizFiles = new SimpleListProperty<>();
        loadScreen();
    }

    public void launch(Stage stage) {
        this.stage = stage;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scenes.get(current));
        stage.setResizable(false);
        stage.show();

    }

    public void back(ActionEvent event) {
        current--;
        stage.setScene(scenes.get(current));
    }

    public void next(ActionEvent event) {
        current++;
        if (current == 1) {
            switch (test) {
                case 0:
                    scenes.remove(4);
                    scenes.remove(4);
                    scenes.remove(4);
                    controllers.remove(4);
                    controllers.remove(4);
                    controllers.remove(4);
                    break;
                case 1:
                    scenes.remove(3);
                    scenes.remove(4);
                    scenes.remove(4);
                    controllers.remove(3);
                    controllers.remove(4);
                    controllers.remove(4);
                    break;
                case 2:
                    scenes.remove(3);
                    scenes.remove(3);
                    scenes.remove(4);
                    controllers.remove(3);
                    controllers.remove(3);
                    controllers.remove(4);
                    break;
                case 3:
                    scenes.remove(3);
                    scenes.remove(3);
                    scenes.remove(3);
                    controllers.remove(3);
                    controllers.remove(3);
                    controllers.remove(3);
                    break;

            }
       }
        controllers.get(current).setWizard(this);
        if (current == controllers.size()-1) {
            ((ScriptPreviewController)controllers.get(current)).setText();
        }
        stage.setScene(scenes.get(current));
    }

    private void loadScreen() {
        for (int i = 0; i < wizard.size(); i++) {

            FXMLLoader fxmlLoader = new FXMLLoader(wizard.get(i));
            try {
                parent = (Parent) fxmlLoader.load(wizard.get(i).openStream());
                controllers.add((WizardView) fxmlLoader.getController());
                scene = new Scene(parent);
            } catch (IOException ex) {

                throw new RuntimeException(ex);
            }

            scenes.add(scene);
        }
        controllers.get(0).setWizard(this);
    }

}
