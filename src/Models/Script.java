package Models;

import com.jcraft.jsch.ChannelSftp;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;

public class Script implements SSHListener, Serializable {

    private SimpleStringProperty name;
    private SimpleStringProperty wallTime;
    private SimpleStringProperty queue;
    private SimpleBooleanProperty month;
    private SimpleIntegerProperty nodes;
    private SimpleIntegerProperty threads;
    private SimpleStringProperty inputFile;
    protected SimpleStringProperty outputName;
    private SimpleStringProperty scriptVal;
    public Thread th;
    public String jobID = "";

    public Script() {
        name = new SimpleStringProperty();
        wallTime = new SimpleStringProperty();
        queue = new SimpleStringProperty();
        month = new SimpleBooleanProperty();
        nodes = new SimpleIntegerProperty();
        threads = new SimpleIntegerProperty();
        inputFile = new SimpleStringProperty();
        scriptVal = new SimpleStringProperty();
    }

    public Script(SimpleStringProperty name, SimpleStringProperty wallTime, SimpleStringProperty queue, SimpleBooleanProperty month, SimpleIntegerProperty nodes, SimpleIntegerProperty threads, SimpleStringProperty inputFile) {
        this.name = name;
        this.wallTime = wallTime;
        this.queue = queue;
        this.month = month;
        this.nodes = nodes;
        this.threads = threads;
        this.inputFile = inputFile;
    }

    public SimpleStringProperty getOutputName() {
        return outputName;
    }

    public void setOutputName(SimpleStringProperty outputName) {
        this.outputName = outputName;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public SimpleStringProperty getWallTime() {
        return wallTime;
    }

    public void setWallTime(SimpleStringProperty wallTime) {
        this.wallTime = wallTime;
    }

    public SimpleStringProperty getQueue() {
        return queue;
    }

    public void setQueue(SimpleStringProperty queue) {
        this.queue = queue;
    }

    public SimpleBooleanProperty getMonth() {
        return month;
    }

    public void setMonth(SimpleBooleanProperty month) {
        this.month = month;
    }

    public SimpleIntegerProperty getNodes() {
        return nodes;
    }

    public void setNodes(SimpleIntegerProperty nodes) {
        this.nodes = nodes;
    }

    public SimpleIntegerProperty getThreads() {
        return threads;
    }

    public void setThreads(SimpleIntegerProperty threads) {
        this.threads = threads;
    }

    public SimpleStringProperty getInputFile() {
        return inputFile;
    }

    public void setInputFile(SimpleStringProperty inputFile) {
        this.inputFile = inputFile;
    }

    public SimpleStringProperty getScriptVal() {
        return scriptVal;
    }

    public void setScriptVal(SimpleStringProperty scriptVal) {
        this.scriptVal = scriptVal;
    }

    public Thread getTh() {
        return th;
    }

    public void setTh(Thread th) {
        this.th = th;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public String toString() {
        return "#PBS -l select=" + nodes.getValue() + ":ncpus=" + threads.getValue() + "\n"
                + "#PBS -q " + queue.getValue() + (month.getValue() ? "-1m" : "") + "\n"
                + "#PBS -N " + name.getValue() + "\n";
    }

    public void submit() throws InterruptedException {
        if (th != null) {
            th.join();
        }
        th = new Thread(new SSHTask(this, "/bin/echo " + scriptVal.getValue() + " > /home/" + SSHWrapper.username + "/ABG/jobs/" + name.getValue()));
        th.setDaemon(true);
        th.start();
        th.join();
        th = new Thread(new SSHTask(this, "/opt/pbs/default/bin/qsub /home/" + SSHWrapper.username + "/ABG/jobs/" + name.getValue()));
        th.setDaemon(true);
        th.start();
        th.join();
    }

    public void uploadInputFile(String path) {
        String temp[] = path.split("/");
        th = new Thread(new SSHTask(this, path, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "datasets/" + temp[temp.length - 1], SSHTask.TaskType.UploadFile));
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void sshResponse(String strCommand, String strResponse) {
        System.out.println(strResponse + "!");
        if (strCommand.contains("qsub")) {
            jobID = strResponse.split("\\.")[0];
        }
    }

    @Override
    public void FileDownloadResponse(String strFilePath, Boolean bStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FileUploadResponse(String strFilePath, Boolean bStatus) {
        System.out.println("File Uploaded!");
    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
