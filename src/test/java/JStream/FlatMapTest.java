package JStream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FlatMapTest {

    @Test
    public void whenFlatMapEmployeeNames_thenGetNameStream() {
        List<List<String>> namesNested = Arrays.asList(
                Arrays.asList("Jeff", "Bezos"),
                Arrays.asList("Bill", "Gates"),
                Arrays.asList("Mark", "Zuckerberg"));

        List<String> namesFlatStream = namesNested.stream()
                // flatMap 接收的函数参数
                // 返回类型 流（Steam）类型
                // flatMap 将流内的元素抽出来，合并/替换原来的流
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // 经过flatMap 的处理，重新聚合的流是一个新的流
        // 元素个数为原数组内的所有数组的 size 之和
        assertEquals(namesFlatStream.size(), namesNested.size() * 2);
    }


}
