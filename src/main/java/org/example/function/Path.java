package org.example.function;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private static List<String> path=new ArrayList<>();
    public static boolean addToPath(String str){
        path.add(str);
        return true;
    }
    public  static boolean delete(String str){
        if(!path.contains(str)){
            return false;
        }
        path.remove(str);
        return true;
    }
    public static void showPath(){
        for (int i = 0; i < path.size(); i++) {
            System.out.print("/" + path.get(i));
        }
        System.out.println();
    }
}
