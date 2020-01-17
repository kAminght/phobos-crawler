package cat.kaminght.wordcount.executor;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kaminght
 * @create 2020/01/08-18:40
 */
class WordCountTest {
    WordCount wordCount;
    
    @BeforeEach
    void setUp() {
        wordCount = new WordCount(10);
    }
    
    @Test
    void testCount() throws IOException, ExecutionException, InterruptedException {
        List<String> aToZ = IntStream.range(0, 26)
            .mapToObj(i -> String.valueOf((char) ('a' + i)))
            .collect(Collectors.toList());
        int count = new Random().nextInt(10) + 100;
        List<String> randomStrings = new ArrayList<>();
        for (String letter : aToZ) {
            randomStrings.addAll(Collections.nCopies(count, letter));
        }
        Collections.shuffle(randomStrings);
        File file = writeToTempFile(count, randomStrings);
        
        Map<String, Integer> countResult = wordCount.count(file);
        System.out.println(countResult);
        Assertions.assertTrue(countResult.values().stream().allMatch(v -> v == count));
    }
    
    private File writeToTempFile(int count, List<String> randomStrings) throws IOException {
        File file = File.createTempFile("tmp", "");
        List<String> lines = Lists.partition(randomStrings, 10).stream()
            .map(list -> String.join(" ", list))
            .collect(Collectors.toList());
        Files.write(file.toPath(), lines);
        return file;
    }
}