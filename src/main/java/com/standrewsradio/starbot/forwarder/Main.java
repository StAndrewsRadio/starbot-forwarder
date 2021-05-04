package com.standrewsradio.starbot.forwarder;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import picocli.CommandLine.ParameterException;

public class Main {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class),
            rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public static void main(String[] args) {
        // parse command line arguments
        Arguments arguments = new Arguments();
        CommandLine commandLine = new CommandLine(arguments);

        try {
            commandLine.parseArgs(args);
        } catch (ParameterException e) {
            logger.error("An error occurred whilst parsing the command line arguments. " + e.getLocalizedMessage());
            System.out.println();
            commandLine.usage(System.out);
            return;
        }

        // set the logger level
        if (arguments.verbosity != null) {
            switch (arguments.verbosity.length) {
                case 0:
                    break;
                case 1:
                    rootLogger.setLevel(Level.DEBUG);
                    break;
                case 2:
                    rootLogger.setLevel(Level.TRACE);
                    break;
                default:
                    rootLogger.setLevel(Level.ALL);
                    break;
            }
        }

        // check for help or version before loading the bot
        if (commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
        } else if (commandLine.isVersionHelpRequested()) {
            commandLine.printVersionHelp(System.out);
        } else {
            try {
                Forwarder forwarder  = new Forwarder(arguments);

                logger.info("Forwarder started. Press CTRL-C to shutdown.");

                // Add a shutdown handler for cleaner exiting
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    logger.info("Shutting down...");
                    forwarder.close();
                }));
            } catch (LoginException e) {
                logger.error("An error occurred whilst logging into Discord.", e);
            } catch (InterruptedException e) {
                logger.error("The bot was interrupted whilst waiting for the ready state.", e);
            } catch (IOException e) {
                logger.error("An I/O error occurred whilst opening the ffmpeg process.", e);
            } catch (Exception e) {
                logger.error("An unknown error occurred.", e);
            }
        }
    }
}
