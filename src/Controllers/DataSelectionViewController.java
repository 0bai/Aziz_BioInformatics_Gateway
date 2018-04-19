package Controllers;

import Models.AlertBox;
import Models.LocalTree;
import Models.RemoteTree;
import Models.SSHTask;
import Models.SSHWrapper;
import Models.WizardView;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class DataSelectionViewController extends WizardView implements Initializable {

    @FXML
    private Button back;
    @FXML
    private Button next;
    @FXML
    private TreeView<ChannelSftp.LsEntry> azizBrowser;
    @FXML
    private TreeView<File> localBrowser;
    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        constractLocalTree();
        constractRemoteTree();
    }


    @FXML
    private void next(ActionEvent event
    ) {
        if (!(localBrowser.getSelectionModel().getSelectedItem()==null)) {
            Platform.runLater(()->loadingIndicator.setVisible(true));
            wizard.script.uploadInputFile(localBrowser.getSelectionModel().getSelectedItem().getValue().getAbsolutePath());
            super.wizard.script.getInputFile().bind(new SimpleStringProperty(localBrowser.getSelectionModel().getSelectedItem().getValue().getName()));
            super.wizard.next(event);
        } else if (!azizBrowser.getSelectionModel().isEmpty()) {
            super.wizard.script.getInputFile().bind(new SimpleStringProperty(azizBrowser.getSelectionModel().getSelectedItem().getValue().getFilename()));
            super.wizard.next(event);
        } else {
            AlertBox.display("Input Error!", "Please select an input file");
        }
    }

    public void constractLocalTree() {
        TreeItem localRoot = new TreeItem<File>();
        LocalTree.constructTree(SSHWrapper.GetLocalHomeFolder(), localRoot);
        localBrowser.autosize();
        localBrowser.setRoot(localRoot);
        localBrowser.setShowRoot(false);
        localBrowser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue().getChildren().contains(null) && newValue.getValue().isDirectory()) {
                newValue.getChildren().remove(null);
                LocalTree.constructTree(newValue.getValue().getAbsolutePath(), newValue);
            }
        });
        localBrowser.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> tv) {
                return new TreeCell<File>() {

                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);

                        setText((empty || item == null) ? "" : item.getName());
                    }
                };
            }
        });

    }

    public void constractRemoteTree() {
        TreeItem root = new TreeItem<ChannelSftp.LsEntry>();
        try {
            new RemoteTree().constructTree(SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "/datasets", root);
        } catch (SftpException | JSchException ex) {
            Logger.getLogger(DataSelectionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        azizBrowser.setRoot(root);
        azizBrowser.autosize();
        azizBrowser.setRoot(root);
        azizBrowser.setShowRoot(false);
        azizBrowser.setCellFactory(new Callback<TreeView<ChannelSftp.LsEntry>, TreeCell<ChannelSftp.LsEntry>>() {
            @Override
            public TreeCell<ChannelSftp.LsEntry> call(TreeView<ChannelSftp.LsEntry> tv) {
                return new TreeCell<ChannelSftp.LsEntry>() {

                    @Override
                    protected void updateItem(ChannelSftp.LsEntry item, boolean empty) {
                        super.updateItem(item, empty);

                        setText((empty || item == null) ? "" : item.getFilename());
                    }
                };
            }
        });
    }

}
