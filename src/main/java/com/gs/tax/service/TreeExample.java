package com.gs.tax.service;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
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

    public void printTree(String indent) {
        // Print children first
        children.sort((a, b) -> Integer.compare(a.getSequence(), b.getSequence()));
        for (TreeNode child : children) {
            child.printTree(indent + "  ");
        }
        // Print the current node's information last
        System.out.println(indent + "Level: " + name + ", Value: " + value + ", Sequence: " + sequence);
    }
}

public class TreeExample {
    public static double aggregateTree(TreeNode node) {
        if (node.getChildren().isEmpty()) {
            return node.getValue();
        }

        double totalValue = node.getValue();
        node.getChildren().sort((a, b) -> Integer.compare(a.getSequence(), b.getSequence()));

        for (TreeNode child : node.getChildren()) {
            totalValue += aggregateTree(child);
        }

        node.setValue(totalValue); // Update the current node's value
        return totalValue;
    }

    public static void main(String[] args) {
        // Simulate data retrieval from a database
        List<TreeNode> roots = new ArrayList<>();

        TreeNode root1 = new TreeNode("Root1", 0, 1);
        TreeNode child1 = new TreeNode("Child1", 0, 1);
        TreeNode child2 = new TreeNode("Child2", 0, 2);
        TreeNode grandchild1 = new TreeNode("Grandchild1", 10, 1);
        TreeNode grandchild2 = new TreeNode("Grandchild2", 20, 2);

        child1.addChild(grandchild1);
        child2.addChild(grandchild2);
        root1.addChild(child1);
        root1.addChild(child2);

        TreeNode root2 = new TreeNode("Root2", 0, 1);
        TreeNode child3 = new TreeNode("Child3", 0, 1);
        TreeNode grandchild3 = new TreeNode("Grandchild3", 30, 1);

        child3.addChild(grandchild3);
        root2.addChild(child3);

        roots.add(root1);
        roots.add(root2);

        // Aggregate and print for each root
        for (TreeNode root : roots) {
            aggregateTree(root);
            System.out.println("==== Tree Structure for " + root.getName() + " ====");
            root.printTree("");
            System.out.println();
        }
    }
}