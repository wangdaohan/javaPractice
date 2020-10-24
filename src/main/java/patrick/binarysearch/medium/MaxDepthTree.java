package patrick.binarysearch.medium;

/**
 * EASY
 * https://leetcode.com/problems/maximum-depth-of-binary-tree/
 * 二叉树的最大深度
 * 思路：使用递归 recursively
 */
public class MaxDepthTree {
    public static void main(String [] args){
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.right = new Node(6);
        root.right.right = new Node(7);
        root.left.left = new Node(4);
        root.left.left.left = new Node(9);
        root.left.right = new Node(5);
        System.out.println("maxDepth= "+ maxDepthTree(root));
    }
    /*
    solution1:
    * Time complexity: o(N)  -  N: number of nodes
    * Space complexity:  O(log(N)
    * */
    public static int maxDepthTree(Node root){
        if( root == null){
            return 0;
        }else{
            int lDepth = maxDepthTree(root.left);
            int rDepth = maxDepthTree(root.right);
            return (lDepth>rDepth?lDepth:rDepth) +1 ;
        }
    }

}
class Node {
    Node left;
    Node right;
    int data;

    Node(int newData) {
        left = null;
        right = null;
        data = newData;
    }
}
