(ns yaaaalab.adapters.shell)

(defn ->chat
  [text]
  {:text text
   :user "shell"
   :source "shell"})

(defn initialize
  {:adapter? true
   :moniker :shell}
  [evaluators]
  (while true
    (print "=> ")
    (flush)
    (let [evaluate-message-for-command (:command-handler evaluators)
          response (:response (evaluate-message-for-command (->chat (read-line))))]
      (if response
        (println (str ">> " response \newline))
        (println)))))
