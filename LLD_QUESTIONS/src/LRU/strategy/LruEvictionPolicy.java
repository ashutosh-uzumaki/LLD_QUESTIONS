package LRU.strategy;

import LRU.model.DoublyLinkedList;
import LRU.model.Node;

import java.util.HashMap;

public class LruEvictionPolicy<K, V> implements EvictionPolicy<K>{
    private final DoublyLinkedList<K, V> list;
    private final HashMap<K, Node<K, V>> map;

    public LruEvictionPolicy(){
        list = new DoublyLinkedList<>();
        map = new HashMap<>();
    }

    @Override
    public void recordAccess(K key) {
        if(!map.containsKey(key)){
            return;
        }
        Node<K, V> node = map.get(key);
        list.moveToHead(node);
    }

    @Override
    public K evict() {
        Node<K, V> node = list.removeTail();
        K key = node.getKey();
        map.remove(key);
        return key;
    }
}
