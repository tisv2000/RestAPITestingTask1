@FishTextTestApi
Feature: API testing of fish-text.ru
  Users should be able to submit GET requests to Fish-text server
  with some certain settings (return text type and amount of the text) and get expecting result

  @Positive
  @FormatJson
  Scenario Outline: Positive Fish JSON test
    Given two parameters <type> and <number>
    When send GET request, specifying two parameters, in order to get json
    Then the status code should be 200
    And result field in json should be 'success'
    And number of elements in json should equal <expectedNumber>

    Examples:
      | type     | number | expectedNumber|
      | sentence |   0    |       3       |
      | sentence |   1    |       1       |
      | sentence |   2    |       2       |
      | sentence |   50   |       50      |
      | sentence |   499  |       499     |
      | sentence |   500  |       500     |
      | paragraph|   0    |       3       |
      | paragraph|   1    |       1       |
      | paragraph|   2    |       2       |
      | paragraph|   50   |       50      |
      | paragraph|   99   |       99      |
      | paragraph|   100  |       100     |
      | title    |   0    |       1       |
      | title    |   1    |       1       |
      | title    |   2    |       2       |
      | title    |   50   |       50      |
      | title    |   499  |       499     |
      | title    |   500  |       500     |

  @Positive
  @FormatHtml
  Scenario Outline: Positive Fish HTML test
    Given two parameters <type> and <number>
    When send GET request, specifying two parameters, in order to get html
    Then the status code should be 200
    And number of elements in html should equal <expectedNumber>

    Examples:
      | type     | number | expectedNumber|
      | sentence |   0    |       3       |
      | sentence |   1    |       1       |
      | sentence |   2    |       2       |
      | sentence |   50   |       50      |
      | sentence |   499  |       499     |
      | sentence |   500  |       500     |
      | paragraph|   0    |       3       |
      | paragraph|   1    |       1       |
      | paragraph|   2    |       2       |
      | paragraph|   50   |       50      |
      | paragraph|   99   |       99      |
      | paragraph|   100  |       100     |
      | title    |   0    |       1       |
      | title    |   1    |       1       |
      | title    |   2    |       2       |
      | title    |   50   |       50      |
      | title    |   499  |       499     |
      | title    |   500  |       500     |

  @Negative
  @FormatJson
  Scenario Outline: Negative Fish JSON test
    Given two parameters <type> and <number>
    When send GET request, specifying two parameters, in order to get json
    Then the status code should be 200
    And result field in json should be 'error'
    And json error code should equal <expectedErrorCode>
    And json error text should equal '<expectedErrorText>'

    Examples:
      | type     | number | expectedErrorCode | expectedErrorText                                         |
      | sentence |   -1   |        31         | Unknown error. Contact the administration.        |
      | sentence |   501  |        11         | You requested too much content. Be more moderate. |
      | paragraph|   -1   |        31         | Unknown error. Contact the administration.        |
      | paragraph|   101  |        11         | You requested too much content. Be more moderate. |
      | title    |   -1   |        31         | Unknown error. Contact the administration.        |
      | title    |   501  |        11         | You requested too much content. Be more moderate. |

  @Negative
  @FormatHtml
  Scenario Outline: Negative Fish HTML test
    Given two parameters <type> and <number>
    When send GET request, specifying two parameters, in order to get html
    Then the status code should be <expectedStatusCode>
    And  html text should equal '<expectedErrorText>'

    Examples:
      | type     | number | expectedStatusCode | expectedErrorText                                         |
      | sentence |   -1   |        200         | Unknown error. Contact the administration.        |
      | sentence |   501  |        200         | You requested too much content. Be more moderate. |
      | paragraph|   -1   |        500         | Unknown error. Contact the administration.        |
      | paragraph|   101  |        200         | You requested too much content. Be more moderate. |
      | title    |   -1   |        500         | Unknown error. Contact the administration.        |
      | title    |   501  |        200         | You requested too much content. Be more moderate. |
