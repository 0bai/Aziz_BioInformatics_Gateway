package Models;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MotifDiscoveryScript extends Script {

    private SimpleBooleanProperty overWrite;
    private SimpleBooleanProperty outputType;
    private SimpleStringProperty inputType;
    private SimpleStringProperty ocurrence;
    private SimpleIntegerProperty motifNumber;
    private SimpleIntegerProperty maxMotifSites;
    private SimpleIntegerProperty minMotifSites;
    private SimpleBooleanProperty exactMotifSites;
    private SimpleDoubleProperty bias;
    private SimpleIntegerProperty motifLength;
    private SimpleIntegerProperty maxMotifLength;
    private SimpleIntegerProperty minMotifLength;
    private SimpleIntegerProperty gapOpeningCost;
    private SimpleIntegerProperty gapExtensionCost;
    private SimpleBooleanProperty trimming;
    private SimpleBooleanProperty noEndGaps;

    public MotifDiscoveryScript() {
        outputName = new SimpleStringProperty();
        overWrite = new SimpleBooleanProperty();
        outputType = new SimpleBooleanProperty();
        inputType = new SimpleStringProperty();
        ocurrence = new SimpleStringProperty();
        motifNumber = new SimpleIntegerProperty();
        maxMotifSites = new SimpleIntegerProperty();
        minMotifSites = new SimpleIntegerProperty();
        exactMotifSites = new SimpleBooleanProperty();
        bias = new SimpleDoubleProperty();
        motifLength = new SimpleIntegerProperty();
        maxMotifLength = new SimpleIntegerProperty();
        minMotifLength = new SimpleIntegerProperty();
        gapOpeningCost = new SimpleIntegerProperty();
        gapExtensionCost = new SimpleIntegerProperty();
        trimming = new SimpleBooleanProperty();
        noEndGaps = new SimpleBooleanProperty();
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

    public SimpleIntegerProperty getMaxMotifSites() {
        return maxMotifSites;
    }

    public SimpleIntegerProperty getMinMotifSites() {
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

    public SimpleIntegerProperty getMaxMotifLength() {
        return maxMotifLength;
    }

    public SimpleIntegerProperty getMinMotifLength() {
        return minMotifLength;
    }

    public SimpleIntegerProperty getGapOpeningCost() {
        return gapOpeningCost;
    }

    public SimpleIntegerProperty getGapExtensionCost() {
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
        return (super.toString() + "module load mpi/openmpi-1.8.5/gcc-4.4.7 \n"+"cd /home/" + SSHWrapper.username + "/app/meme/bin\n"
                + "mpirun ./meme "  + SSHWrapper.GetRemoteHomeFolder()+SSHWrapper.GetABGFolder()+"datasets/"+super.getInputFile().getValue() + " " + " -o" + (overWrite.getValue() ? "c" : " ") + SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + outputName.getValue()+ " -" + inputType.getValue() 
                + (outputType.getValue() ? " -text " : "") + (ocurrence.getValue().isEmpty() ? "" : " -mod " + ocurrence.getValue()) + " -nmotifs " + motifNumber.getValue() + (exactMotifSites.getValue() ? " -nsites " + maxMotifSites.getValue() : "")
                + " -minsites " + minMotifSites.getValue() + " -maxsites " + maxMotifSites.getValue() + " -wnsites " + bias.getValue() + " -w " + motifLength.getValue()
                + " -minw " + minMotifLength.getValue() + " -maxw " + maxMotifLength.getValue() + (trimming.getValue() ? " -nomatrim " : " ") + " -wg " + gapOpeningCost.getValue() + " -ws " + gapExtensionCost.getValue() + (noEndGaps.getValue() ? " -noendgaps " : ""));
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
