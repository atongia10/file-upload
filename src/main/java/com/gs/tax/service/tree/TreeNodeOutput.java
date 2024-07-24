package com.gs.tax.service.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeOutput {
    private String name;
    private double value;
    private int sequence;
    private List<TreeNodeOutput> children;

    public TreeNodeOutput(String name, double value, int sequence) {
        this.name = name;
        this.value = value;
        this.sequence = sequence;
        this.children = new ArrayList<>();
    }

    public String getName() { return name; }
    public double getValue() { return value; }
    public int getSequence() { return sequence; }
    public List<TreeNodeOutput> getChildren() { return children; }
    public void addChild(TreeNodeOutput child) { children.add(child); }

    public static TreeNodeOutput fromTreeNode(TreeNode node) {
        TreeNodeOutput output = new TreeNodeOutput(node.getName(), node.getValue(), node.getSequence());
        for (TreeNode child : node.getChildren()) {
            output.addChild(fromTreeNode(child));
        }
        return output;
    }
}
