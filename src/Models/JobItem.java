package Models;

import java.io.File;
import java.io.Serializable;

public class JobItem implements Serializable {

    public String Id;
    public String Name;
    public String DateCreated;
    public String CpuTime;
    public String WallTime;
    public String CPUs;
    public String Nodes;
    public String Status;
  //  public Script script;
//    public File output;

    public JobItem(String Id, String DateCreated, String Status, String name, String wallTime, String cpusNum, String nodesNum, Script script) {
        this.Id = Id;
        this.DateCreated = DateCreated;
        this.Status = Status;
        this.Name = name;
        this.WallTime = wallTime;
        this.CPUs = cpusNum;
        this.Nodes = nodesNum;
        this.CpuTime = "00:00:00";
    //    this.script = script;
    }

    public JobItem(String strStatus) {
        //|321541          | STDIN           | shejazi0004     | cheistry | 86:41:12        | 606:38:17    | 24    | 1   | 12     |
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

//    public Script getScript() {
//        return script;
//    }
//
//    public void setScript(Script script) {
//        this.script = script;
//    }
//
//    public File getOutput() {
//        return output;
//    }
//
//    public void setOutput(File output) {
//        this.output = output;
//    }
//    
    

}
