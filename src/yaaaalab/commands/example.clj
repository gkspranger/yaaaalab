(ns yaaaalab.commands.example)

(defn example
  "example - does nothing, just an example command"
  {:command? true
   :group :help
   :pattern #"^example$"}
  [{match :match
    reply :response-dispatcher
    send :message-dispatcher :as _message}]
  (reply (str "I can reply to a command (`"
              match
              "`) in the same channel it was invoked."))
  (send "some-other-channel"
        "I can also send messages to other channels."))
