(ns yaaaalab.core
  (:require [yaaaalab.command :as command]
            [yaaaalab.config :as config]
            [clojure.string :as string]
            [yaaaalab.adapters.shell :as shell])
  (:gen-class))

(defn default-response
  [{:keys [text user]}]
  (str "I'm sorry " user ", I don't understand the message:" \newline
       text))

(defn compute-response
  [message {:keys [command match] :as _command-pattern-match}]
  (if match
    ((resolve (symbol (:function command))) (assoc message :match match))
    (default-response message)))

(defn command-prefix-pattern
  []
  (re-pattern (str "^" (:prefix config/config))))

(defn ->command-pattern-match
  [message command]
  (let [command-info (command/get-command command)
        command-text (string/replace (:text message) (command-prefix-pattern) "")]
    {:command command-info
     :match (re-find (:pattern command-info) command-text)}))

(defn command-pattern-match?
  [message command]
  (if (:match (->command-pattern-match message command))
    true
    false))

(defn find-first-command-pattern-match
  ([message] (find-first-command-pattern-match message (command/->sorted-command-keys)))
  ([message [command & remaining-commands :as commands]]
   (cond
     (empty? commands) nil
     (command-pattern-match? message command) (->command-pattern-match message command)
     :else (recur message remaining-commands))))

(defn command-message?
  [message]
  (if (re-find (command-prefix-pattern) (:text message))
    true
    false))

(defn evaluate-message
  [message]
  (when (command-message? message)
    (let [match (find-first-command-pattern-match message)
          response (compute-response message match)]
      (assoc message :response response))))

(defn -main
  [& _args]
  (command/add-commands)
  (shell/adapter evaluate-message))

(comment

  (let [_ (command/add-commands)]
    (evaluate-message {:text "!help"}))

  (let [_ (command/add-commands)]
    (evaluate-message {:text "!help me"}))
  
  (let [_ (command/add-commands)]
    (evaluate-message {:text "!i will fail"}))
  
  (let [_ (command/add-commands)]
    (evaluate-message {:text "some random text"}))
  

  (let [_ (command/add-commands)]
    (find-first-command-pattern-match {:message "i will fail"}))
  
  (let [_ (command/add-commands)]
    (find-first-command-pattern-match {:message "help me"}))
  
  )

