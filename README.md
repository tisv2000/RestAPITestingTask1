#Test task for REST API automation testing

##The task

Create a project for API service (http://fish-text.ru/api) automation testing and upload it on GitHub or BitBucket

1. Develop test cases (as complete as you see fit)
2. Crete regression testing test plan
3. Automate a test plan:
* the project should be written in Java (version 8+)
* the project should be built on maven
* all tests should be framed as .feature files (Cucumber framework)
* tests should be divided into groups in order to have an ability to run them separately

Preferences:
* All API actions have to be performed through one java class
* Amount of code repetition has to be minimal


##Regression Testing Test Plan

###Application Under Test
REST API of the site http://fish-text.ru/api
####Description
>Fish text is used by designers, planners, and front-end developers when there is a need to quickly fill out mock-ups or prototypes with content. This is a test content that should not make any sense, just show the presence of the text itself or demonstrate typography in action.
>
>With the help of this online fish-text generator, you can produce both individual sentences and entire paragraphs of an excellent fish text. And for fans of automation there is an implementation of the Fishtext API.

API allows:
* Request text as a set of sentences, paragraphs or headings (determined by the parameter `type`).
* Set the number of sentences / paragraphs / headings (parameter `number`).
* Choose the format in which the text will be returned (JSON or HTML) (parameter `format`).

###Application Under Test Analysis
During the analysis, the following features were identified, which are essentially bugs:

* When set `format=json`, in response we get a header `Content-Type: text/html`, and not `application/json`.
* When set `format=html`, response body does not contain a root tag, which does not allow RestAssured to parse it normally, which, in turn, deprives us of the ability to use XPath.
* Allowed values for `number` are in range of 1-100 and 1-500. However a null value is acceptable, in this case the default value is used. 
* Incorrect values (not digits) for `number` are acceptable, in this case the default value is used.
* When specify a negative value for `number` when `format=html`, the behavior of the application is different from the behavior for other formats. We will consider it as a bug, therefore **at least one test will be failed**.
* Incorrect values for `type` and `format` are acceptable, in this case the default value is used.

These features will be considered later in the test plan.

###Test Design Techniques Used
* Equivalence class testing
* Boundary testing
* Test matrix
* DDT

###Testing features
All possible combinations of query parameters, given in [description](http://fish-text.ru/api) should be tested. Including situations when any of the parameters is not specified.

At the same time, for the parameter `number` it is necessary to take into account the boundary conditions and equivalence classes, of which there are several: Acceptable values: 1-100 for paragraphs and 1-500 for sentences and headings.
* Zero value: in theory, this is also not a valid value, but in this case the default value is used
    (1 for headings and 3 for sentences and paragraphs).
* Negative values (invalid values).
* Non-numeric values (invalid values).

For positive test cases it is necessary to check:
* Status Code from service response
* Field `status` in JSON, gotten from server (only for `format=json`)
* Correspondence of the number of received sentences/paragraphs/headings to the requested value of `number`

For negative test cases it is necessary to check:
* Status Code from service response
* Field `status` in JSON, gotten from server (only for `format=json`)
* Response error message

###Test cases
A complete list of query parameters for all tests is given in the [matrix](https://drive.google.com/open?id=1eIT7GlUStORdLDSV015jx6Fp1GPl9zFslxRn7TZSzhQ).

Each cell is marked with an identifier (a number starting with the '#' character) is a separate test case.

In the cells of the table, in addition to the number, auxiliary information on the test case is indicated.

Thus, as a result of a combination of query parameters, 66 test cases are obtained.

In addition to those ones, indicated in the table, the following test cases are also required:

* \#67: Parameter `type` is omitted. Default value should be used (`sentence`).
* \#68: Parameter `format`is omitted. Default value should be used (`json`).
* \#69: Not acceptable value is specified for parameter `type`. There should be an error message, but instead, the default value is used.
* \#70: Invalid value specified for `format`. There should be an error message, but the default value is used instead.
* \#71: Invalid value specified for parameters `type` and `format` at the same time. There should be an error message, but the default value is used instead.
* \#72: Parameter `type` is specified twice.
* \#73: Parameter `format` is specified twice.
* \#74: Parameter `number` is specified twice.
* \#75: Check limits on the number of requests per minute.
* \#76: Specify not a GET request, but a POST.
 

Total number of test cases: 76.

###Automation
Based on the considerations given in the section *“Application Under Test Analysis”*,  test cases #7, #14, #21, #28, #35, #42, #45, #46, #49, #50, #53, #54, #57, #58, #61, #62, #65, #66 and from #67 to #76 will not be automated.

Total: 48 test cases will be automated, and 28 will not be.
For this task, manual test cases will not be detailed.

Given the use of DDT, for 48 test cases it is necessary to implement a total of 4 test scenarios:
* Positive JSON
* Positive HTML
* Negative JSON
* Negative HTML

Automation will be done in Java 8 using RestAssured and Cucumber.

###Features of test case automation
I took the liberty of expanding the task and implementing not one, but four sets of test scenarios:
* without Cucumber - package `withoutcucumber`.
* with Cucumber - package `withcucumber`.

And with Cucumber, I decided to make three options:
* using annotations (on branch `master`)
* using cucumber-java8 (on branch `cucumber-java8`)
* using cucumber expressions (on branch `cucumber-java8-expr`)

The result is: four sets of test scripts (one without Cucumber and three with Cucumber).

Each set implements four test scenarios. And each set performs 48 test cases.

To be able to run test scripts separately (only for sets with Cucumber) they will be marked with the following tags:
* @Positive - all positive tests
* @Negative - all negative tests
* @FormatJson - negative and positive tests for JSON
* @FormatHtml - negative and positive tests for HTML

##Test result
Test result is given [here](https://drive.google.com/open?id=1Q9YU6vkdHI_v6E7NIISs7ENZJKq6N2rHy4v6yG4xIz8).

In total: passed: 47, failed: 1

##Getting project

        cd <your_projects_directory>
        git clone https://bitbucket.org/tisv2000/cardpaytesttask.git CardPayTestTask
        cd CardPayTestTask

##Running autotests

The following options for running autotests are possible:

* Run all tests (with and without Cucumber). 96 tests will be executed, two will fall.

        mvn test

* Run all tests (without Cucumber). 48 tests will be executed, one will fall.

        mvn test -Dtest=FishTextTest

* Run all tests with Cucumber (hereinafter, one of the three branches can be selected, as indicated above). 48 will be executed, one will fail.

        mvn test -Dtest=CucumberStarterTest

* Run only positive tests (with Cucumber). 36 tests will be executed.

        mvn test -Dtest=CucumberStarterTest -Dcucumber.options="--tags @Positive"

* Run only negative tests (with Cucumber). 12 tests will be executed, one will fail.

        mvn test -Dtest=CucumberStarterTest -Dcucumber.options="--tags @Negative" 

* Run tests for JSON only (with Cucumber). 24 tests will be executed.

        mvn test -Dtest=CucumberStarterTest -Dcucumber.options="--tags @FormatJson" 

* Run tests for HTML only (with Cucumber). 24 tests will be executed, one will fail.

        mvn test -Dtest=CucumberStarterTest -Dcucumber.options="--tags @FormatHtml" 