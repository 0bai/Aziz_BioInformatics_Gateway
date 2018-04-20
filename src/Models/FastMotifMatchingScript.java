package Models;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FastMotifMatchingScript extends Script {

    private SimpleIntegerProperty motifNumber;
    private SimpleIntegerProperty motifLength;
    private SimpleIntegerProperty mutations;

    public FastMotifMatchingScript() {
        motifLength = new SimpleIntegerProperty();
        motifNumber = new SimpleIntegerProperty();
        mutations = new SimpleIntegerProperty();
        outputName = new SimpleStringProperty();
    }

    public SimpleIntegerProperty getMotifNumber() {
        return motifNumber;
    }

    public void setMotifNumber(SimpleIntegerProperty motifNumber) {
        this.motifNumber = motifNumber;
    }

    public SimpleIntegerProperty getMotifLength() {
        return motifLength;
    }

    public void setMotifLength(SimpleIntegerProperty motifLength) {
        this.motifLength = motifLength;
    }

    public SimpleIntegerProperty getMutations() {
        return mutations;
    }

    public void setMutations(SimpleIntegerProperty mutations) {
        this.mutations = mutations;
    }

    @Override
    public String toString() {
        return (super.toString() + "cd /home/" + SSHWrapper.username + "/app/meme/bin\n"
                + "./fastMotif " + SSHWrapper.GetRemoteHomeFolder()+SSHWrapper.GetABGFolder() + "datasets/" + super.getInputFile().getValue() + " "+ motifLength.getValue()+" "+motifNumber.getValue()+" "+mutations.getValue()+" > "+ SSHWrapper.GetRemoteHomeFolder()+SSHWrapper.GetABGFolder()+"jobs/"+outputName.getValue()+"/meme.txt");
    }

    public void submit() {
        super.setScriptVal(new SimpleStringProperty(toString()));
        try {
            super.submit(toString());
        } catch (InterruptedException ex) {
            Logger.getLogger(MotifDiscoveryScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
