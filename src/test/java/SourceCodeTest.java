import lombok.SneakyThrows;

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


//        String usrHome = System.getProperty("user.home");
//        if(!usrHome.endsWith("/")){
//            usrHome=usrHome+"/";
//        }
//        System.out.println(usrHome);
//        String ipMapFilePath=usrHome+"ipMapFilePath.important";
//        File file=new File(ipMapFilePath);
//        FileOutputStream outputStream = new FileOutputStream(file);
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//        long beginTime=System.currentTimeMillis();
//        TreeMap <Long,IpRelection> temp=new TreeMap<>();
//        IpRelection ipRelection=new IpRelection();
//        ipRelection.setCountryCode("ddd");
//        ipRelection.setIpFrom(293278937L);
//        temp.put(13232L,ipRelection);
//        objectOutputStream.writeObject(temp);
//
//
//        FileInputStream inputStream = new FileInputStream(file);
//        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//        TreeMap <Long,IpRelection>   temp1=(TreeMap <Long,IpRelection> )objectInputStream.readObject();
//        System.out.println(temp1);

//        int result=(int)(0.01*100);
//        System.out.println(result);
//
//        CyclicBarrier cyclicBarrier=new CyclicBarrier(10);
//        cyclicBarrier.await();
//
//
//        CountDownLatch countDownLatch=new CountDownLatch(1);
//        countDownLatch.countDown();
//        countDownLatch.await();
//        System.out.println("what");
//        countDownLatch.await();
//        System.out.println("what");

//        Executors.newCachedThreadPool();
//
//        Executors.newScheduledThreadPool(10);

//        ExecutorService executorService=Executors.newSingleThreadExecutor();
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("run");
//                throw  new RuntimeException("what");
//            }
//        });
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("run");
//                throw  new RuntimeException("what");
//            }
//        });
//        executorService.shutdown();
//        executorService.shutdownNow();
//
//
//        ExecutorService executorService1=Executors.newFixedThreadPool(1);
//        executorService1.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("run1");
//                throw  new RuntimeException("what");
//            }
//        });
//        executorService1.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("run1");
//                throw  new RuntimeException("what");
//            }
//        });
//        Executors.newCachedThreadPool();

//        Executors.newFixedThreadPool(10);

//        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(10);
//        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            @SneakyThrows
//            public void run() {
//                System.out.println("begin"+System.currentTimeMillis());
//                Thread.sleep(10000);
//                System.out.println("end"+System.currentTimeMillis());
//            }
//        },0,5, TimeUnit.SECONDS);
//
//        scheduledExecutorService.shutdown();
//        scheduledExecutorService.shutdownNow();
//
//        Thread.interrupted();

//        Hashtable hashtable=new Hashtable();
//        hashtable.put("one",null);

   new String("");




    }


    public synchronized void print(String name,Object ...attributes){
        System.out.println(name);

        for (Object attribute:attributes){
            System.out.println(attribute);
        }
    }

    @SneakyThrows
    public   synchronized   void printHaha(String name){
        System.out.println(name);
        Thread.sleep(10000);
        System.out.println(name);

    }

    public  static synchronized  void printTime(String name){
        System.out.println(System.currentTimeMillis()+name);
    }

    @SneakyThrows
    public synchronized  void printXIXI(String name){
        System.out.println("xixi"+name);
        Thread.sleep(10000);
        System.out.println("xixi"+name);
    }


}
