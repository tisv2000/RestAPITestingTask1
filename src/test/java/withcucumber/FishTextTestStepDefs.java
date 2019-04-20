package withcucumber;

import cucumber.api.java8.En;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.testng.Assert;

import static common.FishTextTestUtils.*;
import static org.hamcrest.Matchers.is;

public class FishTextTestStepDefs implements En {

    private String type;
    private int number;
    private Response response;

    public FishTextTestStepDefs() {
        Given("two parameters {word} and {int}",
                (String type, Integer number) -> {
                    this.type = type;
                    this.number = number;
                });

        When("send GET request, specifying two parameters, in order to get json",
                () -> {
                    RestAssured.registerParser("text/html", Parser.JSON);
                    response = sendRequest(type, number, FORMAT_JSON);
                });

        Then("the status code should be {int}",
                (Integer statusCode) -> {
                    response.then().statusCode(statusCode);
                });

        And("result field in json should be {string}",
                (String expectedStatus) -> {
                    response.then().body(JSON_FIELD_STATUS, is(expectedStatus));
                });

        And("number of elements in json should equal {int}",
                (Integer expectedNumber) -> {
                    Assert.assertEquals((Integer) countItemsNumber(response.jsonPath().getString(JSON_FIELD_TEXT), type, FORMAT_JSON),
                            expectedNumber, getInvalidAmountMessage(type));
                });


        When("send GET request, specifying two parameters, in order to get html",
                () -> {
                    response = sendRequest(type, number, FORMAT_HTML);
                });

        And("number of elements in html should equal {int}",
                (Integer expectedNumber) -> {
                    Assert.assertEquals((Integer) countItemsNumber(response.then().extract().asString(), type, FORMAT_HTML),
                            expectedNumber, getInvalidAmountMessage(type));
                });

        And("json error code should equal {int}",
                (Integer errorCode) -> {
                    response.then().body(JSON_FIELD_ERROR_CODE, is(errorCode));
                });

        And("json error text should equal {string}",
                (String errorText) -> {
                    response.then().body(JSON_FIELD_TEXT, is(errorText));
                });

        And("html text should equal {string}",
                (String errorText) -> {
                    Assert.assertEquals(response.then().extract().asString(), errorText);
                });
    }

}
