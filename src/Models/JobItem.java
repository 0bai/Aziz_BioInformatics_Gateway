package Models;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

public class JobItem implements Serializable {

    public String Id;
    public String Name;
    public String DateCreated;
    public String CpuTime;
    public String WallTime;
    public String outputName;
    public String CPUs;
    public String Nodes;
    public String Status;
    public URI script;
    public URI outputText;
    public URI outputHTML;

    public JobItem(String Id, String DateCreated, String Status, String name, String wallTime, String cpusNum, String nodesNum, String outputName) {
        this.Id = Id;
        this.DateCreated = DateCreated;
        this.Status = Status;
        this.Name = name;
        this.WallTime = wallTime;
        this.CPUs = cpusNum;
        this.Nodes = nodesNum;
        this.CpuTime = "00:00:00";
        this.outputName = outputName;
    }

    public JobItem(String strStatus) {
        String info[] = strStatus.split("\\|");
        this.Id = info[1].trim();
        this.Name = info[2].trim();
        this.CpuTime = info[4].trim();
        this.WallTime = info[5].trim();
        this.CPUs = info[7].trim();
        this.Nodes = info[8].trim();
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }

    public String getCpuTime() {
        return CpuTime;
    }

    public void setCpuTime(String CpuTime) {
        this.CpuTime = CpuTime;
    }

    public String getWallTime() {
        return WallTime;
    }

    public void setWallTime(String WallTime) {
        this.WallTime = WallTime;
    }

    public String getCPUs() {
        return CPUs;
    }

    public void setCPUs(String CPUs) {
        this.CPUs = CPUs;
    }

    public String getNodes() {
        return Nodes;
    }

    public void setNodes(String Nodes) {
        this.Nodes = Nodes;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public URI getScript() {
        return script;
    }

    public void setScript(URI script) {
        this.script = script;
    }

    public URI getOutputText() {
        return outputText;
    }

    public void setOutputText(URI outputText) {
        this.outputText = outputText;
    }

    public URI getOutputHTML() {
        return outputHTML;
    }

    public void setOutputHTML(URI outputHTML) {
        this.outputHTML = outputHTML;
    }

    
    

}
