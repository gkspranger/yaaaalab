(ns yaaaalab.events.command
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-command
  {:event? true
   :id :known-command}
  [{text :text :as _message}]
  (debug (str "known command invoked: " text)))

(defn on-unknown-command
  {:event? true
   :id :unknown-command}
  [{text :text :as _message}]
  (debug (str "unknown command invoked: " text)))

(defn on-command-exception
  {:event? true
   :id :command-exception}
  [{{text :text} :message
    exception :exception :as _command-message-exception}]
  (error (str "command exception thrown: " text))
  (error exception))
