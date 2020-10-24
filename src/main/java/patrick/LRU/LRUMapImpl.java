package patrick.LRU;

import java.util.HashMap;
import java.util.Map;

/*

需求： 实现LRU(Last Recently Used)缓存，遵循以下原则
  1. adding/removal的operations in O(1) time complexity
  2. Least Recently Used 最久未使用算法

solution1:
  1. 模拟linkedHashMap实现
  2. 首先定义Node 包含 key,value, previous ,next
  3. 使用 HashMap 作为基础存储

思路：


 */
class Node {
    int key;
    int val;
    Node prev;
    Node next;

    public Node(){ }

    public Node(int key, int val){
        this.key = key;
        this.val = val;
    }
}

public class LRUMapImpl<K, V> {
    final int cacheCapacity;
    final Node head = new Node();
    final Node tail = new Node();
    Map<Integer,Node> nodeMap;

    public LRUMapImpl(int capacity) { /* constructor */
        /* this will be called first so intialise the map */
        nodeMap = new HashMap<>();
        this.cacheCapacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if(nodeMap.containsKey(key)){
            Node temp = nodeMap.get(key);
            removeFromCache(temp);
            addToCache(temp);
            return temp.val;
        }
        return -1;
    }

    public void put(int key, int value) {
        /* if key is already present make update it's value and make it most recent */
        if(nodeMap.containsKey(key)){
            Node temp = nodeMap.get(key);
            temp.val = value;
            removeFromCache(temp);
            addToCache(temp);
        }else {
            if(nodeMap.size()==cacheCapacity) {
                nodeMap.remove(tail.prev.key);
                removeFromCache(tail.prev);
            }
            Node addNode = new Node(key,value);
            nodeMap.put(key,addNode);
            addToCache(addNode);
        }
    }
    /* customs methods we are using Doubly linked list as it add and removes
    in constant time. Also head and tail are just dummy nodes to keep track */
    public void addToCache(Node mostRecent){
        /* we add new node that is most recent in front */
        Node recent = head.next;
        recent.prev = mostRecent;
        mostRecent.prev = head;
        mostRecent.next = recent;
        head.next = mostRecent;
    }

    public void removeFromCache(Node node){
        Node nodeBefore = node.prev;
        Node nodeAfter = node.next;
        nodeBefore.next = nodeAfter;
        nodeAfter.prev = nodeBefore;

    }
}