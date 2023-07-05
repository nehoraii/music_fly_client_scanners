package org.example.function;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.connection.ServerFunction;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.mainFunction.SongPage;
import org.example.vo.PlayListVOGet;
import org.example.vo.SongVOGet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PlayList {
    private ServerFunction server=new ServerFunction();
    private String pathGetPlayListsByUserId="http://192.168.1.22:8080/PlayList/getPlayListByUserId";
    private String pathAddPlayList="http://192.168.1.22:8080/PlayList/save";
    private String pathDelPlayList="http://192.168.1.22:8080/PlayList/delete";
    private String pathGetPlayListById="http://192.168.1.22:8080/PlayList/getPlayListByPlayListId";
    private static List<PlayListVOGet> playListsUser;

    public static List<PlayListVOGet> getPlayListsUser() {
        return playListsUser;
    }

    private List<PlayListVOGet> getPlayListByUserId(long userId){
        PlayListVOGet playListVOGet=new PlayListVOGet();
        playListVOGet.setUserId(userId);
        List<PlayListVOGet>list;
        server.connect(pathGetPlayListsByUserId, SendStatusEnum.POST,playListVOGet);
        String answer;
        answer=server.getMassageHead();
        Gson gson=new Gson();
        Type playlistType = new TypeToken<List<PlayListVOGet>>(){}.getType();
        list = gson.fromJson(answer, playlistType);
        playListsUser=list;
        return list;
    }
    private void showPlayLists(List<PlayListVOGet> list){
        PlayListVOGet PlayListVOGet;
        for (int i = 0; i <list.size(); i++) {
            PlayListVOGet=list.get(i);
            System.out.println("___________________________" +
                    "_______________________________________");
            System.out.print(i+"|");
            System.out.println(PlayListVOGet.getPlayListName());
            System.out.println("___________________________" +
                    "________________________________________");
        }
    }
    private ErrorsEnum addPlayList(String playListName , boolean pub){
        PlayListVOGet PlayListVOGet=new PlayListVOGet();
        PlayListVOGet.setPlayListName(playListName);
        PlayListVOGet.setPublic1(pub);
        ErrorsEnum e;
        e=server.connect(pathAddPlayList,SendStatusEnum.POST,PlayListVOGet);
        return e;
    }
    public boolean addPlayListFront(){
        Scanner scanner=new Scanner(System.in);
        String namePlayList , pub;
        boolean publicc;
        System.out.println("Enter Name For Your PlayList");
        namePlayList=scanner.nextLine();
        System.out.println("Enter true for public and false for private");
        pub=scanner.nextLine();
        if(pub.equals("true")){
            publicc=true;
        }
        else {
            publicc=false;
        }
        ErrorsEnum e;
        e=addPlayList(namePlayList,publicc);
        if(e!=ErrorsEnum.GOOD){
            return false;
        }
        return true;

    }
    public boolean getPlayListByUserId(){
        User user=new User();
        long userId;
        List<PlayListVOGet> list;
        userId=user.getUserId();
        list=getPlayListByUserId(userId);
        showPlayLists(list);
        return true;
    }
    private ErrorsEnum deletePlayList(long playListId){
        PlayListVOGet PlayListVOGet=new PlayListVOGet();
        PlayListVOGet.setId(playListId);
        ErrorsEnum e;
        e=server.connect(pathDelPlayList,SendStatusEnum.DELETE,PlayListVOGet);
        return e;
    }
    public boolean deletePlayListFront(){
        Scanner scanner=new Scanner(System.in);
        String indexPlayList=scanner.nextLine();
        int index=Integer.parseInt(indexPlayList);
        List<PlayListVOGet>PlayListVOGetS=getPlayListsUser();
        PlayListVOGet PlayListVOGet=PlayListVOGetS.get(index);
        ErrorsEnum e;
        e=deletePlayList(PlayListVOGet.getId());
        if(e!=ErrorsEnum.GOOD){
            return false;
        }
        return true;
    }
    public boolean playSongFront(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter PlayList Number: ");
        String playList=scanner.nextLine();
        System.out.println("Enter Song number:  ");
        String song=scanner.nextLine();
        new SongPage().SongPage(playList,song);
        return true;
    }
    public boolean playPlayListFront(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter PlayList number");
        String playListNum=scanner.nextLine();
        PlayListVOGet playListVOGet=getPlayListByIndex(playListNum);
        Song songFunction=new Song();
        boolean answer;
        answer=songFunction.PlayPlayList(playListVOGet);
        return answer;

    }
    private PlayListVOGet getPlayListByIndex(String index){
        int i=Integer.parseInt(index);
        List<PlayListVOGet> list=getPlayListsUser();
        PlayListVOGet playListVO=list.get(i);
        return playListVO;
    }
    public boolean showSongInPlayListFront(){
        Scanner scanner=new Scanner(System.in);
        String numberPlayList;
        int playListIndex;
        System.out.println("Choose num playlist");
        numberPlayList=scanner.nextLine();
        playListIndex=Integer.parseInt(numberPlayList);
        Song songFunction=new Song();
        songFunction.showSongInPlayList(playListIndex);
        return true;
    }
    public boolean playRandomPlayListFront(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter playList index: ");
        String index=scanner.nextLine();
        PlayListVOGet playListVOGet=getPlayListByIndex(index);
        Song songFunction=new Song();
        boolean answer;
        answer=songFunction.PlayRandomPlayList(playListVOGet);
        return answer;
    }
}
