(ns yaaaalab.listeners.message
  (:require [taoensso.timbre :refer [debug]]))

(defn non-empty-message
  {:listener? true
   :pattern #"(.+)"}
  [message]
  (debug (str "message received: " message)))
