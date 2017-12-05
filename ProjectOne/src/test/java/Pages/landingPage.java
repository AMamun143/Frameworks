package Pages;

import base.CommonAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;


public class landingPage extends CommonAPI {

    public static Logger Log = LogManager.getLogger(landingPage.class.getName());

    @Test (priority = 1, enabled = false)
    public void title(){
        String title1 = getTitle();

        Log.info(title1);
    }

    @Test(priority = 2, enabled = false)
    public void getLinks(){
        getLinks("a");
    }

    @Test (priority = 3, enabled = false)
    public void clickSolutions(){
        clickByXpath(".//*[@id='stNavigation']/div/nav/ul/li[1]/a");
        String SolURL = getCurrentPageUrl();
        Log.info(SolURL);
        System.out.println(SolURL);
        navigateBack();

    }

    @Test
    public void Links(){
        Log.info( "Checking for broken links" );
        validateBrokeLinks();
        Log.info( "No broken links found" );
    }

    @Test(enabled = false)
    public void brokenImages(){
        validateInvalidImages();
    }


}
