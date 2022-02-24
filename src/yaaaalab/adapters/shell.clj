(ns yaaaalab.adapters.shell)

(def source "shell")

(defn reply-to-command
  [text]
  (println (str source "/bot> " text)))

(defn ->chat
  [text]
  {:text text
   :user "user"
   :source source
   :response-dispatcher (var reply-to-command)})

(defn initialize
  {:adapter? true
   :moniker :shell}
  [evaluators]
  (while true
    (print (str source "/user> "))
    (flush)
    (let [evaluate-message-for-command (:command-handler evaluators)]
      (evaluate-message-for-command (->chat (read-line))))))
