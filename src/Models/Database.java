package Models;

public class Database {

    private String name;
    private String path;

    public Database(String name, String path) {
        this.name = name;
        String paths[] = path.split(" ");
        this.path ="";
        for (int i = 0; i < paths.length; i++) {
            this.path += SSHWrapper.GetRemoteHomeFolder() + "/app/meme/db/motif_databases/" + paths[i]+" ";
        }

    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }

}
