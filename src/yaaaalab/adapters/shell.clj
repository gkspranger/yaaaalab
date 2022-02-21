(ns yaaaalab.adapters.shell)

(defn send-message
  [text]
  (println (str " bot> " text)))

(defn ->chat
  [text]
  {:text text
   :user "user"
   :message-dispatcher (var send-message)})

(defn initialize
  {:adapter? true
   :moniker :shell}
  [evaluators]
  (while true
    (print "user> ")
    (flush)
    (let [evaluate-message-for-command (:command-handler evaluators)]
      (evaluate-message-for-command (->chat (read-line))))))