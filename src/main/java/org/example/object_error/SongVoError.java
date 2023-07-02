package org.example.object_error;

import lombok.Data;
import org.example.enums.ErrorsEnum;
import org.example.vo.SongsVO;
@Data
public class SongVoError {
    private SongsVO songsVO;
    private ErrorsEnum e;
}
