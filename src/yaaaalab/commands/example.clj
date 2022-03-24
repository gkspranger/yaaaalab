(ns yaaaalab.commands.example)

(defn example-command-exception
  "example command exception - throws an exception from a command"
  {:command? true
   :example? true
   :group :example
   :pattern #"^example\s+command\s+exception$"}
  [_message]
  (/ 1 0))

(defn example-event-exception
  "example event exception - throws an exception from an event"
  {:command? true
   :example? true
   :group :example
   :pattern #"^example\sevent\s+exception$"}
  [{emit :event-emitter :as _message}]
  (emit :example-event-exception nil))