(ns yaaaalab.listeners.log
  (:require [yaaaalab.config :refer [->config]]
            [clojure.tools.logging :refer [info]]))

(defn log-command
  {:listener? true
   :pattern (re-pattern (str "^" (:prefix (->config)) "(.*)"))}
  [{match :match}]
  (info (str "command invoked: " (first match))))
