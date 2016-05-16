package example.com.dreamshare;

/**
 * Created by Nancy on 5/3/2016.
 */
public class Dream {
    private String mName;
    private int mBirthYear;
    private String mLocation;
    private String mDreamDescription;

    public Dream(String name, int birthYear, String location, String dreamDescription) {
        mName = name;
        mBirthYear = birthYear;
        mLocation = location;
        mDreamDescription = dreamDescription;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getBirthYear() {
        return mBirthYear;
    }

    public void setBirthYear(int birthYear) {
        mBirthYear = birthYear;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDreamDescription() {
        return mDreamDescription;
    }

    public void setDreamDescription(String dreamDescription) {
        mDreamDescription = dreamDescription;
    }
}
