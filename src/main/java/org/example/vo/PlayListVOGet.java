package org.example.vo;

import lombok.Data;
import java.util.Date;

@Data
public class PlayListVOGet{
    private Long id;
    private long userId;
    private String playListName;
    private long lengthPlayList;
    private boolean public1;
    private Date date;
    private String image;
}
