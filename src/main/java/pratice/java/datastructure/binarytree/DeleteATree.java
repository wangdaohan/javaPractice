//package pratice.java.datastructure.binarytree;
//
///**
// * Created by patrick on 2015/10/29.
// * Copyright @ EEGSmart
// */
//public class DeleteATree {
//    public static void main(String [] args){
//
//        Node root = newNode(1);
//        root.left = newNode(2);
//        root.right = newNode(3);
//        root.left.left = newNode(4);
//        root.left.left.left = newNode(9);
//        root.left.right = newNode(5);
//        deleteTree(root);
//        System.out.println("after deletion tree= "+ root.toString());
//
//
//    }
//
//    public static void deleteTree(Node root){
//        if(root == null) return;
//
//        deleteTree(root.left);
//        deleteTree(root.right);
//
//        root.data = 0;
//        root.left = null;
//        root.right = null;
//
//    }
//
//    public static int maxDepthTree(Node root){
//        if( root == null){
//            return 0;
//        }else{
//            int lDepth = maxDepthTree(root.left);
//            int rDepth = maxDepthTree(root.right);
//            return (lDepth>rDepth?lDepth:rDepth) +1 ;
//        }
//
//    }
//
//
//    public static Node newNode(int data) {
//        Node temp = new Node(data);
//        temp.left = null;
//        temp.right = null;
//        return temp;
//    }
//}
