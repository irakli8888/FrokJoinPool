
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static final String START_PAGE = "";
    public static final int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        LinkExecutor linkExecutor = new LinkExecutor(START_PAGE, START_PAGE);
        String siteMap = new ForkJoinPool(NUMBER_OF_PROCESSORS).invoke(linkExecutor);
        System.out
                .println("Время сканирования " + ((System.currentTimeMillis() - start) / 1000) + " сек.");
        writeFiles(getSort(siteMap));


    }

    private static String getSort(String map){
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        String array[] = map.split("\\n");
        ArrayList<String> list=  new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            list.add(array[i]);
        }
        Collections.sort(list);
        for(int i = 0 ; i < list.size(); i++){
            String array2 []=list.get(i).split("\\w*/\\w*");
            for (int a = 0; a < array2.length; a ++) {
                sb.append("\t");
            }
            sb2.append(sb+list.get(i) + "\n");
            sb.setLength(0);
        }
        return String.valueOf(sb2);
    }
    private static void writeFiles(String map) {
        String filePath = "siteMap.txt";
        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    }

