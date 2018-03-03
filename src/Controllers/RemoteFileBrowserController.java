package Controllers;

import Models.Jobs;
import Models.RemoteTree;
import Models.SSHWrapper;
import Models.WizardView;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class RemoteFileBrowserController extends WizardView implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Thread thread;
    private boolean flag;
    private Jobs jobs;
    private List files = new ArrayList();
    @FXML
    private TreeView<String> azizBrowser;
    @FXML
    private Button select;

    public RemoteFileBrowserController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/RemoteFileBrowserView.fxml"));
        try {
            parent = (Parent) fxmlLoader.load();
            scene = new Scene(parent);
        } catch (IOException ex) {
            System.out.println("fuck");
            throw new RuntimeException(ex);
        }

    }

    public void launch(Stage stage) throws SftpException, JSchException {
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        TreeItem root = new TreeItem<String>("datasets");
        new RemoteTree().constructTree("/home/" + SSHWrapper.username + "/ABG/datasets", root);
        azizBrowser.setRoot(root);
        stage.show();

    }
    
    @FXML 
    public void select(){
    files.add(azizBrowser.getSelectionModel().getSelectedItem().getValue());
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
