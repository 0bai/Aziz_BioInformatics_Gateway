package Models;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.util.Vector;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class RemoteTree extends Thread implements SSHListener {

    static Vector<ChannelSftp.LsEntry> list;
    static Thread th;

    public void constructTree(String remotePath, TreeItem parent) throws SftpException, JSchException {
        th = new Thread(new SftpTask(this, remotePath, SftpTask.TaskType.ListFile));
        th.setDaemon(true);
        th.start();
        while (th.isAlive()) {
        }
        parent.setExpanded(true);
        TreeView<ChannelSftp.LsEntry> tree = new TreeView<ChannelSftp.LsEntry>(parent);
        for (ChannelSftp.LsEntry oListItem : list) {
            TreeItem node = new TreeItem<ChannelSftp.LsEntry>(oListItem);
            if (!oListItem.getFilename().startsWith(".") && oListItem.getAttrs().isDir()) {
                constructTree(remotePath + "/" + oListItem.getFilename(), node);
                parent.getChildren().add(node);
            } else if (!".".equals(oListItem.getFilename()) && !"..".equals(oListItem.getFilename())) {
                parent.getChildren().add(node);
            }
        }
    }

    @Override
    public void sshResponse(String strCommand, String strResponse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FileDownloadResponse(String strFilePath, Boolean bStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void FileUploadResponse(String strFilePath, Boolean bStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems) {
        list = lstItems;
    }
}
