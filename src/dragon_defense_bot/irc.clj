(ns dragon-defense-bot.irc
  "IRC library wrappers and patches"
  (:require [irclj.core :as irc]
            [irclj.process :as process]
            [irclj.events :as events]
            [irclj.connection :as connection]))

(def server "irc.chat.twitch.tv")
(def port 6667)
(def channel "#archonthewizard")

(def callbacks (atom {}))

; Extension to process-line multimethod to handle Twitch specific WHISPER message
(defmethod process/process-line "WHISPER"
  [{:keys [params] :as m} i]
  ; copypaste from PRIVMSG handler from oritinal library but pushing
  ; events to a separate channel
  (let [[target text] params
       m (assoc m :target target, :text text)]
    (events/fire i :whisper m)))

(defn whisper
  "Sends Twitch specific whisper message."
  [conn target text]
  (irc/message conn target (str "/w " target " " text)))

(defn connected-callback
  "Callback for 001 a.k.a. IRC connection accepted event."
  [conn args]
  (connection/write-irc-line conn "CAP REQ :twitch.tv/commands")
  (println (println "Connected and authenticated.")))

(defn joined-callback
  "Callback for JOIN IRC event."
  [conn args]
  (println "Joined channel" channel))

(defn message-callback
  "Callback for PRIVMSG IRC event."
  [conn args]
  (let [{text :text
         nick :nick} args]
    ((@callbacks :message) conn nick text)))

(defn whisper-callback
  "Callback for Twitch specific WHISPER IRC event."
  [conn args]
  (let [{text :text
         nick :nick} args]
    ((@callbacks :whisper) conn nick text)))

(defn connect
  "Wraps passed parameters into connection call."
  [nickname password external-message-callback external-whisper-callback]
  (swap! callbacks merge {:message external-message-callback
                          :whisper external-whisper-callback})
  (println "Connecting to" (str server ":" port))
  (irc/connect server
               port
               nickname
               :pass password
               :callbacks {:001     connected-callback
                           :join    joined-callback
                           :privmsg message-callback
                           :whisper whisper-callback}))

(defn join-channel
  "Joins IRC channel."
  [connection]
  (irc/join connection channel))
