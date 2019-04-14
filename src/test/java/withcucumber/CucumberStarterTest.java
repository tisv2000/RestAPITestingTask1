package withcucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        features = "src/test/features",
        tags = "@FishTextTestApi"
)
public class CucumberStarterTest extends AbstractTestNGCucumberTests {

}
