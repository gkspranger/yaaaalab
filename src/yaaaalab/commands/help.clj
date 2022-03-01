(ns yaaaalab.commands.help
  (:require [yaaaalab.command :refer [->command-descriptions-by-group]]))

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{reply :response-dispatcher :as _message}]
  (reply (str "help: " (->command-descriptions-by-group))))

(defn help-search
  "help * - list all available commands that match the supplied pattern"
  {:command? true
   :group :help
   :pattern #"^help\s(.+)$"}
  [{match :match
    reply :response-dispatcher :as _message}]
  (reply (str "help *: " match)))
