package com.hazeluff.discord.canucksbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazeluff.discord.canucksbot.nhl.NHLGameScheduler;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;


public class CanucksBot {
	private static final Logger LOGGER = LoggerFactory.getLogger(CanucksBot.class);

	private final IDiscordClient client;
	private final NHLGameScheduler nhlGameScheduler;
	private final String id;
	public CanucksBot(String botToken) {
		LOGGER.info("Running CanucksBot v" + Config.VERSION);
		client = getClient(botToken);
		nhlGameScheduler = new NHLGameScheduler(client);
		try {
			id = client.getApplicationClientID();
			LOGGER.info("CanucksBot. id [" + id + "]");
		} catch (DiscordException e) {
			LOGGER.error("Failed to get Application Client ID", e);
			throw new RuntimeException(e);
		}
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new ReadyListener(this));
		dispatcher.registerListener(new CommandListener(this));
	}

	private static IDiscordClient getClient(String token) {
		ClientBuilder clientBuilder = new ClientBuilder();
		clientBuilder.withToken(token);
		try {
			return clientBuilder.login();
		} catch (DiscordException e) {
			LOGGER.error("Could not login.", e);
			return null;
		}
	}

	public IDiscordClient getClient() {
		return client;
	}

	public NHLGameScheduler getNhlGameScheduler() {
		return nhlGameScheduler;
	}

	public String getId() {
		return id;
	}
}
