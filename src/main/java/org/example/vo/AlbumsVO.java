package org.example.vo;

import lombok.Data;
import org.example.enums.ErrorsEnumForAlbums;

@Data
public class AlbumsVO {
    private Long id;
    private long userId;
    private String nameAlbum;
    private byte[] imageAlbum;
    private long lengthAlbum;
    private ErrorsEnumForAlbums e;

}
