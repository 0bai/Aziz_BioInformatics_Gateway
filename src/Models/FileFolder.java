package Models;


public class FileFolder implements Comparable<FileFolder>{
    public enum Type{
        File,
        Folder,
        Unknow
    }

    public final String mPath;
    public final String mName;
    public final Type mType;

    public FileFolder(String pParent, String strLinuxll) {
        //-rw-r--r--  1 far5 far 22206819 May 29  2015 octave-4.0.0.tar.gz
        String tokens[] = strLinuxll.split("\\s*\\s");
        String Name = strLinuxll.substring(strLinuxll.indexOf(tokens[7])+tokens[7].length()+1);
        this.mPath = pParent+"\\" + Name;
        this.mName= Name;
        if (strLinuxll.charAt(0) == 'd')
            this.mType = Type.Folder;
        else
            this.mType = Type.File;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public int compareTo(FileFolder another) {
        if (another.mType != this.mType)
        {
            if (this.mType == Type.File)
                return 1;
            else
                return -1;
        }
        return this.mType.compareTo(another.mType);
    }
}