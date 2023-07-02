package org.example.object_error;

import lombok.Data;
import org.example.enums.ErrorsEnum;
import org.example.vo.AlbumsVO;

@Data
public class AlbumVoError {
    private AlbumsVO albumsVO;
    private ErrorsEnum e;
}
