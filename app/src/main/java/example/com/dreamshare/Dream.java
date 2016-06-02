package example.com.dreamshare;

/**
 * Created by Nancy on 5/3/2016.
 */
public class Dream {
    private String mKey;
    private String mUser_key;
    private String mUsername;
    private String mDescription;
    private String mDate;
    private String mLocation;

    public Dream(String key, String user_key, String username, String description, String date, String location) {
        mKey = key;
        mUser_key = user_key;
        mUsername = username;
        mDescription = description;
        mDate = date;
        mLocation = location;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getUser_key() {
        return mUser_key;
    }

    public void setUser_key(String user_key) {
        mUser_key = user_key;
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

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
