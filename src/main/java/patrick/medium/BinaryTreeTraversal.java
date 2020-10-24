package patrick.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * MEDIUM
 * https://leetcode.com/problems/binary-tree-inorder-traversal/
 * 二叉树的中序遍历：
 *
 *     前提： 不使用递归
 *
 * 先序（1）访问根结点。
 * （2）前序遍历左子树。
 * （3）前序遍历右子树 。
 *
 * 中序
 * 若二叉树非空，则依次执行如下操作：
 * ⑴遍历左子树；
 * ⑵访问根结点；
 * ⑶遍历右子树。
 *
 * 后序：（1）后序遍历左子树
 * （2）后序遍历右子树
 * （3）访问根结点
 *      A
 *    /   \
 *   B     C
 * /  \   /
 * D  E  F
 * inorder 中序遍历：Output: [DBE A FC]
 *
 * preorder 先序遍历 [ ABDE CF]
 *
 * postorder 后序遍历 [DEB FCA]
 */
public class BinaryTreeTraversal {
    public static void main(String[] args){
        TreeNode2 tree = new TreeNode2(1);
        TreeNode2 rightTree = new TreeNode2(2);
        TreeNode2 rightLTree = new TreeNode2(3);
        tree.right = rightTree;
        rightTree.left = rightLTree;
        BinaryTreeTraversal binaryTreeTraversal = new BinaryTreeTraversal();
        System.out.println(binaryTreeTraversal.inorderTraversal(tree));
        System.out.println(binaryTreeTraversal.preOrderTraversal(tree));
        //System.out.println(binaryTreeTraversal.postOrderTraversal(tree));
    }
    /**
     * 最优解：
     * Morris Traversal 莫里斯解法（线索二叉树）
     * 1、如果当前结点的左孩子为空，则输出当前结点并将当前结点的右结点作为当前结点。
     * 2、如果当前结点的左孩子不为空，则从当前结点的左子树找出当前结点的前驱节点：
     * 如果前驱结点p的右孩子为空，则将p的右孩子设为当前结点；否则，输出当前结点，并将p的右孩子置为空，并将当前当前结点的右孩子置为当前结点
     * 3、重复1 ，2两步直到当前结点为空
        参考：http://www.cnblogs.com/AnnieKim/archive/2013/06/15/morristraversal.html
     * 时间复杂度：O(n)。
     * 空间复杂度：O(1)，因为只用了两个辅助指针。
     */
    public List<Integer> inorderTraversal(TreeNode2 root) {
        if(root == null) return new ArrayList<Integer>();
        List<Integer> res = new ArrayList<Integer>();
        TreeNode2 pre = null;
        while(root != null){
            if(root.left == null){//如果当前结点的左孩子为空
                res.add(root.val);//则输出当前结点
                root = root.right;//并将当前结点的右结点作为当前结点
            }else{//如果当前结点的左孩子不为空,则从当前结点的左子树找出当前结点的前驱节点：
                pre = root.left;
                while(pre.right != null && pre.right != root){
                    pre = pre.right;//最后pre指向当前结点的前驱结点
                }
                if(pre.right == null){//如果前驱结点p的右孩子为空，则将p的右孩子设为当前结点
                    pre.right = root;
                    root = root.left;
                }else{//否则，输出当前结点，并将p的右孩子置为空，并将当前当前结点的右孩子置为当前结点
                    pre.right = null;
                    res.add(root.val);
                    root = root.right;
                }
            }
        }
        return res;
    }
    public List< Integer > preOrderTraversal(TreeNode2 root) {
        TreeNode2 cur = root;
        TreeNode2 prev = null;
        List<Integer> res = new ArrayList<Integer>();
        while (cur != null)
        {
            if (cur.left == null)
            {
                res.add(cur.val);
                cur = cur.right;
            }
            else
            {
                prev = cur.left;
                while (prev.right != null && prev.right != cur){
                    prev = prev.right;
                }
                if (prev.right == null){
                    res.add(cur.val);  // the only difference with inorder-traversal
                    prev.right = cur;
                    cur = cur.left;
                }
                else{
                    prev.right = null;
                    cur = cur.right;
                }
            }
        }
        return res;
    }
    /*public List< Integer > postOrderTraversal(TreeNode2 root) {
        TreeNode2 dump =  new TreeNode2(0);
        dump.left = root;
        TreeNode2 cur = dump;
        TreeNode2 prev = null;
        List<Integer> res = new ArrayList<Integer>();
        while (cur != null)
        {
            if (cur.left == null)
            {
                cur = cur.right;
            }
            else
            {
                prev = cur.left;
                while (prev.right != null && prev.right != cur)
                    prev = prev.right;

                if (prev.right == null)
                {
                    prev.right = cur;
                    cur = cur.left;
                }
                else
                {
                    res.add(cur.left);
                    //printReverse(cur.left, prev);  // call print
                    prev.right = null;
                    cur = cur.right;
                }
            }
        }
    }*/
}

class TreeNode2 {
     int val;
    TreeNode2 left;
    TreeNode2 right;
    TreeNode2(int x) { val = x; }
 }