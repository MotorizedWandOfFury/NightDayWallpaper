package diaz.nightdaywallpaper.datalayer;

/**
 * Created by Ping on 10/9/2014.
 */
public class NightDayDBRowModel {
    private String _rowType, _uri;
    private byte[] _thumbnail;

    public NightDayDBRowModel(String _rowType, String _uri, byte[] _thumbnail) {
        this._rowType = _rowType;
        this._uri = _uri;
        this._thumbnail = _thumbnail;
    }

    public String getRowType() {
        return _rowType;
    }

    public void setRowType(String _rowType) {
        this._rowType = _rowType;
    }

    public String getUri() {
        return _uri;
    }

    public void setUri(String _uri) {
        this._uri = _uri;
    }

    public byte[] getThumbnail() {
        return _thumbnail;
    }

    public void setThumbnail(byte[] _thumbnail) {
        this._thumbnail = _thumbnail;
    }
}
