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

public class CLI_Help extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliOperator> TafProvider;

    /**
     * @DESCRIPTION Test Command Line Help Flags
     * @PRIORITY MEDIUM
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param cmdHelpFlag
     *            - the command that is executed
     * @param expectedUsageFormat
     *            - Part of the expected output from executed command
     */
    @TestId(id = "TORF-24031_Help_Flag_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Help_Flags")
    public void verify_Cli_Help_Flags(@Input("id") String testCaseId,
            @Input("title") String testTitle,
            @Input("cmdHelpFlag") String cmdHelpFlag,
            @Output("expectedUsageFormat") String expectedUsageFormat) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(cmdHelpFlag);
        setTestInfo(String.format("CLI command result: %s", result));
        setTestInfo(String.format(
                "The expected result expectedUsageFormat: %s",
                expectedUsageFormat));
        assertTrue(result.contains(expectedUsageFormat));
    }

}
