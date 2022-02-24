(ns yaaaalab.core
  (:require [yaaaalab.command :refer [->sorted-command-keys get-command
                                      load-commands]]
            [yaaaalab.adapter :refer [get-adapter load-adapters]]
            [yaaaalab.config :refer [get-config]]
            [clojure.string :as cs])
  (:gen-class))

(defn ->default-command-response
  [{:keys [user text]}]
  (str "I'm sorry " user ", I don't understand the command: `" text "`"))

(defn dispatch-command
  [{reply :response-dispatcher :as message}
   {:keys [match]
    {apply-command :function} :command :as _command-pattern-match}]
  (if match
    (apply-command (assoc message :match match))
    (reply (->default-command-response message))))

(def command-prefix-pattern (re-pattern (str "^" (:prefix (get-config)))))

(defn ->command-pattern-match
  [message command]
  (let [command-info (get-command command)
        command-text (cs/replace (:text message) command-prefix-pattern "")]
    {:command command-info
     :match (re-find (:pattern command-info) command-text)}))

(defn command-pattern-match?
  [message command]
  (if (:match (->command-pattern-match message command))
    true
    false))

(defn find-first-command-pattern-match
  ([message] (find-first-command-pattern-match message (->sorted-command-keys)))
  ([message [command & remaining-commands :as commands]]
   (cond
     (empty? commands) nil
     (command-pattern-match? message command) (->command-pattern-match message command)
     :else (recur message remaining-commands))))

(defn command-message?
  [message]
  (if (re-find command-prefix-pattern (:text message))
    true
    false))

(defn evaluate-message-for-command
  [message]
  (when (command-message? message)
    (dispatch-command message
                      (find-first-command-pattern-match message))))

(defn -main
  [& _args]
  (load-adapters)
  (load-commands)
  (as-> (:adapter (get-config)) v
    (get-adapter v)
    (:function v)
    (v {:command-handler evaluate-message-for-command})))

(comment

  (let [_ (load-commands)]
    (evaluate-message-for-command {:text "!help"}))

  (let [_ (load-commands)]
    (evaluate-message-for-command {:text "!help me"}))
  
  (let [_ (load-commands)]
    (evaluate-message-for-command {:text "!i will fail"}))
  
  (let [_ (load-commands)]
    (evaluate-message-for-command {:text "some random text"}))
  
  (let [_ (load-commands)]
    (find-first-command-pattern-match {:message "i will fail"}))
  
  (let [_ (load-commands)]
    (find-first-command-pattern-match {:message "help me"}))
  
  )

