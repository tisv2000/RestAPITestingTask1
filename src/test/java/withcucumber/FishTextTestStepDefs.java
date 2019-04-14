package withcucumber;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.testng.Assert;

import static common.FishTextTestUtils.*;
import static org.hamcrest.Matchers.is;

public class FishTextTestStepDefs {

    private String type;
    private int number;
    private Response response;

    @Given("^two parameters (.*) and (-?\\d+)$")
    public void twoParametersTypeAndNumber(String type, int number) {
        this.type = type;
        this.number = number;
    }

    @When("^send GET request, specifying two parameters, in order to get json")
    public void sendGETRequestSpecifyingTwoParametersTypeOfTheReturningTextAndNumberOfItemsInOrderToGetJson() {
        RestAssured.registerParser("text/html", Parser.JSON);
        response = sendRequest(type, number, FORMAT_JSON);
    }

    @Then("^the status code should be (\\d+)$")
    public void theStatusCodeShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("^result field in json should be '(.*)'$")
    public void resultFieldInJsonShouldBeSuccess(String expectedStatus) {
        response.then().body(JSON_FIELD_STATUS, is(expectedStatus));
    }

    @And("^number of elements in json should equal (\\d+)$")
    public void numberOfElementsShouldEqualExpected(int expectedNumber) {
        Assert.assertEquals(countItemsNumber(response.jsonPath().getString(JSON_FIELD_TEXT), type, FORMAT_JSON),
                expectedNumber, getInvalidAmountMessage(type));
    }


    @When("^send GET request, specifying two parameters, in order to get html$")
    public void sendGETRequestSpecifyingTwoParametersTypeOfTheReturningTextAndNumberOfItemsInOrderToGetHtml() {
        response = sendRequest(type, number, FORMAT_HTML);
    }

    @And("^number of elements in html should equal (-?\\d+)$")
    public void numberOfElementsInHtmlShouldEqualExpected(int expectedNumber) {
        Assert.assertEquals(countItemsNumber(response.then().extract().asString(), type, FORMAT_HTML),
                expectedNumber, getInvalidAmountMessage(type));
    }

    @And("^json error code should equal (\\d+)$")
    public void jsonErrorCodeShouldEqualExpected(int errorCode) {
        response.then().body(JSON_FIELD_ERROR_CODE, is(errorCode));
    }

    @And("^json error text should equal (.*)$")
    public void jsonErrorTextShouldEqualErrorText(String errorText) {
        response.then().body(JSON_FIELD_TEXT, is(errorText));
    }

    @And("^html text should equal (.*)$")
    public void htmlTextShouldEqualErrorText(String errorText) {
        Assert.assertEquals(response.then().extract().asString(), errorText);
    }

}
