(ns yaaaalab.config
  (:require [omniconf.core :as cfg]))

(def configs (atom {}))

(defn load-config
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
  (reset! configs {:adapter (keyword (cfg/get :yaaaalab-adapter))
                   :include-examples (cfg/get :yaaaalab-include-examples)
                   :log-level (keyword (cfg/get :yaaaalab-log-level))
                   :prefix (cfg/get :yaaaalab-prefix)}))

(defn ->config
  []
  @configs)
