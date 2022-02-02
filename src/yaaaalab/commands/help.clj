(ns yaaaalab.commands.help)

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{:keys [match] :as _chat}]
  (str "help: " match))

(defn help-find
  "help * - list all available commands that match a pattern"
  {:command? true
   :group :help
   :pattern #"^help\s(.+)$"}
  [{:keys [match] :as _chat}]
  (str "help *: " match))
