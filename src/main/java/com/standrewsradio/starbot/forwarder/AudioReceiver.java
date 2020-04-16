package com.standrewsradio.starbot.forwarder;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * A class for receiving audio from a voice channel.
 */
public class AudioReceiver implements AudioReceiveHandler {
    private final FFMPEG ffmpeg;

    private final static Logger logger = (Logger) LoggerFactory.getLogger(AudioReceiver.class);

    public AudioReceiver(Arguments arguments) throws IOException {
        this.ffmpeg = new FFMPEG(arguments);
    }

    @Override
    public boolean canReceiveCombined() {
        return true;
    }

    @Override
    public void handleCombinedAudio(@Nonnull CombinedAudio combinedAudio) {
        try {
            ffmpeg.giveAudio(combinedAudio.getAudioData(1f));
        } catch (IOException e) {
            logger.error("An error occurred whilst piping data to ffmpeg.", e);
        }
    }
}
