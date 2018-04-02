
package Models;

public class Database {
    private String name;
    private String path;

    public Database(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}
