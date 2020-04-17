package com.standrewsradio.starbot.forwarder;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.EnumSet;

/**
 * The main class for the forwarder bot.
 */
public class Forwarder {
    private final Arguments arguments;
    private final JDA discord;

    private final static Logger logger = (Logger) LoggerFactory.getLogger(Forwarder.class);

    /**
     * Creates a new instance of the forwarder.
     * @param arguments the arguments
     */
    public Forwarder(Arguments arguments) throws LoginException, InterruptedException, IOException {
        // setup variables
        this.arguments = arguments;
        this.discord = new JDABuilder()
                .setToken(arguments.token)
                .setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS))
                .setChunkingFilter(ChunkingFilter.NONE)
                .setGuildSubscriptionsEnabled(false)
                .setActivity(Activity.listening("to you."))
                .build();

        // wait until we're ready
        this.discord.awaitReady();

        // join the voice channel
        VoiceChannel channel = this.discord.getVoiceChannelById(arguments.channelId);

        if (channel == null) {
            logger.error("The channel specified by the --channel-id was not a voice channel!");
            return;
        }

        AudioManager audioManager = channel.getGuild().getAudioManager();
        audioManager.setSelfMuted(true);
        audioManager.setReceivingHandler(new AudioReceiver(arguments));
        audioManager.openAudioConnection(channel);

        logger.info("The audio channel has been joined.");
    }

    public void close() {
        discord.shutdownNow();
    }
}
