package com.tngtech.jgiven.report.html;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import com.tngtech.jgiven.GivenTestStep;
import com.tngtech.jgiven.ThenTestStep;
import com.tngtech.jgiven.WhenTestStep;
import com.tngtech.jgiven.base.ScenarioTestBase;
import com.tngtech.jgiven.report.model.ReportModel;

@RunWith( DataProviderRunner.class )
public class HtmlWriterTest extends ScenarioTestBase<GivenTestStep, WhenTestStep, ThenTestStep> {

    @DataProvider
    public static Object[][] testData() {
        return new Object[][] {
            { 5, 6, 30 },
            { 2, 2, 4 },
            { -5, 1, -5 },
        };
    }

    @Test
    @UseDataProvider( "testData" )
    public void HTML_report_is_correctly_generated_for_scenarios( int a, int b, int expectedResult ) throws UnsupportedEncodingException {
        scenario.startScenario( "values can be multiplied" );

        given().$d_and_$d( a, b );
        when().both_values_are_multiplied_with_each_other();
        then().the_result_is( expectedResult );

        scenario.finished();
        ReportModel model = scenario.getModel();
        String string = HtmlWriter.toString( model.getLastScenarioModel() );
        assertThat( string.replace( '\n', ' ' ) ).matches( ".*"
                + "<h3>Values can be multiplied</h3>.*"
                + "<li><span class='introWord'>Given</span> <span class='argument '>" + a + "</span>.*and.*" + b + ".*</li>.*"
                + "<li><span class='introWord'>When</span> both values are multiplied with each other.*</li>.*"
                + "<li><span class='introWord'>Then</span> the result is.*<span class='argument '>" + expectedResult + "</span>.*</li>.*"
                + "<div class='topRight passed'>Passed</div>"
                + ".*" );
    }

    @DataProvider
    public static Object[][] testArguments() {
        return new Object[][] {
            { "a", "b" },
        };
    }

    @Test
    @UseDataProvider( "testArguments" )
    public void tests_with_arguments_generate_cases( String paramA, String paramB ) throws Exception {
        scenario.getExecutor().startScenario(
            HtmlWriterTest.class.getMethod( "tests_with_arguments_generate_cases", String.class, String.class ),
            Arrays.asList( paramA, paramB ) );

        when().both_values_are_multiplied_with_each_other();

        scenario.finished();
        ReportModel model = scenario.getModel();
        String string = HtmlWriter.toString( model.getLastScenarioModel() );
        assertThat( string.replace( '\n', ' ' ) ).matches( ".*<h4>Case 1: paramA = a, paramB = b</h4>.*" );
    }
}