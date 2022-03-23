(ns yaaaalab.events.listener
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-listener
  {:event? true
   :id :known-listener}
  [message]
  (debug (str "known listener invoked: "
              (select-keys message [:channel :match :source :text :user]))))

(defn on-listener-exception
  {:event? true
   :id :listener-exception}
  [{:keys [exception message] :as _listener-message-exception}]
  (error (str "listener exception thrown: "
              (select-keys message [:channel :match :source :text :user])))
  (error exception))
