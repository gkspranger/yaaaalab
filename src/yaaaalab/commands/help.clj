(ns yaaaalab.commands.help
  (:require [yaaaalab.command :refer [->commands]]))

(defn ->command-descriptions-by-group
  [commands]
  (reduce #(assoc %1 (:group %2) (sort (conj ((:group %2) %1)
                                             (:description %2))))
          {}
          commands))

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{reply :response-dispatcher :as _message}]
  (reply (str "help: " (->command-descriptions-by-group (->commands)))))

(defn help-search
  "help * - list all available commands that match the supplied pattern"
  {:command? true
   :group :help
   :pattern #"^help\s(.+)$"}
  [{match :match
    reply :response-dispatcher :as _message}]
  (reply (str "help *: " match)))
