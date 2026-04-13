package LRU.service;

import LRU.model.Node;
import LRU.strategy.EvictionPolicy;

import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;

public class LRUCache<K, V> implements Cache<K, V>{
    private final EvictionPolicy<K, V> policy;
    private final HashMap<K, Node<K, V>> map;
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();

    public LRUCache(EvictionPolicy<K, V> policy, int capacity){
        this.policy = policy;
        this.map = new HashMap<>();
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            if (!map.containsKey(key)) {
                return null;
            }
            Node<K, V> node = map.get(key);
            policy.recordAccess(node);
            return node.getValue();
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            if (map.containsKey(key)) {
                Node<K, V> oldNode = map.get(key);
                policy.removeNode(oldNode);
                Node<K, V> newNode = new Node<>(key, value);
                map.put(key, newNode);
                policy.addNew(newNode);
                return;
            }
            if (map.size() == capacity) {
                K evictedKey = policy.evict();
                map.remove(evictedKey);
            }

            Node<K, V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            policy.addNew(newNode);
        }finally {
            lock.unlock();
        }
    }
}
