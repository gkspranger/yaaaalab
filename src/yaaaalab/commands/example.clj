(ns yaaaalab.commands.example)

(defn example
  "example - does nothing, just an example command"
  {:command? true
   :group :example
   :pattern #"^example$"}
  [{match :match
    reply :response-dispatcher
    send :message-dispatcher :as _message}]
  (reply (str "I can reply to a command (`"
              match
              "`) in the same channel it was invoked."))
  (send "some-other-channel"
        "I can also send messages to other channels."))

(defn example2-1
  "example2 - ..."
  {:command? true
   :group :example
   :pattern #"^example2$"}
  [{reply :response-dispatcher :as _message}]
  (reply (str "example2-1")))

(defn example2-2
  "example2 - ..."
  {:command? true
   :group :example
   :pattern #"^example2$"}
  [{reply :response-dispatcher :as _message}]
  (reply (str "example2-2")))