package org.example.mainFunction;

import org.example.function.Album;
import org.example.function.Path;
import org.example.function.PlayList;

import java.util.Scanner;

public class HomePage {
    public HomePage(){
        boolean flag=false;
        Scanner scanner=new Scanner(System.in);
        String answer;
        PlayList playList=new PlayList();
        Album albumFunction=new Album();
        Path.addToPath("Home");
        Path.showPath();
        do {
            System.out.println("select show PlayList or create PlayList or delete PlayList");
            answer=scanner.nextLine();
            switch (answer){
                case "1":
                    flag=playList.getPlayListByUserId();
                    break;
                case "create PlayList":
                    flag=playList.addPlayListFront();
                    break;
                case "delete PlayList":
                    flag=playList.deletePlayListFront();
                    break;
                case "add Album":
                    albumFunction.addAlbumFront();
                case "Q":
                    flag=true;
                    break;
                default:
                    System.out.println("Wrong!!!");
            }
        }while (!flag);
    }
}
