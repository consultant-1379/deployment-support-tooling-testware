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
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.itpf.deployment.test.operators.CliOperator;

public class CLI_Fixed extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliOperator> TafProvider;

    /**
     * @DESCRIPTION Verifies that a fixed deployment xml file can be generated
     *              and stored in a user specified directory on the local
     *              machine. This test tests that the correct console log is
     *              displayed and that the physical file exists on the machine.
     *              At end of the test the file is removed from the machine to
     *              tidy up.
     * 
     * @PRIORITY MEDIUM
     * 
     * @param testCaseId
     *            The id of the test within this class
     * 
     * @param testTitle
     *            The title for the test
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param command
     *            Command to generate the fixed deployment file and to save it
     *            on the machine
     * @param fileName
     *            The name of the fixed deployment file
     * 
     * @param directoryPath
     *            The specified local directory where the fixed deployment file
     *            will be stored
     * 
     * @param expectedLog
     *            The expected command line log message
     * 
     */
    @TestId(id = "CLI_Fixed_Generate_Fixed_Deployment_Positive_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Fixed_Positive")
    public void verify_CLI_Fixed_Positive(@Input("id") String testCaseId,
            @Input("title") String testTitle, @Input("command") String command,
            @Input("fileName") String fileName,
            @Input("directoryPath") String directoryPath,
            @Output("expectedLog") String expectedLog) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(command);

        RemoteFileHandler remote = new RemoteFileHandler(
                DataHandler.getHostByName("gateway"));
        boolean fileExists = remote.remoteFileExists(directoryPath + "/"
                + fileName);
        assertTrue(fileExists);
        assertTrue(result.contains(expectedLog));

        // deletes the directory and file to clean up
        remote.deleteRemoteFile(directoryPath);
    }

    /**
     * @DESCRIPTION Verifies that the 'fixed' command error in user input is
     *              handled and that meaningful error message are printed to the
     *              console.
     * 
     * @PRIORITY MEDIUM
     * 
     * @param testCaseId
     *            The id of the test within this class
     * 
     * @param testTitle
     *            The title for the test
     * @param host
     *            - IP address of server (e.g. ms1)
     * @param command
     *            Command to generate the fixed deployment file and to save it
     *            on the machine
     * @param expectedLog
     *            The expected command line log message
     * 
     */
    @TestId(id = "CLI_Fixed_Generate_Fixed_Deployment_Negative_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "CLI_Fixed_Negative")
    public void verify_CLI_Fixed_Negative(@Input("id") String testCaseId,
            @Input("title") String testTitle, @Input("command") String command,
            @Output("expectedLog") String expectedLog) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);
        String result = cliOperator.execute(command);

        assertTrue(result.contains(expectedLog));
    }

}
