package Models;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MotifMatchingScript extends Script {

    private SimpleBooleanProperty overWrite;
    private SimpleBooleanProperty outputType;
    private SimpleBooleanProperty alignedCols;
    private SimpleStringProperty db;
    private SimpleIntegerProperty comparisonFunc;
    private SimpleDoubleProperty threshold;
    private SimpleStringProperty significance;
    private SimpleIntegerProperty overlap;

    public MotifMatchingScript() {
        this.outputName = new SimpleStringProperty();
        this.overWrite = new SimpleBooleanProperty();
        this.outputType = new SimpleBooleanProperty();
        this.alignedCols = new SimpleBooleanProperty();
        this.db = new SimpleStringProperty();
        this.comparisonFunc = new SimpleIntegerProperty();
        this.threshold = new SimpleDoubleProperty();
        this.significance = new SimpleStringProperty();
        this.overlap = new SimpleIntegerProperty();
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

    public SimpleBooleanProperty getAlignedCols() {
        return alignedCols;
    }

    public SimpleStringProperty getDb() {
        return db;
    }

    public SimpleIntegerProperty getComparisonFunc() {
        return comparisonFunc;
    }

    public SimpleDoubleProperty getThreshold() {
        return threshold;
    }

    public SimpleStringProperty getSignificance() {
        return significance;
    }

    public SimpleIntegerProperty getOverlap() {
        return overlap;
    }

    @Override
    public String toString() {
        return (super.toString() + "cd /home/" + SSHWrapper.username + "/app/meme/bin\n"
                + "./tomtom " + " -o" + (overWrite.getValue() ? "c" : "") + SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + outputName.getValue()
                + (outputType.getValue() ? " -text" : "") + (alignedCols.getValue() ? " -incomplete-scores" : "") + " -min-overlap " + overlap.getValue() + (significance.getValueSafe().equalsIgnoreCase("E") ? " -evalue" : "") + " -thresh " + threshold.getValue() + " -dist " + (comparisonFunc.getValue() == 0 ? "pearson " : comparisonFunc.getValue() == 1 ? " ed " : " sandelin ")
                + SSHWrapper.GetABGFolder() + "datasets/" + super.getInputFile().getValue() + " " + db.getValueSafe());
    }

    public void submit() {
        super.setScriptVal(new SimpleStringProperty(toString()));
        try {
            super.submit();
        } catch (InterruptedException ex) {
            Logger.getLogger(MotifMatchingScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
