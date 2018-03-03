package Models;

import com.jcraft.jsch.ChannelSftp;
import java.util.Vector;
import javafx.beans.property.*;

public class Script implements SSHListener {

    private SimpleStringProperty name;
    private SimpleStringProperty wallTime;
    private SimpleStringProperty queue;
    private SimpleBooleanProperty month;
    private SimpleIntegerProperty nodes;
    private SimpleIntegerProperty threads;
    private SimpleStringProperty inputFile;
    private SimpleStringProperty outputName;
    private SimpleBooleanProperty overWrite;
    private SimpleBooleanProperty outputType;
    private SimpleStringProperty inputType;
    private SimpleStringProperty ocurrence;
    private SimpleIntegerProperty motifNumber;
    private SimpleStringProperty maxMotifSites;
    private SimpleStringProperty minMotifSites;
    private SimpleBooleanProperty exactMotifSites;
    private SimpleDoubleProperty bias;
    private SimpleIntegerProperty motifLength;
    private SimpleStringProperty maxMotifLength;
    private SimpleStringProperty minMotifLength;
    private SimpleStringProperty gapOpeningCost;
    private SimpleStringProperty gapExtensionCost;
    private SimpleBooleanProperty trimming;
    private SimpleBooleanProperty noEndGaps;
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
        outputName = new SimpleStringProperty();
        overWrite = new SimpleBooleanProperty();
        outputType = new SimpleBooleanProperty();
        inputType = new SimpleStringProperty();
        ocurrence = new SimpleStringProperty();
        motifNumber = new SimpleIntegerProperty();
        maxMotifSites = new SimpleStringProperty();
        minMotifSites = new SimpleStringProperty();
        exactMotifSites = new SimpleBooleanProperty();
        bias = new SimpleDoubleProperty();
        motifLength = new SimpleIntegerProperty();
        maxMotifLength = new SimpleStringProperty();
        minMotifLength = new SimpleStringProperty();
        gapOpeningCost = new SimpleStringProperty();
        gapExtensionCost = new SimpleStringProperty();
        trimming = new SimpleBooleanProperty();
        noEndGaps = new SimpleBooleanProperty();
        scriptVal = new SimpleStringProperty();
    }

    public void init() {
        scriptVal.setValue(toString());
    }

    public SimpleStringProperty getScriptVal() {
        return scriptVal;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public SimpleStringProperty getWallTime() {
        return wallTime;
    }

    public SimpleStringProperty getQueue() {
        return queue;
    }

    public SimpleBooleanProperty getMonth() {
        return month;
    }

    public SimpleIntegerProperty getNodes() {
        return nodes;
    }

    public SimpleIntegerProperty getThreads() {
        return threads;
    }

    public SimpleStringProperty getInputFile() {
        return inputFile;
    }

    public SimpleStringProperty getOutputName() {
        return outputName;
    }

    public SimpleBooleanProperty getOverWrite() {
        return overWrite;
    }

    public SimpleBooleanProperty getOutputType() {
        return outputType;
    }

    public SimpleStringProperty getInputType() {
        return inputType;
    }

    public SimpleStringProperty getOcurrence() {
        return ocurrence;
    }

    public SimpleIntegerProperty getMotifNumber() {
        return motifNumber;
    }

    public SimpleStringProperty getMaxMotifSites() {
        return maxMotifSites;
    }

    public SimpleStringProperty getMinMotifSites() {
        return minMotifSites;
    }

    public SimpleBooleanProperty getExactMotifSites() {
        return exactMotifSites;
    }

    public SimpleDoubleProperty getBias() {
        return bias;
    }

    public SimpleIntegerProperty getMotifLength() {
        return motifLength;
    }

    public SimpleStringProperty getMaxMotifLength() {
        return maxMotifLength;
    }

    public SimpleStringProperty getMinMotifLength() {
        return minMotifLength;
    }

    public SimpleStringProperty getGapOpeningCost() {
        return gapOpeningCost;
    }

    public SimpleStringProperty getGapExtensionCost() {
        return gapExtensionCost;
    }

    public SimpleBooleanProperty getTrimming() {
        return trimming;
    }

    public SimpleBooleanProperty getNoEndGaps() {
        return noEndGaps;
    }

    @Override
    public String toString() {
        //  System.out.println(nodes.getValue());
        return "#PBS -l select=" + nodes.getValue() + ":ncpus=" + threads.getValue() + "\n"
                + "#PBS -q " + queue.getValue() + (month.getValue() ? "-1m" : "") + "\n"
                + "#PBS -N " + name.getValue() + "\n"
                + "cd /home/" + SSHWrapper.username + "/app/meme/bin\n"
                + "./meme " + inputFile.getValue() + " -" + inputType.getValue() + " -o" + (overWrite.getValue() ? "c" : "") + SSHWrapper.GetRemoteHomeFolder()+SSHWrapper.GetABGFolder()+"/jobs/"+outputName.getName()
                + (outputType.getValue() ? " -text" : "") + " -mod " + ocurrence.getValue() + " -nmotif " + motifNumber.getValue() + (exactMotifSites.getValue() ? " -nsites " + maxMotifSites.getValue() : "")
                + " -minsites " + minMotifSites.getValue() + " -maxsites " + maxMotifSites.getValue() + " -wnsites " + bias.getValue() + " -w " + motifLength.getValue()
                + " -minw " + minMotifLength.getValue() + " -maxw " + maxMotifLength.getValue() + (trimming.getValue() ? " -nomatrim " : "") + "-wg " + gapOpeningCost.getValue() + " -ws " + gapExtensionCost.getValue() + (noEndGaps.getValue() ? " -noendgaps" : "");
    }

    public void submit() {
        while (th != null && th.isAlive()) {
        }
        th = new Thread(new SSHTask(this, "/bin/echo " + scriptVal.getValue() + " > /home/" + SSHWrapper.username + "/ABG/jobs/" + name.getValue()));
        th.setDaemon(true);
        th.start();
        th = new Thread(new SSHTask(this, "/opt/pbs/default/bin/qsub /home/" + SSHWrapper.username + "/ABG/jobs/" + name.getValue()));
        th.setDaemon(true);
        th.start();
    }

    public void uploadInputFile(String path) {
        String temp[] = path.split("/");
        th = new Thread(new SSHTask(this, path, SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "datasets/" + temp[temp.length - 1], SSHTask.TaskType.UploadFile));
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void sshResponse(String strCommand, String strResponse) {
        System.out.println(strResponse);
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
        System.out.println("File Uploaded");
    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
