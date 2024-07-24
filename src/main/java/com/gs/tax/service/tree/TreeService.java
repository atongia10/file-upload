package com.gs.tax.service.tree;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreeService {

    public double aggregateTree(TreeNode node) {
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

    public List<TreeNodeOutput> getTreeStructure() {
        List<TreeNode> roots = createDummyData();

        // Aggregate values
        for (TreeNode root : roots) {
            aggregateTree(root);
        }

        // Convert to output format
        List<TreeNodeOutput> outputRoots = new ArrayList<>();
        for (TreeNode root : roots) {
            outputRoots.add(TreeNodeOutput.fromTreeNode(root));
        }

        return outputRoots;
    }

    private List<TreeNode> createDummyData() {
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

        return roots;
    }
}

