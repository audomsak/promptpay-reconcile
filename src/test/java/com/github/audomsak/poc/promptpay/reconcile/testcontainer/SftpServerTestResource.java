package com.github.audomsak.poc.promptpay.reconcile.testcontainer;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;
import java.util.Map;

@JBossLog
public class SftpServerTestResource implements QuarkusTestResourceLifecycleManager {
    private GenericContainer<?> sftpServerContainer;
    private static final String DOCKER_IMAGE = "atmoz/sftp:latest";
    private static final int EXPOSED_PORT = 22;
    private static final String USER_NAME = "user123";
    private static final String PASSWORD = "pass123";
    private static final String INPUT_PATH = "promptpay/reconcile/input";
    private static String host;
    private static int port;

    @Override
    @SuppressWarnings("resource")
    public Map<String, String> start() {

        sftpServerContainer = new GenericContainer<>(DockerImageName.parse(DOCKER_IMAGE))
                .withExposedPorts(EXPOSED_PORT)
                .withCommand(StringUtils.join(USER_NAME, ":", PASSWORD, ":::", INPUT_PATH));

        sftpServerContainer.start();
        host = sftpServerContainer.getHost();
        port = sftpServerContainer.getMappedPort(EXPOSED_PORT);

        return Map.of("ftp.server.host", sftpServerContainer.getHost(),
                "ftp.server.port", sftpServerContainer.getMappedPort(EXPOSED_PORT).toString());
    }

    @Override
    public void stop() {
        if (sftpServerContainer != null) {
            sftpServerContainer.stop();
        }
    }

    public static void uploadInputFile(InputStream data, String filename) {
        Session session = null;
        ChannelSftp channel = null;
        String filePath = StringUtils.join(INPUT_PATH, "/", filename);

        try {
            session = connect();
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            channel.put(data, filePath);
            channel.exit();

            log.info("File was uploaded successfully");
        } catch (JSchException | SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private static Session connect() throws JSchException {
        Session session = new JSch().getSession(USER_NAME, host, port);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        log.info("Successfully connected to SFTP server.");

        return session;
    }
}
