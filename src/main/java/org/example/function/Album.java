package org.example.function;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.example.connection.ServerFunction;
import org.example.enums.ErrorsEnum;
import org.example.enums.SendStatusEnum;
import org.example.object_error.AlbumVoError;
import org.example.object_error.SongVoError;
import org.example.vo.AlbumsVO;
import org.example.vo.ConnectionSongAlbumsVO;
import org.example.vo.SongVOGet;
import org.example.vo.SongsVO;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Album {
    private ServerFunction server=new ServerFunction();
    private String pathSaveAlbum="http://localhost:8080/Albums/save";
    private String pathSaveSong="http://localhost:8080/Song/save";
    private String pathToAddCon="http://localhost:8080/ConSongAlbum/save";
    public boolean addAlbumFront(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("How many Song");
        String count=scanner.nextLine();
        int countInt=Integer.parseInt(count);
        SongVoError songVoError;
        List<Long> listIdSong=new ArrayList<>();
        long lengthAlbum=0;
        for (int i = 0; i <countInt; i++) {
            System.out.println("Enter path for "+ (i+1) +" Song:");
            String pathToSong = scanner.nextLine();
            System.out.println("Enter name Song: ");
            String nameSong=scanner.nextLine();
            System.out.println("Enter zaner Song: ");
            String zaner=scanner.nextLine();
            songVoError=addSong(nameSong,zaner,pathToSong,User.getUserId());
           // lengthAlbum+=getAudioDurationInSeconds(songVoError.getSongsVO().getTheSong());
            listIdSong.add(songVoError.getSongsVO().getId());
            }
        AlbumVoError albumVoError;
        System.out.println("name to Album");
        String nameAlbum=scanner.nextLine();
        System.out.println("Path to image in your computer");
        String pathToImage=scanner.nextLine();
        albumVoError=addAlbum(nameAlbum,pathToImage,lengthAlbum);
        for (int i = 0; i < listIdSong.size(); i++) {
            addSongToAlbum(albumVoError.getAlbumsVO().getId(),listIdSong.get(i));
        }
        return true;
    }

    private AlbumVoError addAlbum(String nameAlbum,String pathToImage,long length){
        AlbumVoError albumVoError=new AlbumVoError();
        File file=new File(pathToImage);
        if (!file.exists()){
            albumVoError.setE(ErrorsEnum.FILE_NOT_EXIST);
            return albumVoError;
        }
        byte[] imageBytes;
        try {
            imageBytes = FileUtils.readFileToByteArray(file);
        }catch (Exception e){
            System.out.println(e);
            albumVoError.setE(ErrorsEnum.CANT_CONVERT);
            return albumVoError;
        }
        AlbumsVO albumsVO=new AlbumsVO();
        albumsVO.setImageAlbum(imageBytes);
        albumsVO.setNameAlbum(nameAlbum);
        albumsVO.setUserId(User.getUserId());
        albumsVO.setLengthAlbum(length);
        server.connect(pathToImage, SendStatusEnum.POST,albumsVO);
        String answer=server.getMassageHead();
        System.out.println(answer);
        Gson gson=new Gson();
        albumsVO=gson.fromJson(answer,albumsVO.getClass());
        albumVoError.setAlbumsVO(albumsVO);
        albumVoError.setE(ErrorsEnum.GOOD);
        return albumVoError;
    }
    private void addSongToAlbum(long albumId,long songId){
        ConnectionSongAlbumsVO con=new ConnectionSongAlbumsVO();
        con.setAlbumId(albumId);
        con.setSongId(songId);
        server.connect(pathToAddCon,SendStatusEnum.POST,con);
    }
    private SongVoError addSong(String nameSong,String zaner, String pathToFile,long userId){
        SongVoError songVoError=new SongVoError();
        SongsVO songsVO=new SongsVO();
        File file=new File(pathToFile);
        if(!file.exists()){
            songVoError.setE(ErrorsEnum.FILE_NOT_EXIST);
            return songVoError;
        }
        byte[] song;
        try {
            song=FileUtils.readFileToByteArray(file);
        }catch (Exception e){
            System.out.println(e);
            songVoError.setE(ErrorsEnum.CANT_CONVERT);
            return songVoError;
        }
        songsVO.setNameSong(nameSong);
        songsVO.setDate(new Date());
        songsVO.setUserId(userId);
        songsVO.setZaner(zaner);
        songsVO.setTheSong(song);
        server.connect(pathSaveSong,SendStatusEnum.POST,songsVO);
        String answer=server.getMassageHead();
        Gson gson=new Gson();
        SongVOGet songVOGet=new SongVOGet();
        songVOGet=gson.fromJson(answer,songVOGet.getClass());
        copyProperty(songVOGet,songsVO);
        songVoError.setSongsVO(songsVO);
        songVoError.setE(ErrorsEnum.GOOD);
        return songVoError;
    }
    private long getAudioDurationInSeconds(byte[] audioData) {
        long durationInSeconds = (long) 0.0;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(bais);
            long microsecondDuration = (long) fileFormat.properties().get("duration");
            durationInSeconds = (long) (microsecondDuration / 1_000_000.0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return durationInSeconds;
    }
    private void copyProperty(SongVOGet from, SongsVO to){
        to.setId(from.getId());
        to.setDate(from.getDate());
        to.setZaner(from.getZaner());
        byte[] byteArray = Base64.getDecoder().decode(from.getTheSong());
        to.setTheSong(byteArray);
        to.setNameSong(from.getNameSong());
        to.setE(from.getE());
        to.setUserId(from.getUserId());
    }
}
