package Models;

import javafx.concurrent.*;
import java.util.ArrayList;
import java.util.List;


public class SSHTask extends Task {

    public enum TaskType{
        Command,
        UploadFile,
        DownloadFile,
        ListFile,
        Unknow
    }
    private SSHListener mListener;
    private String mCommand;
    private String mSourceFile;
    private String mDestFile;
    private   TaskType mType;
    private String strResult;

    public SSHTask(Object context, String command) {
        mType = TaskType.Command;
        if (context instanceof SSHListener) {
            mListener = (SSHListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement SSHListener");
        }
        mCommand = command;
    }

    public SSHTask(Object context, String strSourceFile, String strDestFile, TaskType pTaskType) {
        mType = pTaskType;
        if (context instanceof SSHListener) {
            mListener = (SSHListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement SSHListener");
        }
        mSourceFile = strSourceFile;
        mDestFile = strDestFile;
    }
    
    
     @Override
    protected Void call() throws Exception {
        try {
            if (mType == TaskType.Command) {
                List<String> lstCommads = new ArrayList<String>();
                lstCommads.add(mCommand);
                 strResult = SSHWrapper.executeRemoteCommand(mCommand);           
            } else if (mType == TaskType.UploadFile) {
                boolean bResult = SSHWrapper.UploadFile(mSourceFile, mDestFile);
                 strResult =  Boolean.toString(bResult);
            } else if (mType == TaskType.DownloadFile){
                boolean bResult = SSHWrapper.DownloadFile(mSourceFile, mDestFile);
                 strResult =  Boolean.toString(bResult);
            }
        } catch (Exception e) {
             strResult =  "";
        }
        return null;
       
    }

    @Override
    protected void done() {
        if (mType == TaskType.Command)
            mListener.sshResponse(mCommand, strResult);
        else if (mType == TaskType.DownloadFile)
            mListener.FileDownloadResponse(mDestFile, Boolean.parseBoolean(strResult));
        else if (mType == TaskType.UploadFile)
            mListener.FileUploadResponse(mDestFile, Boolean.parseBoolean(strResult));
    }
}

