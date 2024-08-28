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

public class CLI_Version extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliOperator> TafProvider;

    /**
     * @DESCRIPTION Test Command Line Version Flag. This version the version of
     *              the DST.
     * @PRIORITY MEDIUM
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param cmdVersionFlag
     *            - the command that is executed
     * @param expectedApplication
     *            - Part of the expected output from executed command (the
     *            Application Name)
     * @param expectedVersion
     *            - Part of the expected output from executed command (the
     *            Version of the Jar File)
     */
    @TestId(id = "TORF-24031_Version_Flag_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Version")
    public void verify_Cli_Version(@Input("id") String testCaseId,
            @Input("title") String testTitle,
            @Input("cmdVersionFlag") String cmdVersionFlag,
            @Output("expectedApplication") String expectedApplication,
            @Output("containsVersionString") String containsVersionString) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(cmdVersionFlag);
        assertTrue(result.contains(expectedApplication));
        assertTrue(result.contains(containsVersionString));
    }

}
