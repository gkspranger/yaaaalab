(ns yaaaalab.commands.help)

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{match :match
    reply :response-dispatcher :as _message}]
  (reply (str "help: " match)))

(defn help-find
  "help * - list all available commands that match a pattern"
  {:command? true
   :group :help
   :pattern #"^help\s(.+)$"}
  [{match :match
    reply :response-dispatcher :as _message}]
  (reply (str "help *: " match)))
