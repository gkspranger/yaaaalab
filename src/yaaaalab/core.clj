(ns yaaaalab.core
  (:require [yaaaalab.command :as command])
  (:gen-class))

(defn default-response
  [{:keys [message user]}]
  (str "I'm sorry " user ", I don't understand the message:" \newline
       message))

(defn find-command-pattern
  [chat command]
  (when command
    (re-find (:pattern (command/fetch command)) (:message chat))))

(defn compute-response
  [chat command]
  (if command
    (let [match (find-command-pattern chat command)
          command-function (:function (command/fetch command))
          command-symbol (symbol command-function)]
      ((resolve command-symbol) (assoc chat :match match)))
    (default-response chat)))

(defn evaluate-chat
  [chat]
  (->> (command/sorted-keys)
       (filter #(find-command-pattern chat %))
       (first)
       (compute-response chat)
       (assoc chat :response)))

(defn -main
  [& _args]
  nil)

(comment

  (let [_ (command/add-commands)]
    (evaluate-chat {:message "help"}))

  (let [_ (command/add-commands)]
    (evaluate-chat {:message "help me"}))
  
  (let [_ (command/add-commands)]
    (evaluate-chat {:message "i will fail"}))
  
  )

