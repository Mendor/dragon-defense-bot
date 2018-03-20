(defproject dragon-defense-bot "0.1.1"
  :description "Dumb bot for Stream Defense"
  :url "https://github.com/Mendor/dragon-defense-bot"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [irclj "0.5.0-alpha4"]
                 [io.forward/yaml "1.0.7"]]

  :main ^:skip-aot dragon-defense-bot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
