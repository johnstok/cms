
package ccc.acceptance;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


public class Ticket25AcceptanceTest
    extends
        TestCase {

    private Selenium     selenium;
    private final int    port    = 5555;
    private final String browser = "*custom firefox -P Selenium -no-remote";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {

        final String url = "http://localhost:8080/";
        selenium = new DefaultSelenium("localhost", port, browser, url);
        selenium.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tearDown() throws Exception {

        selenium.stop();
    }

    public void testNew() throws Exception {
        selenium.setSpeed("2000");
        selenium.open("/content-creator/");
        selenium.click("user-navigator");
        selenium.click("//div[@id='Users']/div[1]/table/tbody/tr/td[2]/div");
        selenium.click("//div[@id='All']/div[1]/table/tbody/tr/td[2]/div");
        selenium.click("//div[@id='Content creator']/div[1]/table/tbody/tr/td[6]/span");
//        selenium.mouseUpRightAt("//div[@id='UserGrid']/div/div/div[2]/div/div/", "");
        selenium.click("editUserMenu");
        selenium.type("Username", "admin_test");
        selenium.type("Email", "test@civiccomputing.com");
        selenium.click("//table[@id='userSave']/tbody/tr/td[2]/em/button");

        selenium.click("//div[@id='Search']/div[1]/table/tbody/tr/td[6]/span");
        selenium.click("//table[@id='searchButton']/tbody/tr/td[2]/em/button");
        selenium.type("searchString", "admin_test");
        selenium.click("//table[@id='searchButton']/tbody/tr/td[2]/em/button");
//        selenium.mouseUpRightAt("//div[@id='UserGrid']/div/div/div[2]/div/div/", "");
        selenium.click("editUserMenu");
        assertEquals("admin_test", selenium.getValue("Username"));
        assertEquals("test@civiccomputing.com", selenium.getValue("Email"));


    }
}
