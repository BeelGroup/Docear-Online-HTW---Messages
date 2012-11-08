package examples;

import java.util.LinkedList;
import java.util.List;

public class MindMap {
    private String text;
    private List<MindMap> leftChildren = new LinkedList<MindMap>();
    private List<MindMap> rightChildren = new LinkedList<MindMap>();

    public MindMap(String text) {
        this.text = text;
    }

    public List<MindMap> getLeftChildren() {
        return leftChildren;
    }

    public void setLeftChildren(List<MindMap> leftChildren) {
        this.leftChildren = leftChildren;
    }

    public List<MindMap> getRightChildren() {
        return rightChildren;
    }

    public void setRightChildren(List<MindMap> rightChildren) {
        this.rightChildren = rightChildren;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
