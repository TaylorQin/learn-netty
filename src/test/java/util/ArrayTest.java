package util;

import com.learn.util.Array;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class ArrayTest {

    @Test
    public void testAdd() {
        Array<Integer> arr = new Array<>();
        arr.addFirst(1);
        arr.addFirst(2);
        System.out.println(arr.removeFirst());
        System.out.println(arr.removeFirst());
        Map<Integer,Integer> map = new HashMap<>();
        map.putIfAbsent(1,1);

        CyclicBarrier barrier;
    }

}
