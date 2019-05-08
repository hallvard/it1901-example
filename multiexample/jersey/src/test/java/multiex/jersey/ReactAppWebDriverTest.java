package multiex.jersey;

import java.net.URI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ReactAppWebDriverTest extends MultiExJerseyTest {

	private static final String REACT_APP_PATH = ReactApp.REACT_APP_PATH;

	private WebDriver driver;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		driver = new ChromeDriver();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		if (driver != null) {
			driver.quit();
		}
		super.tearDown();
	}

	@Test
	public void testShowApp() throws InterruptedException {
		final URI uri = target(REACT_APP_PATH).path("index.html").getUri();
		// Optional, if not specified, WebDriver will search your path for chromedriver.
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		driver.get(uri.toString());
		Thread.sleep(5000);  // Let the user actually see something!
		final WebElement latLongList = driver.findElement(By.xpath("//td[@data-testid='latLong0']"));
		Assert.assertNotNull(latLongList);
		Thread.sleep(5000);  // Let the user actually see something!
	}
}
