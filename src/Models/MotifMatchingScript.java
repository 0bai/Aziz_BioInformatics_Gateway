package Models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MotifMatchingScript extends Script {

    private SimpleStringProperty outputName;
    private SimpleBooleanProperty overWrite;
    private SimpleBooleanProperty outputType;
    private SimpleBooleanProperty alignedCols;
    private SimpleStringProperty db;
    private SimpleIntegerProperty comparisonFunc;
    private SimpleDoubleProperty threshold;
    private SimpleStringProperty significance;
    private SimpleIntegerProperty overlap;

    public MotifMatchingScript() {
        outputName = new SimpleStringProperty();
        overWrite = new SimpleBooleanProperty();
        outputType = new SimpleBooleanProperty();

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
                + "./tomtom " + outputName.getValue() + " -o" + (overWrite.getValue() ? "c" : "") + SSHWrapper.GetRemoteHomeFolder() + SSHWrapper.GetABGFolder() + "jobs/" + outputName.getName()
                + (outputType.getValue() ? " -text" : "") + (alignedCols.getValue() ? " -incomplete-scores" : "") + " -min-overlap " + overlap + (significance.getValueSafe().equalsIgnoreCase("E") ? " -evalue" : "") + " -thresh " + threshold.getValue() + " -dist " + (comparisonFunc.getValue() == 0 ? "pearson " : comparisonFunc.getValue() == 1 ? " ed " : " sandelin ")
                + super.getInputFile() + " " + db.getValueSafe());
    }

    public void submit() {
        super.setScriptVal(new SimpleStringProperty(toString()));
        super.submit();
    }

}
