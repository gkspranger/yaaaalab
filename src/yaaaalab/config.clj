(ns yaaaalab.config
  (:require [omniconf.core :as cfg]))

(def configs (atom {}))

(defn load-configs
  []
  (cfg/define {:yaaaalab-adapter {:type :string
                                  :default "shell"}
               :yaaaalab-include-examples {:type :boolean
                                           :default false}
               :yaaaalab-log-level {:type :string
                                    :default "info"}
               :yaaaalab-prefix {:type :string
                                 :default "!"}})
  (cfg/populate-from-env)
  (cfg/verify)
  (reset! configs {:yaaaalab.adapter.id (keyword (cfg/get :yaaaalab-adapter))
                   :yaaaalab.examples.include? (cfg/get :yaaaalab-include-examples)
                   :yaaaalab.logging.level (keyword (cfg/get :yaaaalab-log-level))
                   :yaaaalab.bot.prefix (cfg/get :yaaaalab-prefix)}))

(defn ->config
  []
  @configs)
