package HitCounter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HitCounter {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> hits = new ConcurrentHashMap<>();
    private final int windowMinutes;

    public HitCounter(int windowMinutes){
        this.windowMinutes = windowMinutes;
    }

    public void recordHits(String key){
        hits.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>()).add(System.currentTimeMillis());
    }

    public long getHitCount(String key){
        ConcurrentLinkedQueue<Long> queue = hits.get(key);
        if(queue == null){
            return 0;
        }
        long cutOff = System.currentTimeMillis() - (windowMinutes * 60 * 1000);
        while(!queue.isEmpty() && queue.peek() < cutOff){
            queue.poll();
        }
        return queue.size();
    }

    public long getTotalCount(){
        long total = 0;
        for(String key: hits.keySet()){
            total += getHitCount(key);
        }
        return total;
    }
}
