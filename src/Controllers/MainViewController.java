package Controllers;

import Models.JobItem;
import Models.Jobs;
import Models.SSHListener;
import Models.SSHTask;
import Models.SSHWrapper;
import Models.Script;
import Models.SftpTask;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainViewController implements Initializable, SSHListener {

    private File ABG;
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Thread thread;
    private Vector<ChannelSftp.LsEntry> list;
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
    TableView<JobItem> jobsTable;
    @FXML
    Pane pane;
    @FXML
    ContextMenu contextMenu;
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
                    Thread.sleep(10000);
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
        Platform.runLater(() -> progressIndicator.setVisible(true));
        File textOut = new File(ABG.getAbsolutePath() + "/" + job.getOutputName() + "/" + "meme.txt");
        File htmlOut = new File(ABG.getAbsolutePath() + "/" + job.getOutputName() + "/" + "meme.html");
        File error = new File(ABG.getAbsolutePath() + "/" + job.getOutputName() + "/" + job.getName() + ".e" + job.getId());
        if (!textOut.exists() && !htmlOut.exists() && !error.exists()) {
            getOutputDirectory(job);
        }
        if (htmlOut.exists()) {
            setHtmlURI(htmlOut, job);
            showHtmlOutput(job);
        }
        if (textOut.exists()) {
            setTextURI(textOut, job);
            showTextOutput(job);
        }
        if (!error.exists()) {
            getErrorFile(job, error);
            showTextOutput(job);
        }
        Platform.runLater(() -> progressIndicator.setVisible(false));
    }

    private void getOutputDirectory(JobItem job) throws InterruptedException {
        File output = new File(ABG.getAbsolutePath() + "/" + job.getOutputName() + "/");
        output.mkdirs();
        th = new Thread(new SftpTask(this, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + job.getOutputName(), SftpTask.TaskType.ListFile));
        th.setDaemon(true);
        th.start();
        th.join();
        for (int i = 2; i < list.size(); i++) {
            th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + job.getOutputName() + "/" + list.get(i).getFilename(), output.getAbsolutePath() + "/" + list.get(i).getFilename(), SSHTask.TaskType.DownloadFile));
            th.setDaemon(true);
            th.start();
            th.join();
        }
    }

    private void getErrorFile(JobItem job, File error) throws InterruptedException {
        th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + "/*.e" + job.getId(), error.getAbsolutePath(), SSHTask.TaskType.DownloadFile));
        th.setDaemon(true);
        th.start();
        th.join();
        job.setOutputText(error.toURI());
    }

    private void setTextURI(File output, JobItem job) {
        job.setOutputText(output.toURI());
    }

    private void setHtmlURI(File output, JobItem job) {
        job.setOutputHTML(output.toURI());
    }

    private void showTextOutput(JobItem job) {
        try {
            Desktop.getDesktop().browse(job.getOutputText());
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showHtmlOutput(JobItem job) {
        try {
            Desktop.getDesktop().browse(job.getOutputHTML());
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadContent() throws IOException, FileNotFoundException, ClassNotFoundException, BackingStoreException, InterruptedException {
        ABG = new File(SSHWrapper.GetLocalHomeFolder() + "/ABG/");
        if (!ABG.exists()) {
            System.out.println(ABG.getAbsolutePath());
            ABG.mkdir();
        }
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
        }
        updatingDaemon();
        flag = false;

    }

    public void addJob() {
        newJob();
    }

    public void removeJob() {
        deleteJob();
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
        if (!jobsTable.getSelectionModel().isEmpty() && jobsTable.getSelectionModel().getSelectedItem().getStatus().equalsIgnoreCase("Running")) {
            th = new Thread(new SSHTask(this, "/opt/pbs/default/bin/qdel " + (jobsTable.getSelectionModel().getSelectedItem()).getId()));
            th.setDaemon(true);
            th.start();

        }
        new Thread(new SSHTask(this, "/bin/rm " + SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + jobsTable.getSelectionModel().getSelectedItem().getOutputName())).start();
        new Thread(new SSHTask(this, "/bin/rm " + SSHWrapper.GetRemoteHomeFolder() + "/" + jobsTable.getSelectionModel().getSelectedItem().getName() + ".*" + jobsTable.getSelectionModel().getSelectedItem().getId())).start();
        jobs.removeJob(jobsTable.getSelectionModel().getSelectedItem());
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        list = lstItems;
    }

    @FXML
    private void showScript(ActionEvent event) throws InterruptedException, IOException {
        Platform.runLater(() -> progressIndicator.setVisible(true));
        if (!jobsTable.getSelectionModel().isEmpty()) {
            JobItem job = jobsTable.getSelectionModel().getSelectedItem();
            File script = new File(ABG.getAbsolutePath() + "/" + job.getOutputName() + "/script");
            if (!script.exists()) {
                script.mkdirs();
                getScriptFile(job, script);
            }

            if (script.exists()) {
                setScriptURI(job, script);
                showScript(job);
            }
        }
        Platform.runLater(() -> progressIndicator.setVisible(false));
    }

    private void showScript(JobItem job) throws IOException {
        Desktop.getDesktop().browse(job.getScript());
    }

    private void setScriptURI(JobItem job, File script) throws IOException {
        job.setScript(script.toURI());
        Desktop.getDesktop().browse(job.getScript());
    }

    private void getScriptFile(JobItem job, File script) throws InterruptedException {
        th = new Thread(new SSHTask(this, SSHWrapper.GetRemoteHomeFolder() + "/" + SSHWrapper.GetABGFolder() + "jobs/" + job.getName(), script.getAbsolutePath() + "/" + job.getName(), SSHTask.TaskType.DownloadFile));
        th.setDaemon(true);
        th.start();
        th.join();
    }

    public void close() {
        System.exit(0);
    }

}
