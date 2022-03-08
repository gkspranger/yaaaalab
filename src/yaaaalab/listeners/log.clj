(ns yaaaalab.listeners.log
  (:require [yaaaalab.config :refer [->config]]
            [taoensso.timbre :refer [debug info]]))

(defn log-command
  {:listener? true
   :pattern (re-pattern (str "^" (:prefix (->config)) "(.*)"))}
  [{match :match}]
  (info (str "command invoked: " (first match))))

(defn log-message
  {:listener? true
   :pattern #"(.*)"}
  [{match :match}]
  (debug (str "message received: " (first match))))