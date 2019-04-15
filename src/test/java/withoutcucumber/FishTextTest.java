package withoutcucumber;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static common.FishTextTestUtils.*;
import static org.hamcrest.CoreMatchers.is;

public class FishTextTest {

    @Test(dataProvider = "positiveTestDataProvider")
    public void positiveJsonTest(String type, int number, int expectedNumber) {
        RestAssured.registerParser("text/html", Parser.JSON);

        String text = sendRequest(type, number, FORMAT_JSON)
                .then()
                .statusCode(HTTP_STATUS_CODE_200)
                .body(JSON_FIELD_STATUS, is(JSON_STATUS_SUCCESS))
                .extract()
                .jsonPath()
                .getString("text");

        Assert.assertEquals(countItemsNumber(text, type, FORMAT_JSON), expectedNumber, getInvalidAmountMessage(type));
    }

    @Test(dataProvider = "positiveTestDataProvider")
    public void positiveHtmlTest(String type, int number, int expectedNumber) {
        String text = sendRequest(type, number, FORMAT_HTML)
                .then()
                .statusCode(HTTP_STATUS_CODE_200)
                .extract()
                .asString();

        Assert.assertEquals(countItemsNumber(text, type, FORMAT_HTML), expectedNumber, getInvalidAmountMessage(type));
    }

    @DataProvider
    private Object[][] positiveTestDataProvider() {
        return new Object[][]{
                {TYPE_SENTENCE, 1, 1},
                {TYPE_SENTENCE, 500, 500},
                {TYPE_SENTENCE, 0, 3},
                {TYPE_PARAGRAPH, 1, 1},
                {TYPE_PARAGRAPH, 100, 100},
                {TYPE_PARAGRAPH, 0, 3},
                {TYPE_TITLE, 1, 1},
                {TYPE_TITLE, 500, 500},
                {TYPE_TITLE, 0, 1}
        };
    }


    @Test(dataProvider = "negativeJsonTestDataProvider")
    public void negativeJsonTest(String type, int number, int expectedErrorCode, String expectedErrorText) {
        RestAssured.registerParser("text/html", Parser.JSON);

        sendRequest(type, number, FORMAT_JSON)
                .then()
                .statusCode(HTTP_STATUS_CODE_200)
                .body(JSON_FIELD_STATUS, is(JSON_STATUS_ERROR))
                .body(JSON_FIELD_ERROR_CODE, is(expectedErrorCode))
                .body(JSON_FIELD_TEXT, is(expectedErrorText));
    }

    @DataProvider
    private Object[][] negativeJsonTestDataProvider() {
        return new Object[][]{
                {TYPE_SENTENCE, -1, 31, ERROR_TEXT_2},
                {TYPE_SENTENCE, 501, 11, ERROR_TEXT_1},
                {TYPE_PARAGRAPH, -1, 31, ERROR_TEXT_2},
                {TYPE_PARAGRAPH, 101, 11, ERROR_TEXT_1},
                {TYPE_TITLE, -1, 31, ERROR_TEXT_2},
                {TYPE_TITLE, 501, 11, ERROR_TEXT_1},
        };
    }


    @Test(dataProvider = "negativeHtmlTestDataProvider")
    public void negativeHtmlTest(String type, int number, int expectedStatusCode, String expectedErrorText) {
        String actualErrorText = sendRequest(type, number, FORMAT_HTML)
                .then()
                .statusCode(expectedStatusCode)
                .extract()
                .asString();

        Assert.assertEquals(actualErrorText, expectedErrorText, "Invalid error text");
    }

    @DataProvider
    private Object[][] negativeHtmlTestDataProvider() {
        return new Object[][]{
                {TYPE_SENTENCE, -1, HTTP_STATUS_CODE_500, ERROR_TEXT_2},
                {TYPE_SENTENCE, 501, HTTP_STATUS_CODE_200, ERROR_TEXT_1},
                {TYPE_PARAGRAPH, -1, HTTP_STATUS_CODE_500, ERROR_TEXT_2},
                {TYPE_PARAGRAPH, 101, HTTP_STATUS_CODE_200, ERROR_TEXT_1},
                {TYPE_TITLE, -1, HTTP_STATUS_CODE_500, ERROR_TEXT_2},
                {TYPE_TITLE, 501, HTTP_STATUS_CODE_200, ERROR_TEXT_1},
        };
    }

}
