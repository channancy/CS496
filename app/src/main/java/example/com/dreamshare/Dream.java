package example.com.dreamshare;

/**
 * Created by Nancy on 5/3/2016.
 */
public class Dream {
    private String mKey;
    private String mUser;
    private String mDescription;
    private String mDate;

    public Dream(String key, String user, String description, String date) {
        mKey = key;
        mUser = user;
        mDescription = description;
        mDate = date;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
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
