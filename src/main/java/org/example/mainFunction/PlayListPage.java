package org.example.mainFunction;

import org.example.function.Path;
import org.example.function.PlayList;
import java.util.Scanner;

public class PlayListPage {
    public PlayListPage(){
        boolean flag=false;
        Scanner scanner=new Scanner(System.in);
        String answer;
        PlayList playListFunction=new PlayList();
        Path.addToPath("PlayList");
        Path.showPath();
        do {
            System.out.println("select show Song PlayList: ");
            answer=scanner.nextLine();
            switch (answer){
                case "show path":
                    Path.showPath();
                    break;
                case "show Song In PlayList":
                    flag=playListFunction.showSongInPlayListFront();
                    break;
                case "play Song":
                    playListFunction.playSongFront();
                    break;
                case "play PlayList":
                    playListFunction.playPlayListFront();
                    break;
                case "play Random PlayList":
                    playListFunction.playRandomPlayListFront();
                    break;
                case "Q":
                    Path.delete("PlayList");
                    flag=false;
            }

        }while (flag);
    }
}
