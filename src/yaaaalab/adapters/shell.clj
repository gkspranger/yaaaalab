(ns yaaaalab.adapters.shell)

(def source "shell")
(def channel "default")

(defn send-message
  [channel text]
  (println (str source "/" channel "/bot> " text)))

(def reply-to-command (partial send-message channel))

(defn ->chat
  [text]
  {:text text
   :user "user"
   :source source
   :channel channel
   :response-dispatcher (var reply-to-command)
   :message-dispatcher (var send-message)})

(defn initialize
  {:adapter? true
   :moniker :shell}
  [evaluators]
  (while true
    (print (str source "/" channel "/user> "))
    (flush)
    (let [text (->chat (read-line))]
      ((:command-handler evaluators) text)
      ((:listener-handler evaluators) text))))
