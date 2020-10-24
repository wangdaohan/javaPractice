package patrick.linklist.medium;

/**
 * EASY
 * 题1；https://leetcode.com/problems/linked-list-cycle/solution/
 * 题2：https://leetcode.com/problems/linked-list-cycle-ii/
 */

/**
 * 题意：题1：检查一个Linkedlist是否有环， 并给点环的起始结点（入口）  - 正常Linked List不应该在里面有环
 *      题2：找到linkedList环的入口
 *
 * 思路：2个指针，快指针每次走2步，慢指针每次走1步。
 *      原理： 如果有环，则快指针迟早会等于慢指针 -> 快指针快于慢指针，如果有环，则会一直循环下去。直到fast = slow
 *
 * 实现步骤：1.fast走2步，slow走1步，能得知有没有环.
 *            如下面例子：当找到时，fast和slow在4的位置（不是入口）   ->题1需求
 *         2. 需要找到入口3， fast=head,slow不变，-》 fast = fast.next and slow=slow.next.      ->题2需求
 *            当fast = head时，返回fast 就是入口
 *
 * 例子： 1 -> 2 -> 3 -> 4 -> 5 -> 3  环从3开始 入口 3->4->5
 *
 */

public class ALinkedListCycle {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node3;
        //node6.next = node1;
        System.out.println(detectCycle(node1).val);
    }
    public static ListNode detectCycle(ListNode head) {
        if(head == null)
            return null;
        ListNode fast = head;
        ListNode slow = head;

        boolean isCycle = false;
        //这里只能得出有没有环，无法得知环的入口  - 起始点
        while(fast.next != null && fast.next.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if(slow.equals(fast)){
                isCycle = true;
                break;
            }
        }
        //上面slow保存了环的一个结点，有可能是入口，也有可能不是。因此下面的逻辑来找到入口
        //当fast = head时，返回fast 就是入口
        if(isCycle){
            fast = head;
            while(!fast.equals(slow)){
                fast = fast.next;
                slow = slow.next;
            }
            return fast;
        }else{
            return null;
        }
    }
}


 class ListNode02 {
      int val;
      ListNode next;
     ListNode02(int x) {
          val = x;
          next = null;
      }
}