package patrick;

/**
 * EASY
 * https://leetcode.com/problems/design-hashmap/
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class MyHashMap {

    class Entry {
        int key;
        int value;
        Entry next;

        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    int SIZE = 128;
    Entry[] entries;

    /**
     * Initialize your data structure here.
     */
    public MyHashMap() {
        entries = new Entry[SIZE];
    }

    private int hash(int key) {
        return (key * 13) % SIZE;
    }

    /**
     * value will always be non-negative.
     */
    public void put(int key, int value) {
        int hash = hash(key);
        if (entries[hash] == null) {
            entries[hash] = new Entry(key, value);
        } else {

            Entry prev = null;
            Entry curr = entries[hash];

            while (curr != null && curr.key != key) {
                prev = curr;
                curr = curr.next;
            }

            if (curr == null) {
                prev.next = new Entry(key, value);
            } else {
                curr.value = value;
            }

        }

    }

    /**
     * Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key
     */
    public int get(int key) {
        int hash = hash(key);
        if (entries[hash] == null) {
            return -1;
        } else {

            Entry curr = entries[hash];
            while (curr != null && curr.key != key) {
                curr = curr.next;
            }

            if (curr == null) {
                return -1;
            } else {
                return curr.value;
            }
        }


    }

    /**
     * Removes the mapping of the specified value key if this map contains a mapping for the key
     */
    public void remove(int key) {
        int hash = hash(key);

        if (entries[hash] == null) {
            return;
        } else {
            Entry prev = null;
            Entry curr = entries[hash];
            while (curr != null && curr.key != key) {
                prev = curr;
                curr = curr.next;
            }

            if (curr == null) {
                return;
            } else {
                if (prev == null) {
                    entries[hash] = curr.next;
                } else {
                    prev.next = curr.next;
                }

            }
        }

    }
}