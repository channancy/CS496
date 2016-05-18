package example.com.dreamshare;

/**
 * Created by Nancy on 5/3/2016.
 */
public class Dream {
    private String mKey;
    private String mUsername;
    private String mDescription;
    private String mDate;

    public Dream(String key, String username, String description, String date) {
        mKey = key;
        mUsername = username;
        mDescription = description;
        mDate = date;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
