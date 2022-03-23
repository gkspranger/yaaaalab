(ns yaaaalab.events.command
  (:require [taoensso.timbre :refer [debug error]]))

(defn on-known-command
  {:event? true
   :id :known-command}
  [message]
  (debug (str "known command invoked: "
              (select-keys message [:channel :match :source :text :user]))))

(defn on-unknown-command
  {:event? true
   :id :unknown-command}
  [message]
  (debug (str "unknown command invoked: "
              (select-keys message [:channel :source :text :user]))))

(defn on-command-exception
  {:event? true
   :id :command-exception}
  [{:keys [exception message] :as _command-message-exception}]
  (error (str "command exception thrown: "
              (select-keys message [:channel :match :source :text :user])))
  (error exception))
