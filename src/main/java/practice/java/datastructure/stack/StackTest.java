package practice.java.datastructure.stack;

public class StackTest{
    //need to improve/enhance
    public static void main(String [] args){
        Node nodeStack =new Node();
        nodeStack.push(3);
        nodeStack.push(4);
        nodeStack.push(5);
        nodeStack.push(6);
        nodeStack.push(7);

        Node newStack = reverseStatck(nodeStack);
        for(int i=0;i<newStack.size();i++){
            System.out.println(newStack.pop());
        }
    }

    public static Node reverseStatck(Node node){
        Node newStack = new Node();
        for(int i=0;i<node.size();i++){
            newStack.push(node.pop());
        }
        return newStack;
    }

    public static Node reverseStackRecursively(Node node){
        int temp;
        Node newNode = new Node();
        if(node!=null){
            temp = node.pop();
            newNode = insertAtBottom(temp);
            reverseStackRecursively(node);
        }

        return newNode;
    }
    private static Node newNode=new Node();
    public static Node insertAtBottom(int data){
        newNode.push(data);
        return newNode;
    }

}