(ns yaaaalab.commands.help)

(defn help
  "help - list all available commands"
  {:group :help
   :pattern #"help"}
  [{:keys [match] :as _chat}]
  (str "help: " match))

(defn help-find
  "help * - list all available commands that match a pattern"
  {:group :help
   :pattern #"help\s(.+)"}
  [{:keys [match] :as _chat}]
  (str "help *: " match))
