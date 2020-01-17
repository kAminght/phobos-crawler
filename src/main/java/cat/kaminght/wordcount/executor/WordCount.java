package cat.kaminght.wordcount.executor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 词频统计对象
 *
 * @author kaminght
 * @create 2020/01/08-18:02
 */
public class WordCount {
    private final int threadNum;
    private ExecutorService threadPool;
    
    public WordCount(int threadNum) {
        threadPool = Executors.newFixedThreadPool(threadNum);
        this.threadNum = threadNum;
    }
    
    public Map<String, Integer> count(File file) throws FileNotFoundException, ExecutionException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            Future<Map<String, Integer>> future = threadPool.submit(new Callable<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> call() throws Exception {
                    String line;
                    Map<String, Integer> result = new HashMap<>();
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split(" ");
                        
                        for (String word : words) {
                            result.put(word, result.getOrDefault(word, 0) + 1);
                        }
                    }
                    return result;
                }
            });
            futures.add(future);
        }
        Map<String, Integer> finalResult = new HashMap<>();
        for (Future<Map<String, Integer>> future : futures) {
            Map<String, Integer> resultFromFuture = future.get();
            mergeResultIntoFinalResult(resultFromFuture, finalResult);
        }
        return finalResult;
    }
    
    private void mergeResultIntoFinalResult(Map<String, Integer> resultFromFuture,
                                            Map<String, Integer> finalResult) {
        for (Map.Entry<String, Integer> entry : resultFromFuture.entrySet()) {
            int mergeResult = finalResult.getOrDefault(entry.getKey(), 0) + entry.getValue();
            finalResult.put(entry.getKey(), mergeResult);
        }
    }
}
