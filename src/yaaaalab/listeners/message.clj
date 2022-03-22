(ns yaaaalab.listeners.message
  (:require [taoensso.timbre :refer [debug]]))

(defn log-message
  {:listener? true
   :pattern #"(.*)"}
  [{match :match :as _message}]
  (when-not (empty? (first match))
    (debug (str "message received: " (first match)))))
