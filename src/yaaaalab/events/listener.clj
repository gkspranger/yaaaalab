(ns yaaaalab.events.listener
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-listener
  {:event? true
   :id :known-listener}
  [{text :text :as _message}]
  (debug (str "known listener invoked: " text)))

(defn on-listener-exception
  {:event? true
   :id :listener-exception}
  [{{text :text} :message
    exception :exception :as _listener-message-exception}]
  (error (str "listener exception thrown: " text))
  (error exception))
