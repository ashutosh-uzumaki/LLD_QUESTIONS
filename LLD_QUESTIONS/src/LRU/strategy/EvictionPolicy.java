package LRU.strategy;

import LRU.model.Node;

public interface EvictionPolicy<K, V> {
    void recordAccess(Node<K, V> node);
    K evict();
}
