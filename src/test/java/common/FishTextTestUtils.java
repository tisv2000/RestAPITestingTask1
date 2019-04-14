package common;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class FishTextTestUtils {

    public static final String BASE_ENDPOINT = "http://fish-text.ru/get";
    public static final int HTTP_STATUS_CODE_200 = 200;
    public static final int HTTP_STATUS_CODE_500 = 500;
    public static final String PARAMETER_TYPE = "type";
    public static final String PARAMETER_NUMBER = "number";
    public static final String PARAMETER_FORMAT = "format";
    public static final String FORMAT_JSON = "json";
    public static final String FORMAT_HTML = "html";
    public static final String TYPE_SENTENCE = "sentence";
    public static final String TYPE_PARAGRAPH = "paragraph";
    public static final String TYPE_TITLE = "title";
    public static final String ERROR_TEXT_1 = "You requested too much content. Be more moderate.";
    public static final String ERROR_TEXT_2 = "Unknown error. Contact the administration.";
    public static final String JSON_FIELD_STATUS = "status";
    public static final String JSON_STATUS_SUCCESS = "success";
    public static final String JSON_STATUS_ERROR = "error";
    public static final String JSON_FIELD_ERROR_CODE = "errorCode";
    public static final String JSON_FIELD_TEXT = "text";


    public static Response sendRequest(String type, int number, String format) {
        return given()
                .queryParam(PARAMETER_TYPE, type)
                .queryParam(PARAMETER_NUMBER, number)
                .queryParam(PARAMETER_FORMAT, format)
                .get(BASE_ENDPOINT);
    }


    public static String getInvalidAmountMessage(String type) {
        return "Invalid amount of '" + type + "':";
    }


    public static int countItemsNumber(String responseTest, String type, String format) {
        String[] tokens = responseTest.split(getDelimiter(type, format));
        if (type.equals(TYPE_SENTENCE) && format.equals(FORMAT_HTML)) {
            return tokens.length - 1;
        } else {
            return tokens.length;
        }
    }

    public static String getDelimiter(String type, String format) {
        if (type.equals(TYPE_SENTENCE)) {
            return "[.?!]";
        } else {
            if (format.equals(FORMAT_JSON)) {
                return "\\\\n\\\\n";
            } else {
                if (type.equals(TYPE_PARAGRAPH)) {
                    return "</p>";
                } else { // TYPE_TITLE
                    return "</h1>";
                }
            }
        }
    }

}
