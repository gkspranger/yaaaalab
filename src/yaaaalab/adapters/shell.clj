(ns yaaaalab.adapters.shell)

(defn ->chat
  [text]
  {:raw-message text
   :user "shell"})

(defn adapter
  [evaluate-chat]
  (while true
    (print "=> ")
    (flush)
    (let [response (:response (evaluate-chat (->chat (read-line))))]
      (println (str ">> response: " response \newline)))))
