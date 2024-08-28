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

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.*;

public class CLI_DST_LITP_InstallDeployment extends TorTestCaseHelper implements
        TestCase {

    private CLICommandHelper gatewayCmd;
    private CLICommandHelper ms1Cmd;

    private static final String DIRECTORY = "/tmp/dst_deployment";
    private static final String FILE_NAME = "litp_dd_com.ericsson.oss.itpf.deployment.tools-ms-1.0.66.xml";
    private static final String CLEAN_LITP = "/opt/ericsson/enminst/bin/cleanup.sh";
    private static final String GENERATE_DEPLOYMENT = "dst generate -g com.ericsson.oss.itpf.deployment.tools:ms:1.0.66 -cells 100 -users 32220 -o "
            + DIRECTORY;
    private static final String CONFIG_HOSTNAME = "lms219";
    private static final String SHOW_HOSTNAME = "litp show -p /ms -o hostname";
    private static final String LOAD_DEPLOYMENT = "litp load -p / -f "
            + DIRECTORY + "/" + FILE_NAME + " --merge";

    private final Host gateway = DataHandler.getHostByName("gateway");
    private final Host ms1 = DataHandler.getHostByName("ms1");

    @BeforeTest
    public void setup() throws Exception {
        ms1Cmd = new CLICommandHelper(ms1);
        gatewayCmd = new CLICommandHelper(gateway);
        ms1Cmd.simpleExec(CLEAN_LITP);
    }

    /**
     * @DESCRIPTION Test Command Line Generate Deployment Load Into Litp
     * @PRIORITY MEDIUM
     */
    @TestId(id = "TORF-27831_Generate_Deployment_Description")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    public void verify_Generated_Deployment_Loaded_Into_Litp() {
        setTestcase(
                getTestId() + "-"
                        + "TORF-27831_Generate_Deployment_Description",
                "-"
                        + "Verify generated deployment description can be loaded into litp");

        String generateDeploymentResult = gatewayCmd
                .simpleExec(GENERATE_DEPLOYMENT);
        assertTrue(generateDeploymentResult
                .contains("LITP deployment description saved"));

        RemoteFileHandler remoteGW = new RemoteFileHandler(gateway);
        assertTrue(checkFileExists(remoteGW));

        copyFileFromGatewayToMS(FILE_NAME, DIRECTORY);

        RemoteFileHandler remoteMS = new RemoteFileHandler(ms1);
        assertTrue(checkFileExists(remoteMS));

        //Loads the deployment description into litp
        ms1Cmd.simpleExec(LOAD_DEPLOYMENT);

        //Ensure hostname has been changed
        String updatedHostname = ms1Cmd.simpleExec(SHOW_HOSTNAME);
        assertTrue(updatedHostname.contains(CONFIG_HOSTNAME));

        remoteGW.deleteRemoteFile(DIRECTORY);
        remoteMS.deleteRemoteFile(DIRECTORY + "/" + FILE_NAME);

        ms1Cmd.simpleExec(CLEAN_LITP);
    }

    private boolean checkFileExists(RemoteFileHandler remote) {
        return remote.remoteFileExists(DIRECTORY + "/" + FILE_NAME);
    }

    private void copyFileFromGatewayToMS(String fileName, String directoryPath) {
        try {
            Shell shell = gatewayCmd.openShell(Terminal.VT100);
            shell.writeln("scp " + directoryPath + "/" + fileName
                    + " root@ms-1:" + directoryPath);
            shell.expect("root@ms-1's password:");
            shell.writeln("12shroot");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
