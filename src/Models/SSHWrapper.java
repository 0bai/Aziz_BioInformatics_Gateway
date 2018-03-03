package Models;
import com.jcraft.jsch.*;
import java.io.*;
import static java.lang.Thread.sleep;
import java.util.Vector;

public class SSHWrapper {
    public static String username;
    public static String password;
  //  public static String host = "localhost";
    public static String host = "10.113.16.7";
    public static int port;

    public static String GetLocalHomeFolder(){
        
        return "/Volumes/MacintoshHDD/OBAI";
    }
    
    public static String GetRemoteHomeFolder(){
        return "/home/"+username;
    }
    
    public static String GetABGFolder(){
        return "/ABG/";
    }
    
    public static void SetCredentials(String Username, String Password, String Hostname, int Port) {
        username = Username;
        password = Password;
        host = Hostname;
        port = Port;

    }

    public static String executeRemoteCommand(String cmd) throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        session.setConfig("StrictHostKeyChecking", "no");

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");

        //PrintStream out = new PrintStream(channelssh.getOutputStream());
        InputStream in = channelssh.getInputStream();
        String result = "";
        byte[] temp = new byte[10000];
        int iTotalSleep = 10000;

        channelssh.setCommand(cmd);
        channelssh.connect();

        while (in.available() == 0 && iTotalSleep > 0) {
            sleep(500);
            iTotalSleep -= 300;
        }
        while (in.available() > 0) {
            int x = in.read(temp);
            result += new String(temp, 0, x);
           sleep(500);
        }


        channelssh.disconnect();
        session.disconnect();
        return result;
    }


    public static boolean CopyFileToRemoteEx(String strLocalFile, String strRemoteDest) {

        FileInputStream fis = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.connect();

            boolean ptimestamp = true;

            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + strLocalFile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            File _lfile = new File(strRemoteDest);

            if (ptimestamp) {
                command = "T " + (_lfile.lastModified() / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
                out.write(command.getBytes());
                out.flush();
                if (checkAck(in) != 0) {
                    return false;
                }
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if (strLocalFile.lastIndexOf('/') > 0) {
                command += strLocalFile.substring(strLocalFile.lastIndexOf('/') + 1);
            } else {
                command += strLocalFile;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(strLocalFile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            out.close();

            channel.disconnect();
            session.disconnect();

            return true;
        } catch (Exception e) {
            System.out.println(e);
            try {
                if (fis != null) fis.close();
            } catch (Exception ee) {
                return false;
            }
        }
        return true;
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    public static boolean UploadFile(String mSourceFile, String mDestFile) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.put(mSourceFile, mDestFile);
            channel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean DownloadFile(String mSourceFile, String mDestFile) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.get(mSourceFile, mDestFile);
            channel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Vector<ChannelSftp.LsEntry> ListFile(String pFolderPath) {
        Vector<ChannelSftp.LsEntry> result = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            // Avoid asking for key confirmation
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            result = channel.ls(pFolderPath);
            channel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return result;
    }
}




