package org.example;

import org.example.function.Album;
import org.example.mainFunction.AlbumPage;
import org.example.mainFunction.ConOrCreate;
import org.example.mainFunction.HomePage;
import org.example.mainFunction.PlayListPage;

public class Main {
    public static void main(String[] args) {
        /*
        ConOrCreate conOrCreate=new ConOrCreate();
        conOrCreate.ConOrCreate();
        new HomePage();
        new PlayListPage();
         */
        Album album=new Album();
        album.addAlbumFront();
    }
}