package patrick.medium;

import java.util.ArrayList;
import java.util.List;
/**
 * MEDIUM
 * https://leetcode.com/problems/binary-tree-inorder-traversal/
 * 二叉树的中序遍历：
 *
 *     前提： 不使用递归
 * 中序
 * 若二叉树非空，则依次执行如下操作：
 * ⑴遍历左子树；
 * ⑵访问根结点；
 * ⑶遍历右子树。
 *
 * 先序遍历
 *
 * 后序遍历
 *
 * Given a binary tree, return the inorder traversal of its nodes' values.
 *
 * Example:
 *
 * Input: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 * Output: [1,3,2]
 */
public class BinaryTreeInorderTraversal {
    public static void main(String[] args){
        TreeNode tree = new TreeNode(1);
        TreeNode rightTree = new TreeNode(2);
        TreeNode rightLTree = new TreeNode(3);
        tree.right = rightTree;
        rightTree.left = rightLTree;
        BinaryTreeInorderTraversal binaryTreeInorderTraversal = new BinaryTreeInorderTraversal();
        System.out.println(binaryTreeInorderTraversal.inorderTraversal(tree));
    }

    public List< Integer > inorderTraversal(TreeNode root) {
        List < Integer > res = new ArrayList< >();
        helper(root, res);
        return res;
    }

    public void helper(TreeNode root, List < Integer > res) {
        if (root != null) {
            if (root.left != null) {
                helper(root.left, res);
            }
            res.add(root.val);
            if (root.right != null) {
                helper(root.right, res);
            }
        }
    }
}

 class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }