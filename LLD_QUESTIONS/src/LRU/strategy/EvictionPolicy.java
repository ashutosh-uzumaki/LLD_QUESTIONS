package LRU.strategy;

import LRU.model.Node;

public interface EvictionPolicy<K, V> {
    void recordAccess(Node<K, V> node);
    K evict();
    void addNew(Node<K, V> newNode);
    void removeNode(Node<K, V> node);
}
