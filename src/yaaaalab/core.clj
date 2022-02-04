(ns yaaaalab.core
  (:require [yaaaalab.command :as command]
            [yaaaalab.config :as config]
            [clojure.string :as string]
            [yaaaalab.adapters.shell :as shell])
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
     :match (re-find (:pattern command-info) (:command-message chat))}))

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
  (some->> (find-first-command-pattern-match chat)
           (compute-response chat)
           (assoc chat :response)))

(defn command-message?
  [chat]
  (if (re-find (re-pattern (str "^" (:prefix config/config))) (:raw-message chat))
    true
    false))

(defmulti evaluate-chat2 :next)
(defmethod evaluate-chat2 :compute-and-attach-response
  [chat]
  (->> (find-first-command-pattern-match chat)
       (compute-response chat)
       (assoc chat :response)))
(defmethod evaluate-chat2 :default
  [chat]
  (let [prefix-pattern (re-pattern (str "^" (:prefix config/config)))
        command-message? (if (re-find prefix-pattern (:raw-message chat))
                           true
                           false)]
    (when command-message?
      (evaluate-chat2 (assoc chat
                             :command-message (string/replace (:raw-message chat)
                                                              prefix-pattern
                                                              "")
                             :next :compute-and-attach-response)))))

(defn -main
  [& _args]
  (command/add-commands)
  (shell/adapter evaluate-chat2))

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

