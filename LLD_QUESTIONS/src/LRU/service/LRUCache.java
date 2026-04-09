package LRU.service;

import LRU.model.Node;
import LRU.strategy.EvictionPolicy;

import java.util.HashMap;

public class LRUCache<K, V> implements Cache<K, V>{
    private final EvictionPolicy<K> policy;
    private final HashMap<K, Node<K, V>> map;
    private final int capacity;

    public LRUCache(EvictionPolicy<K> policy, int capacity){
        this.policy = policy;
        this.map = new HashMap<>();
        this.capacity = capacity;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }
}
