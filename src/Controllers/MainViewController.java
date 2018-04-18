package Controllers;

import Models.JobItem;
import Models.Jobs;
import Models.SSHListener;
import Models.SSHTask;
import Models.SSHWrapper;
import Models.Script;
import com.jcraft.jsch.ChannelSftp;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainViewController implements Initializable, SSHListener {

    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Thread thread;
    public Jobs jobs;
    public boolean flag;
    public WizardController wizard = new WizardController();
    Thread th;
    @FXML
    Button add;
    @FXML
    Button delete;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    TableView jobsTable;
    @FXML
    Pane pane;
    SimpleDoubleProperty sceneWidth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jobsTable.setRowFactory(tableView -> {
            TableRow<JobItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) && row.getItem().getStatus().equalsIgnoreCase("Finished")) {
                    try {
                        try {
                            showOutput(row.getItem());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row;

        });
        getDB();
    }

    private void getDB() {
        File tmp = new File("motif_db.csv");
        th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + "/app/meme/db/motif_databases/motif_db.csv", tmp.getAbsolutePath(), SSHTask.TaskType.DownloadFile));
        th.setDaemon(true);
        th.start();
    }

    public void updatingDaemon() {
        Thread th = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15000);
                    if (!wizard.script.jobID.isEmpty() && flag) {
                        System.out.println(wizard.script.getOutputName().getValue() + "!!!!!!!!!!!");
                        jobs.addJob(new JobItem(wizard.script.jobID, LocalDate.now().toString(), "Queued", wizard.script.getName().getValue(), wizard.script.getWallTime().getValue(), wizard.script.getNodes().getValue() + "", wizard.script.getThreads().getValue() + "", wizard.script.getOutputName().getValue()));
                        jobsTable.refresh();
                        wizard.script = new Script();
                    }
                    flag = false;
                    jobs.saveData();
                } catch (InterruptedException | IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.runLater(() -> progressIndicator.setVisible(true));

                Platform.runLater(() -> {
                    jobsTable.refresh();
                    progressIndicator.setVisible(false);
                });

            }
        });
        th.setDaemon(true);
        th.start();
    }

    public MainViewController() throws IOException, FileNotFoundException, ClassNotFoundException, BackingStoreException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/MainView.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = (Parent) fxmlLoader.load();
            scene = new Scene(parent);
        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }
        progressIndicator.layoutXProperty().bind(scene.widthProperty().subtract(40));
        loadContent();
        jobsTable.setItems(jobs.getJobs());

    }

    public void launch(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();

    }

    private void showOutput(JobItem job) throws IOException, InterruptedException {
        if (job.getOutput() == null) {
            File output = new File(job.getOutputName());
            th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "/jobs/" + job.getOutputName(), output.getAbsolutePath(), SSHTask.TaskType.DownloadFile));
            th.setDaemon(true);
            th.start();
            th.join();
            if (output.exists()) {
                job.setOutput(output);
                Desktop.getDesktop().browse(job.getOutput().toURI());
            } else {
                output = new File(job.getName());
                th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "/jobs/" + job.getName(), output.getAbsolutePath(), SSHTask.TaskType.DownloadFile));
                th.setDaemon(true);
                th.start();
                th.join();
                job.setOutput(output);
                Desktop.getDesktop().browse(job.getOutput().toURI());
            }
        } else {
            Desktop.getDesktop().browse(job.getOutput().toURI());
        }

    }

    private void loadContent() throws IOException, FileNotFoundException, ClassNotFoundException, BackingStoreException, InterruptedException {
        progressIndicator.setVisible(true);
        thread = new Thread(new SSHTask(this, "/usr/bin/file /home/" + SSHWrapper.username + "/ABG/config/Data.SER"));
        thread.setDaemon(true);
        thread.start();
        thread.join();
        if (flag) {
            thread = new Thread(new SSHTask(this, "/bin/mkdir -p  /home/" + SSHWrapper.username + "/ABG/{config,datasets,jobs}"));
            thread.setDaemon(true);
            thread.start();
            jobs = new Jobs();
        } else {
            jobs = new Jobs();
            jobs.init();
            jobs.th.join();
            updatingDaemon();
        }
        flag = false;

    }

    public void newJob() {
        Platform.runLater(() -> progressIndicator.setVisible(true));
        wizard = new WizardController();
        wizard.launch(new Stage());
        wizard.stage.setOnCloseRequest(e -> {
            flag = true;
        });
    }

    public void deleteJob() {
        jobs.removeJob((JobItem) jobsTable.getSelectionModel().getSelectedItem());

    }

    @Override
    public void sshResponse(String strCommand, String strResponse) {
        Platform.runLater(() -> progressIndicator.setVisible(false));
        if (strResponse.contains("cannot")) {
            flag = true;
        }
    }

    @Override
    public void FileDownloadResponse(String strFilePath, Boolean bStatus) {
        System.out.println("Clear");
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
