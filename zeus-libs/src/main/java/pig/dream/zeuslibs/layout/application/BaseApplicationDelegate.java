package pig.dream.zeuslibs.layout.application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhukun on 2017/5/14.
 */

public class BaseApplicationDelegate {

    private List<SonApplication> applications = new ArrayList<>();

    public void onCreate() {
        for (SonApplication sonApplication: applications) {
            sonApplication.onCreate();
        }
    }

    public void addSonApplication(SonApplication sonApplication) {
        applications.add(sonApplication);
    }

    public void removeSonApplication(SonApplication sonApplication) {
        applications.remove(sonApplication);
    }
}
