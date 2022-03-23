(ns yaaaalab.logging
  (:require [yaaaalab.config :refer [->config]]
            [taoensso.timbre :refer [default-config set-config!]]))

(defn load-logging
  []
  (let [log-level (:log-level (->config))
        log-config (assoc default-config :min-level log-level)]
    (set-config! log-config)))