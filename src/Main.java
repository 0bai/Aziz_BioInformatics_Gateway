/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Controllers.MainViewController;
import Controllers.SignInViewController;
import Controllers.WizardController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author OBAI
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException, FileNotFoundException, ClassNotFoundException, BackingStoreException {
        primaryStage.setTitle("Aziz Bioinformatics Gateway");
        new SignInViewController().launch(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
