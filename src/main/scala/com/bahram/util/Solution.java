package main.scala.com.bahram.util;

import java.util.LinkedList;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

//        Scanner sc = new Scanner(System.in);
//        int n = sc.nextInt();
//        LinkedList<Integer> list = new LinkedList<Integer>();
//        for (int i = 0; i < n; ++i)
//            list.add(sc.nextInt());
//        boolean flag = true;
//        int count = -1;
//        while (flag) {
//            ++count;
//            flag = false;
//            ListIterator<Integer> iter = list.listIterator();
//            Integer last = iter.next();
//            while (iter.hasNext()) {
//                Integer a2 = iter.next();
//                if (a2 != null) {
//                    if (last < a2) {
//                        flag = true;
//                        iter.remove();
//                    } else System.out.print(a2 + " ");
//                    last = a2;
//                }
//            }
//            System.out.println();
//        }
//        System.out.println(count);

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Integer[] p = new Integer[n];
        for (int i = 0; i < n; ++i)
            p[i] = sc.nextInt();
        LinkedList<MyClass> s = new LinkedList<MyClass>();
        int max = -1;
        s.push(new MyClass(0, -1));
        for (int i = 1; i < n; ++i) {
            int daysToDie = 1;
            while (!s.isEmpty()) {
                if (p[s.getLast().index] >= p[i]) {
                    daysToDie = Math.max(daysToDie, s.getLast().daysToDie + 1);
                    s.pop();
                } else break;
            }
            if (s.isEmpty()) {
                daysToDie = -1;
            }
            max = Math.max(max, daysToDie);
            s.push(new MyClass(i, daysToDie));
        }
        System.out.println(Math.max(0, max));
    }

    private static class MyClass {
        MyClass(int index, int daysToDie) {
            this.index = index;
            this.daysToDie = daysToDie;
        }

        int index;
        int daysToDie;
    }
}
