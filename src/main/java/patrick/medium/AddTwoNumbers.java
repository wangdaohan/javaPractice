package patrick.medium;
/**
 * MEDIUM
 * https://leetcode.com/problems/add-two-numbers/solution/
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order and each of their nodes contain a single digit.
 * Add the two numbers and return it as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * 题意：给定2个链表数据，对它们进行相加，对于加总大于10的，进位1，带到下一位
 * 如：  6+4=10 -》 0在第2位 1加到最后一位
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 * Explanation: 342 + 465 = 807.
 */
public class AddTwoNumbers {
    public static void main(String[] args){
        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode l2 = new ListNode(2); ListNode l4 = new ListNode(4); ListNode l3 = new ListNode(3);
        l2.next=l4; l4.next=l3; // (2 -> 4 -> 3)

        ListNode l5 = new ListNode(5); ListNode l6 = new ListNode(6); ListNode l41 = new ListNode(4);
        l5.next=l6; l6.next=l41; //(5 -> 6 -> 4)

        ListNode result = addTwoNumbers.addTwoNumbers01(l2,l5);
        System.out.println(result.val);
        System.out.println(result.next.val);
        System.out.println(result.next.next.val);
    }
    /**
     * 思路：1.先创建链表类 val next carry(当相加>=10时，carry则是放进位值，如sum = 11, carry值=11/10=1,向前进1
     *
     */
    public ListNode addTwoNumbers01(ListNode l1, ListNode l2) {
        ListNode resultHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = resultHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;  //当相加>=10时，需要向前后一位
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return resultHead.next;
    }
}
class ListNode{
    ListNode next;
    int val;
    public ListNode(int val){
        this.val=val;
    }
}
