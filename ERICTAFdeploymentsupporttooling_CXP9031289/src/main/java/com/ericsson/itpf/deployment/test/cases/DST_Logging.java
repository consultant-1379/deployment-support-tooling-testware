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

public class DST_Logging extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliOperator> TafProvider;

    /**
     * @DESCRIPTION Verifies that CE input appears in the dst.log file. It will
     *              first remove the existing log file, retrieve the
     *              cloud-fixed-deployment POM file from nexus and during this
     *              retrieval the entire POM content will be outputted to
     *              dst.log.
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
     * @param removeDSTLog
     *            Command to remove the log file
     * @param fetchFromNexusFixedDepl
     *            This will fetch the cloud-fixed-deployment from Nexus
     * 
     * @param resolvedExpected
     *            Test that the following statement exist: Artifact
     *            'com.ericsson.oss.itpf.deployment.tools:cloud-fixed-deployment
     *            : 1 . 0 . 3 7 ' pom file resolved.
     * 
     * @param grepItemTypeName
     *            it will grep for "itemType name"
     * 
     * @param fdceXMLSnippet
     *            Checks that the following snippet from the
     *            cloud-fixed-deployment pom exist in the log. <itemType
     *            name="ms-items-collection">
     */
    @TestId(id = "TORF-23897_FixedDeployment_Printed_To_Log_Test")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "DST_Logging")
    public void verify_Cloud_FixedDeploymentCe_in_Log(
            @Input("id") String testCaseId,
            @Input("title") String testTitle,
            @Input("removeDSTLogCmd") String removeDSTLogCmd,
            @Input("fetchFromNexusFixedDeplCmd") String fetchFromNexusFixedDeplCmd,
            @Output("resolvedExpected") String resolvedExpected,
            @Input("grepItemTypeNameCmd") String grepItemTypeNameCmd,
            @Output("expectedXMLSnippet") String fdceXMLSnippet) {
        setTestcase(getTestId() + "-" + testCaseId, "-" + testTitle);
        CliOperator cliOperator = TafProvider.provide(CliOperator.class);

        // remove the log file
        cliOperator.execute(removeDSTLogCmd);

        // fetch the fixed deployment from nexus
        String result = cliOperator.execute(fetchFromNexusFixedDeplCmd);
        assertTrue(result.contains(resolvedExpected));

        // need to check if CE input exist in the log file. We will do this by
        // checking that the following entry from the cloud-fixed-deployment POM
        // exist in the dst.log file
        // <itemType name="ms-items-collection">
        result = cliOperator.execute(grepItemTypeNameCmd);
        assertTrue(result.contains(fdceXMLSnippet));
    }

}
