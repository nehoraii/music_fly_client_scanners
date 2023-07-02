package org.example.vo;

import lombok.Data;
import org.example.enums.ErrorsEnumForSongs;

import java.util.Date;
@Data
public class SongVOGet {
    private long id;
    private long userId;
    private String theSong;
    private String zaner;
    private Date date;
    private String nameSong;
    private ErrorsEnumForSongs e;
}
