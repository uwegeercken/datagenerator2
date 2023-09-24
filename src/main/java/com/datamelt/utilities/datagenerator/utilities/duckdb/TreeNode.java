package com.datamelt.utilities.datagenerator.utilities.duckdb;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T>
{
    T name;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public TreeNode(T name) {
        this.name = name;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public static void main(String[] args)
    {
        TreeNode<String> root = new TreeNode<>("person");
        TreeNode<String> child1 = root.addChild("address");
        TreeNode<String> child2 = child1.addChild("street");

        TreeNode<Struct> rs = new TreeNode<>(new Struct("person"));
        TreeNode<Struct> ch1 = rs.addChild(new Struct("address"));
        TreeNode<Struct> ch2 = ch1.addChild(new Struct("street"));

        System.out.println();
    }

}
