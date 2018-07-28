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

        int result=(int)(0.01*100);
        System.out.println(result);








    }


    public static void print(String name,Object ...attributes){
        System.out.println(name);

        for (Object attribute:attributes){
            System.out.println(attribute);
        }
    }
}
