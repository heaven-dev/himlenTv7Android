package sv.tv7.himlentv7.helpers;

import org.json.JSONArray;

import static sv.tv7.himlentv7.helpers.Constants.CACHE_EXPIRATION_TIME;

public class ArchiveDataCacheItem {
    private JSONArray data = null;
    private long timestamp = 0;

    public ArchiveDataCacheItem(JSONArray data) {
        this.data = data;
        this.timestamp = sv.tv7.himlentv7.helpers.Utils.getTimeInMilliseconds();
    }

    public JSONArray getData() {
        return data;
    }

    public boolean isCacheValid() {
        return sv.tv7.himlentv7.helpers.Utils.getTimeInMilliseconds() < timestamp + CACHE_EXPIRATION_TIME;
    }

    public boolean isDataInIndex(int index) {
        return data != null && data.length() - 1 >= index;
    }
}
