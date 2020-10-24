package patrick.LRU;


import java.util.LinkedHashMap;
import java.util.Map;
/**
 Design and implement a data structure for Least Recently Used (LRU) cache.最久未使用算法（LRU, Least Recently Used) It should support the following operations: get and put.

 get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
 put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.

 The cache is initialized with a positive capacity.

 Follow up:
 Could you do both operations in O(1) time complexity?

 Example:

 LRUCache01 cache = new LRUCache01( 2 );   -  2 -> capacity

 cache.put(1, 1);
 cache.put(2, 2);
 cache.get(1);       // returns 1
 cache.put(3, 3);    // evicts key 2, 因为根据LRU原则，2是最久未使用的。
 cache.get(2);       // returns -1 (not found)
 cache.put(4, 4);    // evicts key 1
 cache.get(1);       // returns -1 (not found)
 cache.get(3);       // returns 3
 cache.get(4);       // returns 4

 related topic: Design - 最久未使用算法（LRU, Least Recently Used)
 */

/*
需求：
  1. adding/removal的operations in O(1) time complexity

solution2:

 */
public class LRUMapImpl02 extends LinkedHashMap<Integer,Integer> {
    private final int MAX_CACHE_SIZE;

    public LRUMapImpl02(int capacity) {
        super(capacity, 0.75F, true);
        MAX_CACHE_SIZE = capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > MAX_CACHE_SIZE;
    }


}
