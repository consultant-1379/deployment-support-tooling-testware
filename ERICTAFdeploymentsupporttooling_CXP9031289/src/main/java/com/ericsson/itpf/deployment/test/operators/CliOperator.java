package com.ericsson.itpf.deployment.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

@Operator(context = Context.CLI)
public class CliOperator {

    private String getCommand(String commandRef) {
        String command = (String) DataHandler.getAttribute(commandRef);
        return command;
    }

    public String execute(String commandRef) {
        Host apihost = DataHandler.getHostByName("gateway");
        CLICommandHelper cmdHelper = new CLICommandHelper(apihost);
        String command = getCommand(commandRef);
        String result = cmdHelper.simpleExec(command);
        return result;
    }

}
