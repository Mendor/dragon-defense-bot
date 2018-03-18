# dragon-defense-bot

Dumb auto-follow bot for [Stream Defense](http://www.streamdefense.com/) game.
Example is run on [main and only [Twitch channel](https://www.twitch.tv/archonthewizard)
under nickname **here_be_dragon** where its author plays as **archydragon**.

## Build and run

Requires [Leiningen](https://leiningen.org/).

Before running copy `config-sample.yml` with any suitable name, e.g. just `config.yml`.
Oauth key can be generated at [twitchapps.com](https://twitchapps.com/tmi/).
Other configuration options are obvious enough.

Just build and run:

    # inside of cloned application directory
    lein run config.yml

Produce standalone JAR which can be run on pure JVM without Clojure:

    # inside of cloned application directory
    lein uberjar
    java -jar ./target/uberjar/dragon-defense-bot-0.1.0-standalone.jar config.yml

## Usage

* `%link` switches bot to active state in which it handles other commands;
* `%unlink` turns off all reactions;
* when in the linked state, all commands aside of class and map selection and `!gold`
  are copied as is to GM bot whisper;
* to forbid "copying" any of whitelisted commands add a dash in the end (e.g. `!1 -`
  if you don't want the bot to follow you);
* to execute command on bot only add `%` in the beginning (e.g. `%!t` will move
  bot to training).

All whispers received by bot (e.g. response to `!stats` private command) are
forwarded to owner's inbox.

## License

Copyright © 2018 Nikita K.

Distributed under the Eclipse Public License either version 1.0 or (at your option)
any later version.
