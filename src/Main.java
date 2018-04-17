
import Controllers.SignInViewController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, FileNotFoundException, ClassNotFoundException, BackingStoreException {
        primaryStage.setTitle("Aziz Bioinformatics Gateway");
        new SignInViewController().launch(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
