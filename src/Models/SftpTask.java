package Models;

import com.jcraft.jsch.ChannelSftp;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

public class SftpTask extends Task {

    public enum TaskType {
        ListFile,
        Unknow
    }
    private SSHListener mListener;
    private String mSourceFile;
    private String mDestFile;
    private TaskType mType;

    public SftpTask(Object context, String strSourceFile, TaskType pTaskType) {
        mType = pTaskType;
        if (context instanceof SSHListener) {
            mListener = (SSHListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SSHListener");
        }
        mSourceFile = strSourceFile;
    }

    @Override
    protected Vector<ChannelSftp.LsEntry> call() throws Exception {
        try {
            if (mType == TaskType.ListFile) {
                return SSHWrapper.ListFile(mSourceFile);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void done() {
        if (mType == TaskType.ListFile) {
            try {
                mListener.GotFilesList(mSourceFile, (Vector<ChannelSftp.LsEntry>) this.get());
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(SftpTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
