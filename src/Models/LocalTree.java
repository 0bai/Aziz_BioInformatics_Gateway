package Models;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import java.io.File;

public class LocalTree {

    public static void constructTree(String Path, TreeItem parent) {
        parent.setExpanded(true);
        File file = new File(Path);
        TreeView<File> tree = new TreeView<File>(parent);
        File[] files = file.listFiles();
        for (File child : files) {
            TreeItem node = new TreeItem<File>(child);
            if (!child.isHidden() && child.isDirectory()) {
                node.getChildren().add(null);
                parent.getChildren().add(node);
            } else if (!child.isHidden() && !child.getName().startsWith(".")) {
                parent.getChildren().add(node);
            }
        }
    }
}
