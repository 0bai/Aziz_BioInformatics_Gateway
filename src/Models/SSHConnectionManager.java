package Models;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;


public class SSHConnectionManager {

    private static Session mSession;
    private static ChannelShell mChannel;
    private static String mUsername = "";
    private static String mPassword = "";
    private static String mHostname = "";

    public static void SetCredentials(String strUserName, String strPassword, String strHostname){
        mUsername = strUserName;
        mPassword = strPassword;
        mHostname = strHostname;
    }

    private static Session getSession(){
        if(mSession == null || !mSession.isConnected()){
            mSession= connect();
        }
        return mSession;
    }

    private static Channel openChannel(){
        if(mChannel == null || !mChannel.isConnected()){
            try{
                mChannel= (ChannelShell)getSession().openChannel("shell");
                mChannel.connect();

            }catch(Exception e){
                System.out.println("Error while opening channel: "+ e);
            }
        }
        return mChannel;
    }

    private static Session connect(){
        JSch jSch = new JSch();
        try {

            mSession = jSch.getSession(mUsername, mHostname, 22);
            mSession.setConfig("StrictHostKeyChecking", "no");
            mSession.setPassword(mPassword);

            mSession.connect();

        }catch(Exception e){
            System.out.println("An error occurred while connecting to "+mHostname+": "+e);
        }
        return mSession;
    }

    public static String executeCommands(List<String> lstCommands){
        String strResult = "";
        try{
            openChannel();
            sendCommands(lstCommands);
            strResult = readChannelOutput();
        }catch(Exception e){
            System.out.println("An error ocurred during executeCommands: "+e);
        }
        return strResult;
    }

    private static void sendCommands(List<String> lstCommands){

        try{
            PrintStream out = new PrintStream(mChannel.getOutputStream());

            out.println("#!/bin/bash");
            for(String strCommand : lstCommands) {
                out.println(strCommand);
            }
            out.println("exit");
            out.flush();
        }catch(Exception e){
            System.out.println("Error while sending commands: "+ e);
        }
    }

    private static String readChannelOutput(){
        StringBuilder strResult = new StringBuilder();

        byte[] buffer = new byte[1024];

        try{
            InputStream in = mChannel.getInputStream();
            String line = "";
            while (true){
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    strResult.append(line);
                    strResult.append(System.getProperty("line.separator"));
                    //System.out.println(line);
                }

                if(line.contains("logout")){
                    break;
                }

                if (mChannel.isClosed()){
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee){}
            }
        }catch(Exception e){
            System.out.println("Error while reading channel output: "+ e);
        }
        return strResult.toString();
    }

    public static void close(){
        mChannel.disconnect();
        mSession.disconnect();
    }
}