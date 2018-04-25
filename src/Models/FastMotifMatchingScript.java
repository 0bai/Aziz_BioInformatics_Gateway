package Models;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class FastMotifMatchingScript extends Script {

    private SimpleIntegerProperty sequenceLength;
    private SimpleIntegerProperty sequenceNumber;

    public FastMotifMatchingScript() {
        sequenceLength = new SimpleIntegerProperty();
        sequenceNumber = new SimpleIntegerProperty();
        outputName = new SimpleStringProperty();
    }

    public SimpleIntegerProperty getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(SimpleIntegerProperty sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public SimpleIntegerProperty getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(SimpleIntegerProperty sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return (super.toString() + "module load composer_xe/2015.2.164 impi/5.0.3.048\n"
                + "cd " + SSHWrapper.GetRemoteHomeFolder() + "/Motif-Obai\n"
                + "./main s " + SSHWrapper.GetRemoteHomeFolder() + "/" + outputName.getValue() + " " + sequenceNumber.getValue() + " " + sequenceLength.getValue());
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
