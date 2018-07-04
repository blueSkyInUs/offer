import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceCodeTest {

    @SneakyThrows
    public static void main(String[] args) {

//        ArrayList<String> arrayList=new ArrayList();
//        arrayList.add("one");
//        arrayList.add("two");
//        arrayList.add("three");
//
//        print("lesson",arrayList.toArray(new String[arrayList.size()] ));
//
//
//        TreeMap<Integer,String> treeMap=new TreeMap<>();
//
//
//
//        treeMap.put(10,null);
//        treeMap.put(20,"20");
//        treeMap.put(30,"30");
//
//        System.out.println(treeMap.ceilingEntry(25).getValue());
//
//        String payout=Double.parseDouble("0.1")*Double.parseDouble("0.00")/100+"";
//        System.out.println(payout);
//
//        HashMap<String,String> result=new HashMap<>();
//        result.put(null,null);
//
//        ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>();
//        concurrentHashMap.put(null,null);
//
//        LinkedBlockingQueue<String> stringLinkedBlockingQueue=new LinkedBlockingQueue<>();
//        stringLinkedBlockingQueue.put("one");
//        stringLinkedBlockingQueue.poll();
//
//        SynchronousQueue<String> stringSynchronousQueue=new SynchronousQueue<>();
//        stringSynchronousQueue.offer("one");
//        stringSynchronousQueue.poll();
//
//        ExecutorService executorService= Executors.newFixedThreadPool(100);

        ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>();
        concurrentHashMap.put("one","two");
        concurrentHashMap.get("one");

        AtomicInteger atomicInteger=new AtomicInteger();
        atomicInteger.incrementAndGet();







    }


    public static void print(String name,Object ...attributes){
        System.out.println(name);

        for (Object attribute:attributes){
            System.out.println(attribute);
        }
    }
}
