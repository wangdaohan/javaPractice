package patrick.linklist.medium;

/**
 *MEDIUM
https://leetcode.com/problems/remove-nth-node-from-end-of-list/
Given a linked list, remove the n-th node from the end of list and return its head.

Example:

Given linked list: 1->2->3->4->5, and n = 2.

After removing the second node from the end, the linked list becomes 1->2->3->5.
Note:
Given n will always be valid.

Follow up:
Could you do this in one pass?
 */
public class RemoveNthNodeFromEnd {
    public static void main(String[] args) {
        ListNode head1 = new ListNode(1);
        ListNode head2 = new ListNode(2);
        ListNode head3 = new ListNode(3);
        ListNode head4 = new ListNode(4);
        ListNode head5 = new ListNode(5);
        head1.next = head2;
        head2.next = head3;
        head3.next = head4;
        head4.next = head5;
        RemoveNthNodeFromEnd removeNthNodeFromEnd = new RemoveNthNodeFromEnd();
        removeNthNodeFromEnd.removeNthNode2(head1, 2);
    }
    /*
    s2:
    time complexity: O(n) one pass
    思路: 2个指针 1个指针用正常向前  另1个指针跟第1个指针差n-1步  当n=2时， 1->2->3->4->5  当1个指针在3，另1个在2， pre指针在1
     */
    public ListNode removeNthNode2(ListNode head, int n){
        //排队只有一个的ListNode ,返回 Null
        if(head.next==null){
            return null;
        }
        ListNode start1 = head;
        ListNode start2 = head;
        ListNode pre = null;//记录后面算起第N个元素的前面一个元素
        int length =0;
        while(start1.next != null){
            /**
             * start2与start1的间隔是n-1,如何保证呢，
             * 就是length>=n-1时，start2才往前移，而此时start1已经移了n-1步了
             * 之后start2和start1都是同时向前移动，保持间隔是n-1
             */
            if(length >= n-1) {
                pre = start2;
                start2 = start2.next;
            }
            start1 = start1.next;
            length++;
        }
        if (start2 == head) {
            return head.next;
        }
        pre.next = start2.next;
        return head;
    }

    /*
    s1:
    思路： 1. 先一个循环计算Length
         2. 再一个循环，当到达target位置时，target.next = target.next.next
     */
    public ListNode removeNthNode(ListNode head, int n){
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;

        int length =0;
        ListNode first = head;
        while(first != null){
            length++;
            first = first.next;
        }

        int targetLoc = length - n;
        ListNode target = head;
        for(int i=1;i<targetLoc;i++){
            target = target.next;
        }

        target.next = target.next.next;

        return dummyNode.next;
    }


}
 class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
}
