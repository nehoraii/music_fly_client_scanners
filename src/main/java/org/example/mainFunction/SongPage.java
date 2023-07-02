package org.example.mainFunction;

import org.example.function.Path;
import org.example.function.Song;

import java.util.Scanner;

public class SongPage {
    public  void SongPage(String indexPlayList,String indexSongInPlayList){
        boolean flag;
        Scanner scanner=new Scanner(System.in);
        Song songFunction=new Song();
        String answer;
        flag=songFunction.getSong(indexPlayList,indexSongInPlayList);

        do {
            System.out.println("S-stop , P-play , R-Play again , Q-Quiet");
            answer=scanner.nextLine();
            answer=answer.toUpperCase();
            switch (answer){
                case "P":
                    songFunction.playSong();
                    break;
                case "S":
                    songFunction.stopSong();
                    break;
                case "R":
                    songFunction.replay();
                    break;
                case"Q":
                    songFunction.stopSong();
                    flag=false;
                    break;
                default:
                    System.out.println("Wrong!!!");
            }

        }while (flag);
    }
    public void SongPage(){
        boolean flag=true;
        Scanner scanner=new Scanner(System.in);
        Song songFunction=new Song();
        String answer;
        do {
            System.out.println("S-stop , P-play , R-Play again , Q-Quiet");
            answer=scanner.nextLine();
            answer=answer.toUpperCase();
            switch (answer){
                case "P":
                    songFunction.playSong();
                    break;
                case "S":
                    songFunction.stopSong();
                    break;
                case "R":
                    songFunction.replay();
                    break;
                case"Q":
                    songFunction.stopSong();
                    flag=false;
                    break;
                default:
                    System.out.println("Wrong!!!");
            }

        }while (flag);
    }
    public void SongPage(Song songFunction){
        boolean flag=true;
        Scanner scanner=new Scanner(System.in);
        String answer;
        Path.addToPath("Song");
        Path.showPath();
        boolean isPlay=false;
        do {
            System.out.println("S-Stop , P-Play , R-Play again , +-Up volume , - -Down volume Q-Quiet");
            answer=scanner.nextLine();
            answer=answer.toUpperCase();
            switch (answer){
                case "P":
                    songFunction.playSong();
                    isPlay=true;
                    break;
                case "S":
                    songFunction.stopSong();
                    isPlay=false;
                    break;
                case "R":
                    songFunction.replay();
                    isPlay=true;
                    break;
                case "+":
                    songFunction.upVolume();
                    isPlay=true;
                    break;
                case "-":
                    songFunction.downVolume();
                    isPlay=true;
                    break;
                case "N":
                    songFunction.next();
                    isPlay=true;
                    break;
                case "LS":
                    songFunction.loopSong();
                    break;
                case "LP":
                    songFunction.loopPlayList();
                    break;
                case "B":
                    songFunction.before();
                    isPlay=true;
                    break;
                case "T":
                    songFunction.showQueue();
                    isPlay=false;
                    break;
                case "RAN":
                        songFunction.random();
                    break;
                case"Q":
                    songFunction.stopSong();
                    Path.delete("Song");
                    flag=false;
                    break;
                default:
                    System.out.println("Wrong!!!");
            }
            if(isPlay && (!songFunction.isFinish())){
                Path.delete("Song");
                return;
            }
        }while (flag);
    }
}
