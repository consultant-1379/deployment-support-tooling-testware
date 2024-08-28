/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.itpf.deployment.test.cases;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.itpf.deployment.test.operators.CliOperator;

public class CLI_Generate extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliOperator> TafProvider;

    /**
     * @DESCRIPTION Test Command Line Simple GAV
     * @PRIORITY MEDIUM
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param cmdGavFlag
     *            - the command that is executed
     * @param expectedOutput
     *            - Part of the expected output from executed command
     */
    @TestId(id = "TORF-24031_GAV_Flag_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Generate_Positive")
    public void verify_Cli_GAV_Flags(@Input("id") String testCaseId,
            @Input("title") String testTitle,
            @Input("cmdGavFlag") String cmdGavFlag,
            @Output("expectedOutput") String expectedOutput) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(cmdGavFlag);
        assertTrue(result.contains(expectedOutput));
    }

    /**
     * @DESCRIPTION Test Command Line Generate Negative Test
     * @PRIORITY MEDIUM
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param cmdGavFlag
     *            - the command that is executed
     * @param expectedOutput
     *            - Part of the expected output from executed command
     */
    @TestId(id = "TORF-24031_Negative_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Generate_Negative")
    public void check_Cli_Negative_Scenarios(@Input("id") String testCaseId,
            @Input("title") String testTitle,
            @Input("cmdIncorrectFlag") String cmdIncorrectFlag,
            @Output("expectedOutput") String expectedOutput) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(cmdIncorrectFlag);
        assertTrue(result.contains(expectedOutput));
    }

}
