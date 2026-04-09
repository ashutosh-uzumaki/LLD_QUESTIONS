package LRU.strategy;

public interface EvictionPolicy<K> {
    void recordAccess(K key);
    K evict();
}
