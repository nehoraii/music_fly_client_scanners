package org.example.function;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.connection.ServerFunction;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.mainFunction.SongPage;
import org.example.vo.*;
import org.example.vo.PlayListVOGet;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.*;

public class Song {
    private ServerFunction server=new ServerFunction();
    private float decibel= 6.0F;
    private boolean loopSong=false;
    private boolean loopPlayList=false;
    private byte[]song;
    private Clip clip;
    private boolean isPlay=false;
    private SongPage songPage=new SongPage();
    private boolean isNewSong=true;
    private int indexSongInPlayList=0;
    private List<SongVOGet> listSongMain;
    private String pathGetSongsInPlayList="http://localhost:8080/PlayList/getPlayListByPlayListId";
    private String pathGetSongById="http://localhost:8080/Song/getSongById";
    private List<SongVOGet> getInformationSongForPlayList(long playListId){
        PlayListVO playListVO=new PlayListVO();
        playListVO.setId(playListId);
        server.connect(pathGetSongsInPlayList, SendStatusEnum.POST,playListVO);
        String answer;
        answer=server.getMassageHead();
        List<SongVOGet>list;
        Gson gson=new Gson();
        Type songVoType = new TypeToken<List<SongVOGet>>(){}.getType();
        list=gson.fromJson(answer,songVoType);
        return list;
    }
    private SongVOGet getSong(long songId){
        SongsVO songsVO=new SongsVO();
        songsVO.setId(songId);
        server.connect(pathGetSongById,SendStatusEnum.POST,songsVO);
        String answer;
        answer=server.getMassageHead();
        SongVOGet songVOGet=new SongVOGet();
        Gson gson=new Gson();
        songVOGet=gson.fromJson(answer,songVOGet.getClass());
        return songVOGet;
    }
    public boolean getSong(String indexPlayList,String indexSong){
        try {
            int iPlayList=Integer.parseInt(indexPlayList);
            int iSong=Integer.parseInt(indexSong);
            List<PlayListVOGet>list=PlayList.getPlayListsUser();
            PlayListVOGet playListVOGet=list.get(iPlayList);
            List<SongVOGet> listInformation=getInformationSongForPlayList(playListVOGet.getId());
            SongVOGet theSong=listInformation.get(iSong);
            theSong=getSong(theSong.getId());
            setSong(theSong.getTheSong());
        }catch (Exception e){
            return false;
        }
        return true;
    }
    private void showSong(List<SongVOGet> list){
        for (int i = 0; i <list.size() ; i++) {
            System.out.println("________________________________");
            System.out.println(list.get(i).getNameSong());
            System.out.println("________________________________");
        }
    }
    public boolean showSongInPlayList(int playListIndex){
        PlayList playList=new PlayList();
        List<PlayListVOGet> playListVOS;
        playListVOS=playList.getPlayListsUser();
        List<SongVOGet> listSong;
        PlayListVOGet playListVO=playListVOS.get(playListIndex);
        listSong=getInformationSongForPlayList(playListVO.getId());
        showSong(listSong);
        return true;
    }
    public ErrorsEnum playSong(){
        if(isPlay){
            return ErrorsEnum.PLAYED;
        }
        if (!isNewSong){
            long currentPosition = clip.getMicrosecondPosition();
            clip.setMicrosecondPosition(currentPosition);
            clip.start();
            return ErrorsEnum.GOOD;
        }
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(song);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            isPlay=true;
            //Thread.sleep(clip.getMicrosecondLength() / 1000);
        }catch (Exception e){
            System.out.println(e);
            return ErrorsEnum.ERROR_OPEN_FILE;
        }
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum stopSong(){
        clip.stop();
        isPlay=false;
        isNewSong=false;
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum replay(){
        clip.setFramePosition(0);
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum upVolume(){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if(gainControl.getValue()+decibel > gainControl.getMaximum()){
            gainControl.setValue(gainControl.getMaximum());
            return ErrorsEnum.MAX_VOLUME;
        }
        gainControl.setValue(gainControl.getValue()+decibel);
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum downVolume(){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if(gainControl.getValue()-decibel<gainControl.getMinimum()){
            gainControl.setValue(gainControl.getMinimum());
            return ErrorsEnum.MIN_VOLUME;
        }
        gainControl.setValue(gainControl.getValue()-decibel);
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum next(){
        indexSongInPlayList++;
        indexSongInPlayList%=listSongMain.size();
        stopSong();
        isNewSong=true;
        SongVOGet songVOGet=listSongMain.get(indexSongInPlayList);
        String song=getSong(songVOGet.getId()).getTheSong();
        setSong(song);
        playSong();
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum before(){
        if(indexSongInPlayList==0){
            stopSong();
            setSong(getSong(listSongMain.get(indexSongInPlayList).getId()).getTheSong());
            isNewSong=true;
            playSong();
            return ErrorsEnum.MIN_VALUE;
        }
        indexSongInPlayList--;
        stopSong();
        isNewSong=true;
        setSong(getSong(listSongMain.get(indexSongInPlayList).getId()).getTheSong());
        playSong();
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum random(){
        ErrorsEnum e;
        e = randomThePlayList(listSongMain);
        return e;
    }
    public void loopSong(){
        loopSong=!loopSong;
    }
    public void loopPlayList(){
        loopPlayList=!loopPlayList;
    }
    public boolean isFinish(){
        return clip.isRunning();
    }
    private void setSong(String song){
        byte[] byteArray = Base64.getDecoder().decode(song);
        this.song=byteArray;
    }
    private ErrorsEnum randomThePlayList(List<SongVOGet>list){
        Random random=new Random();
        int indexRandom;
        for (int i = 0; i <list.size(); i++) {
            indexRandom=random.nextInt(list.size());
            Collections.swap(list, i, indexRandom);
        }
        return ErrorsEnum.GOOD;
    }
    public boolean PlayPlayList(PlayListVOGet playList){
        listSongMain=getInformationSongForPlayList(playList.getId());
        playListSong();
        return true;
    }
    public boolean PlayRandomPlayList(PlayListVOGet playList){
        listSongMain=getInformationSongForPlayList(playList.getId());
        randomThePlayList(listSongMain);
        playListSong();
        return true;
    }
    private void playListSong(){
        SongVOGet songSpe;
        do {
            for (; indexSongInPlayList < listSongMain.size(); indexSongInPlayList++) {
                songSpe=getSong(listSongMain.get(indexSongInPlayList).getId());
                setSong(songSpe.getTheSong());
                isPlay=false;
                isNewSong=true;
                do{
                    songPage.SongPage(Song.this);
                    isPlay=false;
                }while (loopSong);
            }
        }while (loopPlayList);
    }
    public void showQueue(){
        for (int i = indexSongInPlayList; i < listSongMain.size(); i++) {
            i%=listSongMain.size();
            System.out.println("_____________________________________________");
            System.out.println(listSongMain.get(i).getNameSong());
            System.out.println("_____________________________________________");
        }
    }

}
