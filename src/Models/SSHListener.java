package Models;

import com.jcraft.jsch.ChannelSftp;
import java.util.Vector;

public interface SSHListener {
    public void sshResponse(String strCommand, String strResponse);
    public void FileDownloadResponse(String strFilePath, Boolean bStatus);
    public void FileUploadResponse(String strFilePath, Boolean bStatus);
    public void GotFilesList(String strDirecory, Vector<ChannelSftp.LsEntry> lstItems);
}