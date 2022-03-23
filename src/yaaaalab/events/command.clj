(ns yaaaalab.events.command
  (:require [taoensso.timbre :refer [debug]]))

(defn on-known-command
  {:event? true
   :id :known-command}
  [{text :text :as _message}]
  (debug (str "command invoked: " text)))

(defn on-unknown-command
  {:event? true
   :id :unknown-command}
  [{text :text :as _message}]
  (debug (str "unknown command invoked: " text)))
