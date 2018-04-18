package Models;

import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.collections.ObservableList;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class Jobs implements SSHListener {

    private ObservableList<JobItem> jobs = FXCollections.observableArrayList();
    private FileOutputStream fileOutput;
    private ObjectOutputStream objectOutput;
    private FileInputStream fileInput;
    private ObjectInputStream objectInput;
    static SSHTask AuthTasks;
    static Preferences pref;

    File file;
    SimpleStringProperty fileu = new SimpleStringProperty();
    public Thread th = new Thread();

    public Jobs() {

    }

    public void addJob(JobItem e) {
        jobs.add(e);
        try {
            saveData();
        } catch (IOException ex) {
            Logger.getLogger(Jobs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeJob(JobItem e) {
        jobs.remove(e);
    }

    public void init() throws IOException, ClassNotFoundException, BackingStoreException {
        pref = Preferences.userRoot().node(this.getClass().getName());
        file = new File("Data.SER");
        this.downloadData();
    }

    private void loadData() throws FileNotFoundException, IOException, ClassNotFoundException {
        fileInput = new FileInputStream(pref.get("data", null));
        objectInput = new ObjectInputStream(fileInput);
        List<JobItem> list = (ArrayList<JobItem>) objectInput.readObject();
        jobs = FXCollections.observableArrayList(list);
        fileInput.close();
        objectInput.close();
    }

    private void prepData() throws IOException, FileNotFoundException, ClassNotFoundException {
        try {
            File backUp = (File) getClass().getClassLoader().getResource(pref.absolutePath() + File.separator + "bData.SER").getContent();
            File data = (File) getClass().getClassLoader().getResource(pref.absolutePath() + File.separator + "Data.SER").getContent();
            if (backUp.lastModified() < data.lastModified()) {
                backUp.renameTo(data);
            }
        } catch (Exception ex) {
            pref.put("data", file.toString());
        }
        loadData();
    }

    private void downloadData() throws ClassNotFoundException {
        while (th.isAlive()) {
        }
        th = new Thread(new SSHTask(this, "/home/" + SSHWrapper.username + "/ABG/config/Data.SER", file.getAbsolutePath(), SSHTask.TaskType.DownloadFile));
        th.setDaemon(true);
        th.start();
    }

    public void saveData() throws FileNotFoundException, IOException {
        update();
        file = new File("Data.SER");
        fileOutput = new FileOutputStream(file);
        objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(new ArrayList<JobItem>(jobs));
        objectOutput.close();
        fileOutput.close();
        uploadData();
    }

    public void update() {
        for (JobItem job : this.jobs) {
            if (!job.getStatus().equalsIgnoreCase("Finished")) {
                new Thread(new SSHTask(this, "/opt/pbs/default/bin/qstat -x " + job.getId())).start();
            }
        }

    }

    public void updateJob(String strCommand, String strResponse) {
        for (JobItem job : jobs) {
            if (job.getId().equalsIgnoreCase(strCommand.split(" ")[2])) {
                if (strResponse.contains("Unknown")) {
                    job.setStatus("Error");
                } else {
                    strResponse = strResponse.replaceAll(" +", " ");
                    switch (strResponse.split(" ")[16]) {
                        case "R":
                            job.setStatus("Running");
                            break;
                        case "F":
                            job.setStatus("Finished");
                            break;
                        case "E":
                            job.setStatus("Error");
                            break;
                        default:
                            job.setStatus("Queued");
                    }
                    if (job.getStatus().equalsIgnoreCase("Running")) {
                        job.setCpuTime(strResponse.split(" ")[15]);
                    }

                }
            }
        }
    }

    public ObservableList<JobItem> getJobs() {
        return jobs;
    }

    private void uploadData() {
        while (th.isAlive()) {
        }
        th = new Thread(new SSHTask(this, file.getAbsolutePath(), "/home/" + SSHWrapper.username + "/ABG/config/Data.SER", SSHTask.TaskType.UploadFile));
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void sshResponse(String strCommand, String strResponse) {
       
        if (strCommand.contains("qstat")) {
            updateJob(strCommand, strResponse);
        }
    }

    @Override
    public void FileDownloadResponse(String strFilePath, Boolean bStatus) {
        try {
            prepData();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Jobs.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void FileUploadResponse(String strFilePath, Boolean bStatus) {

    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
