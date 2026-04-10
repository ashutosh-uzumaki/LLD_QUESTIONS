package LRU.strategy;

import LRU.model.DoublyLinkedList;
import LRU.model.Node;

import java.util.HashMap;

public class LruEvictionPolicy<K, V> implements EvictionPolicy<K, V>{
    private final DoublyLinkedList<K, V> list;
    public LruEvictionPolicy(){
        list = new DoublyLinkedList<>();
    }

    @Override
    public void recordAccess(Node<K, V> node) {
        list.moveToHead(node);
    }

    @Override
    public K evict() {
        Node<K, V> node = list.removeTail();
        K key = node.getKey();
        return key;
    }
}
