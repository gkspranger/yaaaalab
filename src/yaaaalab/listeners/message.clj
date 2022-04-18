(ns yaaaalab.listeners.message
  (:require [taoensso.timbre :refer [debug]]))

(defn non-empty-message
  {:yaaaalab.listener.listener? true
   :yaaaalab.listener.id :yaaaalab.listener.non-empty-message
   :yaaaalab.listener.pattern #"(.+)"}
  [message]
  (debug (str "message received: " message)))
