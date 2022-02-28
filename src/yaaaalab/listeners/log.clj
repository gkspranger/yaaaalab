(ns yaaaalab.listeners.log
  (:require [yaaaalab.config :refer [get-config]]
            [clojure.tools.logging :refer [info]]))

(defn log-command
  {:listener? true
   :pattern (re-pattern (str "^" (:prefix (get-config)) "(.*)"))}
  [{match :match}]
  (info (str "command invoked: " (first match))))
