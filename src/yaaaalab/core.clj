(ns yaaaalab.core
  (:require [yaaaalab.command :as command])
  (:gen-class))

(defn default-response
  [{:keys [message user]}]
  (str "I'm sorry " user ", I don't understand the message:" \newline
       message))

(defn compute-response
  [chat {:keys [command match] :as _command-pattern-match}]
  (if match
    (let [command-symbol (symbol (:function command))]
      ((resolve command-symbol) (assoc chat :match match)))
    (default-response chat)))

(defn ->command-pattern-match
  [chat command]
  (let [command-info (command/get-command command)]
    {:command command-info
     :match (re-find (:pattern command-info) (:message chat))}))

(defn command-pattern-match?
  [chat command]
  (if (:match (->command-pattern-match chat command))
    true
    false))

(defn find-first-command-pattern-match
  ([chat] (find-first-command-pattern-match chat (command/->sorted-command-keys)))
  ([chat [command & remaining-commands :as commands]]
   (cond
     (empty? commands) nil
     (command-pattern-match? chat command) (->command-pattern-match chat command)
     :else (recur chat remaining-commands))))

(defn evaluate-chat
  [chat]
  (->> (find-first-command-pattern-match chat)
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
  

  (let [_ (command/add-commands)]
    (find-first-command-pattern-match {:message "i will fail"}))
  
  (let [_ (command/add-commands)]
    (find-first-command-pattern-match {:message "help me"}))
  
  )

