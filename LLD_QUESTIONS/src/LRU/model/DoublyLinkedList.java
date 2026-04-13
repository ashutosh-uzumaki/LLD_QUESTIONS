package LRU.model;

public class DoublyLinkedList<K, V> {
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public DoublyLinkedList(){
        head = new Node<>(null, null);
        tail = new Node<>(null, null);

        head.next = tail;
        tail.prev = head;
    }

    public void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public Node<K, V> removeTail(){
        Node<K, V> removed = tail.prev;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        return removed;
    }

    public void moveToHead(Node<K, V> node){
        node.prev.next = node.next;
        node.next.prev = node.prev;

        addToHead(node);
    }


    public void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }


}
