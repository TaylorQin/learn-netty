package com.learn;

import com.sun.tools.corba.se.idl.IncludeGen;

public class Test {

    final static char[] digits = {
            '0' , '1'
    };

    public static void main(String[] args) {
//        for (int i = -10; i < 10; i++) {
//            System.out.println(toBinaryString(i));
//            System.out.println(Integer.toBinaryString(i));
//        }
//        System.out.println(toBinaryString(Integer.MIN_VALUE));
//        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
//        System.out.println(toBinaryString(Integer.MAX_VALUE));
//        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
//
//
//
        String s1 = "ab";
        String s2 = "ab";
        System.out.println(s1 == s2);

        String s3 = "a";
        String s4 = "b";
        System.out.println(s1 == s3 + s4);

        final String s5 = "a";
        final String s6 = "b";
        System.out.println(s1 == s5 + s6);

        String s7 = new String("a");
        String s8 = new String("b");
        System.out.println(s1 == s7 + s8);
//
//
//        String s12 = new String("ab");
//        String s13 = new String("ab");
//        System.out.println(s12==s13);
//
//        Integer i=120,j=120,m=150,n=150;
//
//        System.out.println(i==j);
//        System.out.println(m==n);
        Integer i = 127, j = 127;
        while (i < 129 && j < 129) {
            print(i++, j++);
        }

        print(i-2,j-2);
        print(i+1,j+1);

    }

    private static void print(Integer i,Integer j) {
        System.out.println(i +" == " + j + " ? : " + (i == j));
    }

    static String toBinaryString(int i) {
        char[] chars = new char[32];
        for (int j = 31; j >= 0; j--) {
            chars[j] = digits[i & 1];
            i = i >> 1;
        }
        int startPos = 31;
        for (int j = 0; j < chars.length; j++) {
            if(chars[j] != '0') {
                startPos = j;
                break;
            }
        }
        return new String(chars,startPos,32-startPos);
    }

}
