package test;

import cn.hutool.core.util.RandomUtil;

public class RandomTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String string = RandomUtil.randomStringUpper(8);
            System.out.println(string);
        }
    }
}
