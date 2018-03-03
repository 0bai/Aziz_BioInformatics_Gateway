/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.AlertBox;
import Models.SSHConnectionManager;
import Models.SSHListener;
import Models.SSHTask;
import Models.SSHWrapper;
import com.jcraft.jsch.ChannelSftp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author OBAI
 */
public class SignInViewController implements Initializable, SSHListener {
    
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Task<Boolean> task;
    private Thread thread;
    private StringProperty username;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordF;
    @FXML
    private Button signInBT;
    @FXML
    private Button cancelBT;
    static SSHTask AuthTask;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        usernameTF.textProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                usernameTF.setStyle("-fx-text-box-border: red ;-fx-focus-color: red ;");
            } else {
                usernameTF.setStyle("-fx-text-box-border: green ;-fx-focus-color: green ;");
            }
        });
        
        passwordF.textProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                passwordF.setStyle("-fx-text-box-border: red ;-fx-focus-color: red ;");
            } else {
                passwordF.setStyle("-fx-text-box-border: green ;-fx-focus-color: green ;");
            }
        });
        
    }
    
    public SignInViewController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/SignInView.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            // set height and width here for this login scene
            scene = new Scene(parent, 309, 129);
        } catch (IOException ex) {
            System.out.println("Error displaying login window");
            throw new RuntimeException(ex);
        }
    }
    
    public void launch(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public void SignIn() throws UnknownHostException, IOException, Exception, Throwable {
//        usernameTF.setText("obai");
//        passwordF.setText("0ba1Alnajjar");
        usernameTF.setText("onajjar0001");
        passwordF.setText("hphf9Nr2X");
        if (!usernameTF.getText().equalsIgnoreCase("") && !passwordF.getText().equalsIgnoreCase("")) {
            Platform.runLater(() -> progressIndicator.setVisible(true));
            
            thread = new Thread(task = new Task() {
                @Override
                protected Boolean call() throws Exception {
                    return InetAddress.getByName(SSHWrapper.host).isReachable(30000);
                }
            });
            thread.setDaemon(true);
            thread.start();
            
            if (task.get()) {
                SSHWrapper.SetCredentials(usernameTF.getText(), passwordF.getText(), SSHWrapper.host, 22);
                SSHConnectionManager.SetCredentials(usernameTF.getText(), passwordF.getText(), SSHWrapper.host);
                thread = new Thread(AuthTask = new SSHTask(this, "/usr/bin/id"));
                thread.setDaemon(true);
                thread.start();
            } else {
                sshResponse("Connection Error", "Please Check Your VPN !");
            }
            
        }
        
    }
    
    public void cancelM() {
        stage.close();
    }
    
    @Override
    public void sshResponse(String strCommand, String strResponse) {
        
        if (strResponse.equalsIgnoreCase("")) {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                AlertBox.display("Login Error", "Incorrect Username or Password !");
            });
        } else if (strResponse.equalsIgnoreCase("Please Check Your VPN !")) {
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                AlertBox.display(strCommand, strResponse);
            });
        } else {
            
            Platform.runLater(() -> {
                stage.close();
                try {
                    new MainViewController().launch(stage);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(SignInViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BackingStoreException ex) {
                    Logger.getLogger(SignInViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SignInViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        
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
