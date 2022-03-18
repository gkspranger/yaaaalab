(ns yaaaalab.listeners.message
  (:require [taoensso.timbre :refer [debug]]))

(defn log-message
  {:listener? true
   :pattern #"(.*)"}
  [{match :match}]
  (when-not (empty? (first match))
    (debug (str "message received: " (first match)))))
