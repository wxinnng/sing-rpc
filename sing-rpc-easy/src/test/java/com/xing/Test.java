package com.xing;

import com.xing.serializer.Serializer;

import java.io.IOException;
import java.util.*;

public class Test {

    public static void main(String[] args) {
        Map<String,Integer> people = new HashMap<>();
        String ans = "";
        Integer max = 0;
        //遍历hashMap
        //1.
        Iterator<String> iterator = people.keySet().iterator();
        while(iterator.hasNext()){
            if(max < people.get(iterator.next())){
                max = people.get(iterator.next());
                ans = iterator.next();
            }
        }

        //2. forEach
        people.forEach((k,v)->{
            //TODO：进行判断
        });

        //3. stream
    }

    public void test(){
        List<String> list = new ArrayList<>();
        Map<String,Integer> ans = new HashMap<>();
        for(String str:list){
            ans.put(str,ans.getOrDefault(str,0) + 1);
        }

    }

    public void test2(){
        List<String> list = new ArrayList<>();
        List<String> number  = new ArrayList<>();
        List<String> digit = new ArrayList<>();
        for(String str:list){
            if (way(str) == 1){
                number.add(str);
            }else{
                digit.add(str);
            }
        }
    }

    private int way(String str){
        char ch = str.charAt(0);
        if(ch >= '0' && ch <= '9')
            return 1;
        else
            return 2;
    }

    public void test3(){
        Map<String,List<Integer>> students = new HashMap<>();
        Iterator<String> iterator = students.keySet().iterator();
        Map<String,Double> ans = new HashMap<>();
        while(iterator.hasNext()){
            //
            String key = iterator.next();
            List<Integer> scores = students.get(key);
            int sum = 0;
            for(Integer score:scores){
                sum += score;
            }
            ans.put(key,(double) sum / scores.size());
        }
    }

    public void test4(){
        List<String> info = new ArrayList<>();
        Map<String,String> ans = new HashMap<>();
        for(String i :info){
            String[] str = i.split(",");
            ans.put(str[0],str[1]);
        }
    }

    public void last(){
        int[][] map = new int[6][6];
    }
}
