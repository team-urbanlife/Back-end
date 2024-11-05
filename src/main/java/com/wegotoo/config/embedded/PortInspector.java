package com.wegotoo.config.embedded;

import static io.micrometer.common.util.StringUtils.isEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("portInspector")
@Profile("local")
public class PortInspector {

    private static final int MIN_PORT = 10000;
    private static final int MAX_PORT = 65535;

    public boolean isPortRunning(int port) throws IOException {
        return isRunning(executeGrepProcessCommand(port));
    }

    public int findAvailablePort() throws IOException {
        for (int port = MIN_PORT; port <= MAX_PORT; port++) {
            Process process = executeGrepProcessCommand(port);

            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available Port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) throws IOException {
        String line;
        StringBuilder pid = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pid.append(line);
            }
        } catch (Exception e) {
        }

        return !isEmpty(pid.toString());
    }

}
