(ns dragon-defense-bot.core
  "Main application namespace."
  (:require [dragon-defense-bot.config :refer :all]
            [dragon-defense-bot.irc :as irc]
            [clojure.string :as string]))

(def gm-nick "ttdbot")

(def blacklist
  "Combined regular expression for commands which should never be 'copied'."
  (re-pattern (string/join "|" (flatten
    ["!map.+"
     "!archer" "!rogue" "!firemage" "!frostmage" "!alchemist" "!bard"]))))

(def state (atom {:link false}))

(defn whisper-to-gm
  "Whispers command to GM bot if link is active."
  [conn text]
  ; if this command is a dedicated one for bot, need to cut % from the beginning
  (let [valid-command (last (re-matches #"%?(!.+)" text))]
    (when (@state :link)
      (do
        (irc/whisper conn gm-nick valid-command)
        (println valid-command)))))

(defn whisper-to-owner
  "Whispers messages to bot owner"
  [conn text]
  (irc/whisper conn (@config :owner-nick) text))

(defn message-callback
  "Chat messages handler. Reacts only to owner's posts."
  [conn nick text]
    (when (= nick (@config :owner-nick))
      (condp re-matches text
        #"%link"   (do (swap! state assoc :link true) (println "Linked."))
        #"%unlink" (do (swap! state assoc :link false) (println "Unlinked."))
        #"!.*\-$"  nil ; if the command is trailed with dash, don't try to "mirror" it
        blacklist  nil ; same for the commands from blacklist
        #"!.*"     (whisper-to-gm conn text) ; all other comands and their chains
        #"%!.+"    (whisper-to-gm conn text) ; are passed as is
        nil)))

(defn whisper-callback
  "Whisper messages handler. Just proxies them to owner."
  [conn nick text]
    (whisper-to-owner conn text))

(defn -main
  "Main application entrypoint"
  [& args]
  (load-config (first *command-line-args*))

  (def conn (irc/connect (@config :bot-nick)
                         (@config :oauth-key)
                         message-callback
                         whisper-callback))

  (irc/join-channel conn))
