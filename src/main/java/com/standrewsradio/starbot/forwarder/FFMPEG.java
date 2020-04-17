package com.standrewsradio.starbot.forwarder;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a process-backed ffmpeg instance.
 */
public class FFMPEG {
    private final Process process;

    /**
     * Creates a new process-backed instance of ffmpeg.
     * @param arguments the command line arguments
     * @throws IOException if an error occurred whilst starting the process
     */
    public FFMPEG(Arguments arguments) throws IOException {
        List<String> command = new ArrayList<>();
        Collections.addAll(command, "ffmpeg", "-hide_banner", "-re", "-f", "s16be", "-ac", "2",
                "-ar", "48000", "-i", "pipe:0", "-ar", String.valueOf(arguments.sampleRate),
                "-ac", String.valueOf(arguments.compressionLevel), "-c:a", "libmp3lame", "-f", "mp3");

        if (arguments.outputGroup.icecastUrl != null) {
            Collections.addAll(command, "-reconnect_at_eof", "1", "-reconnect_streamed", "1",
                    "-reconnect", "1", "-reconnect_delay_max", "1000", "-content_type", "audio/mpeg",
                    arguments.outputGroup.icecastUrl);
        } else if (arguments.outputGroup.path != null) {
            command.add(arguments.outputGroup.path.toString());
        } else {
            throw new IllegalArgumentException("An output location was not specified.");
        }

        this.process = new ProcessBuilder()
                .command(command.toArray(new String[0]))
                .redirectError(Redirect.INHERIT)
                .redirectOutput(arguments.redirectFfmpegOutput ? Redirect.INHERIT : Redirect.DISCARD)
                .redirectInput(Redirect.PIPE)
                .start();
    }

    /**
     * Gives some audio to the ffmpeg process.
     * @param audioData the audio to give
     */
    public void giveAudio(byte[] audioData) throws IOException {
        this.process.getOutputStream().write(audioData);
    }
}
