package com.gs.tax.service.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String name;
    private double value;
    private int sequence;
    private List<TreeNode> children;

    public TreeNode(String name, double value, int sequence) {
        this.name = name;
        this.value = value;
        this.sequence = sequence;
        this.children = new ArrayList<>();
    }

    public String getName() { return name; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public int getSequence() { return sequence; }
    public List<TreeNode> getChildren() { return children; }
    public void addChild(TreeNode child) { children.add(child); }
}
