# starbot-forwarder
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/StAndrewsRadio/starbot-forwarder?sort=semver&style=flat-square)
![GitHub](https://img.shields.io/github/license/StAndrewsRadio/starbot-forwarder?style=flat-square)

A simple Discord bot that forwards audio from a voice channel to an Icecast stream.

## Requirements
The program requires the following software:
- A Discord bot;
- Java 11 (or higher); and
- FFMPEG.

## Installation
The programs runs as a standalone Java jar file.
To download this file, see the [latest release](https://github.com/StAndrewsRadio/starbot-forwarder/releases/latest).

## Usage
The simplest usage of the program is to act as a forwarder to an Icecast stream. 
This can be done as easily as:
```shell
java -jar starbot-forwarder.jar --token "MY-TOKEN-HERE" --channel-id 4815162342 https://icecast.example.com/stream
```

The token is used to log into the Discord bot. This can be obtained from Discord's [Developer Portal](https://discord.com/developers/docs/intro). A guide on how to obtain the channel ID can be found [here](https://support.discord.com/hc/en-us/articles/206346498-Where-can-I-find-my-User-Server-Message-ID-). Finally, the Icecast stream is provided.

For full usage instructions, run the following:
```shell
java -jar starbot-forwarder.jar --help
```
