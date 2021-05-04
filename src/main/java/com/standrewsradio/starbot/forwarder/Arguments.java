package com.standrewsradio.starbot.forwarder;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;

/**
 * Command line arguments for the program.
 */
@Command(versionProvider = com.standrewsradio.starbot.forwarder.VersionProvider.class)
public class Arguments {
    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Displays this help message.")
    private boolean helpRequested;

    @Option(names = {"--version"}, versionHelp = true, description = "Displays the version information.")
    private boolean versionRequested;

    @Option(names = {"-t", "--token"}, required = true, description = "The token for the Discord bot.")
    String token;

    @Option(names = {"-v"}, description = "Specify multiple -v options to increase the verbosity of the logger.")
    boolean[] verbosity;

    @Option(names = {"-c", "--channel-id"}, required = true, description = "The channel ID for the voice channel to" +
            " forward from.")
    long channelId;

    @Option(names = {"-s", "--single-channel"}, description = "If ffmpeg should process everything as a single" +
            " audio channel.")
    boolean singleChannel;

    @Option(names = {"-r", "--sample-rate"}, description = "The sample rate to use for the output.  " +
            "Default value: ${DEFAULT-VALUE}.")
    int sampleRate = 48000;

    @Option(names = {"-x", "--compression-level"}, description = "The level of audio compression to use for" +
            " the output. Default value: ${DEFAULT-VALUE}.")
    int compressionLevel = 0;

    @Option(names = {"-d", "--redirect-ffmpeg-output"}, description = "If ffmpeg's stdout should be printed.")
    boolean redirectFfmpegOutput;

    @Option(names = {"-l", "--listening-to"}, description = "The activity string following \"Listening to \". " +
            "Default value: ${DEFAULT-VALUE}.")
    String listeningTo = "you.";

    @ArgGroup(heading = "Output Location", multiplicity = "1")
    OutputGroup outputGroup;

    public static class OutputGroup {

        @Parameters(index = "0", description = "The full Icecast URL to send the audio to.")
        String icecastUrl;

        @Option(names = {"-f", "--file-output"}, description = "The file to output the audio to.")
        Path path;
    }
}
