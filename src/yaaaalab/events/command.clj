(ns yaaaalab.events.command
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-command
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.known-command}
  [message]
  (debug (str "known command invoked: " message)))

(defn on-unknown-command
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.unknown-command}
  [message]
  (debug (str "unknown command invoked: " message)))

(defn on-command-exception
  {:yaaaalab.event.event? true
   :yaaaalab.event.id :yaaaalab.event.command-exception}
  [{:keys [exception message] :as _command-message-exception}]
  (error (str "command exception thrown: " message))
  (error exception))
