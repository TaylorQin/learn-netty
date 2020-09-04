import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SearchMissingNumberTest {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        Map<Integer, Boolean> mark = new HashMap<>();
        while (in.hasNext()) {// 注意，如果输入是多个测试用例，请通过while循环处理多个测试用例
            String inputString = in.next();
            String[] nums = inputString.split(",");
            for (int i = 0; i < nums.length; i++) {
                int num = -1;
                try {
                    num = Integer.parseInt(nums[i]);
                } catch (Exception e) {
                    throw new IllegalArgumentException("请输入合法的数字，且以逗号分隔。");
                }

                mark.put(num, true);
            }

            boolean found = false;
            for (int i = 0; i < nums.length; i++) {
                if(mark.get(i)==null) {
                    if(found)
                        throw new IllegalArgumentException("输入不合法。");
                    System.out.println(i);
                    found = true;
                }
            }
        }

    }
}
