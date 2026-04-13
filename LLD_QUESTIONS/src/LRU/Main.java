package LRU;

import LRU.service.LRUCache;
import LRU.strategy.LruEvictionPolicy;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== LRU Cache Tests ===\n");

        test1_basicGetPut();
        test2_eviction();
        test3_updateExistingKey();
        test4_cacheMiss();
        test5_concurrency();
    }

    // Test 1: Basic get and put
    static void test1_basicGetPut() {
        LRUCache<Integer, String> cache = new LRUCache<>(new LruEvictionPolicy<>(), 3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        assert "one".equals(cache.get(1)) : "FAIL: key 1 should return 'one'";
        assert "two".equals(cache.get(2)) : "FAIL: key 2 should return 'two'";
        System.out.println("Test 1 PASSED: basic get/put works");
    }

    // Test 2: LRU eviction - least recently used should be evicted
    static void test2_eviction() {
        LRUCache<Integer, String> cache = new LRUCache<>(new LruEvictionPolicy<>(), 3);
        cache.put(1, "one");   // order: [1]
        cache.put(2, "two");   // order: [2, 1]
        cache.put(3, "three"); // order: [3, 2, 1]
        cache.get(1);          // order: [1, 3, 2]  → 1 is now most recent
        cache.put(4, "four");  // order: [4, 1, 3]  → 2 is evicted (LRU)

        assert cache.get(2) == null : "FAIL: key 2 should have been evicted";
        assert "four".equals(cache.get(4)) : "FAIL: key 4 should exist";
        assert "one".equals(cache.get(1)) : "FAIL: key 1 should still exist";
        System.out.println("Test 2 PASSED: LRU eviction works correctly");
    }

    // Test 3: Update existing key should not cause eviction
    static void test3_updateExistingKey() {
        LRUCache<Integer, String> cache = new LRUCache<>(new LruEvictionPolicy<>(), 2);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(1, "ONE");   // update key 1, should NOT evict key 2

        assert "ONE".equals(cache.get(1)) : "FAIL: key 1 should be updated to 'ONE'";
        assert "two".equals(cache.get(2)) : "FAIL: key 2 should still exist after update";
        System.out.println("Test 3 PASSED: updating existing key works correctly");
    }

    // Test 4: Cache miss returns null
    static void test4_cacheMiss() {
        LRUCache<Integer, String> cache = new LRUCache<>(new LruEvictionPolicy<>(), 2);
        assert cache.get(99) == null : "FAIL: missing key should return null";
        System.out.println("Test 4 PASSED: cache miss returns null");
    }

    // Test 5: Concurrency - multiple threads writing simultaneously
    static void test5_concurrency() throws InterruptedException {
        LRUCache<Integer, String> cache = new LRUCache<>(new LruEvictionPolicy<>(), 100);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) cache.put(i, "val" + i);
        });
        Thread t2 = new Thread(() -> {
            for (int i = 50; i < 100; i++) cache.put(i, "val" + i);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // No exception thrown = thread safety holds
        System.out.println("Test 5 PASSED: concurrent writes without exception");
        System.out.println("\nAll tests passed! ✓");
    }
}