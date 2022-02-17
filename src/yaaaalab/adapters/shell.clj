(ns yaaaalab.adapters.shell)

(defn ->chat
  [text]
  {:text text
   :user "shell"
   :source "shell"})

(defn adapter
  [evaluate-message]
  (while true
    (print "=> ")
    (flush)
    (let [response (:response (evaluate-message (->chat (read-line))))]
      (if response
        (println (str ">> response: " response \newline))
        (println)))))
