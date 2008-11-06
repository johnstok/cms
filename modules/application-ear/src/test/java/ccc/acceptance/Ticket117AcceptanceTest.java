
package ccc.acceptance;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


public class Ticket117AcceptanceTest
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
        selenium.click("assets-navigator");
        selenium.click("//div[@id='assets']/div[1]/table/tbody/tr/td[2]/div");
        selenium.click("//div[@id='templates']/div[1]/table/tbody/tr/td[6]/span");
//        selenium.mouseUpRightAt("//div[@id='ash_display.jsp']/table/tbody/tr/td[1]/div/div", "");
        selenium.click("//*[@id=\"edit-resource\"]");
        selenium.type("//input[@type='text']", "foo");
        selenium.type("//tr[2]/td[2]/input", "foo");
        selenium.type("//textarea", "foo");
        selenium.click("gwt-debug-Save");
//        selenium.mouseDownRightAt("//div[@id='ash_display.jsp']/table/tbody/tr/td[2]/div/div", "");
        selenium.click("edit-resource");
        assertEquals("foo", selenium.getValue("//input[@type='text']"));
        assertEquals("foo", selenium.getValue("//tr[2]/td[2]/input"));
        assertEquals("foo", selenium.getValue("//textarea"));
    }
}
