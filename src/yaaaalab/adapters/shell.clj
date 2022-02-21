(ns yaaaalab.adapters.shell)

(defn ->chat
  [text]
  {:text text
   :user "user"
   :source "shell"})

(defn send-message
  [text]
  (println (str " bot> " text \newline)))

(defn initialize
  {:adapter? true
   :moniker :shell}
  [evaluators]
  (while true
    (print "user> ")
    (flush)
    (let [evaluate-message-for-command (:command-handler evaluators)
          response (:response (evaluate-message-for-command (->chat (read-line))))]
      (if response
        (println (str " bot> " response \newline))
        (println)))))
