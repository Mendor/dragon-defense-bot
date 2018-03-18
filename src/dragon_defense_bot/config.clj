(ns dragon-defense-bot.config
  "Application configuration related stuff."
  (:require [yaml.core :as yaml]))

(def config (atom {}))
(def required-keys [:owner-nick :bot-nick :oauth-key])

(def valid-config?
  "Lazy checking all required parameters presence."
  (delay (every? #(contains? @config %) required-keys)))

(defn load-config
  "Read and parse YAML configuration file."
  [filename]
  (println "Using configuration from file" filename)
  (swap! config merge (yaml/from-file filename))
  (if-not @valid-config?
    (do
      (println "One of requred parameters is not set.")
      (System/exit 1))))
