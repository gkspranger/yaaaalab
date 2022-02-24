(ns yaaaalab.core
  (:require [yaaaalab.command :refer [->sorted-command-keys get-command
                                      load-commands]]
            [yaaaalab.adapter :refer [get-adapter load-adapters]]
            [yaaaalab.listener :refer [->listener-vals load-listeners]]
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

(defn ->listener-pattern-match
  [message listener]
  {:listener listener
   :match (re-find (:pattern listener) (:text message))})

(defn listener-pattern-match?
  [message listener]
  (if (:match (->listener-pattern-match message listener))
    true
    false))

(defn find-first-command-pattern-match
  ([message] (find-first-command-pattern-match message (->sorted-command-keys)))
  ([message [command & remaining-commands :as commands]]
   (cond
     (empty? commands) nil
     (command-pattern-match? message command) (->command-pattern-match message command)
     :else (recur message remaining-commands))))

(defn find-listener-pattern-matches
  [message]
  (filter #(listener-pattern-match? message %) (->listener-vals)))

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

(defn evaluate-message-for-listener
  [message]
  (find-listener-pattern-matches message))

(defn -main
  [& _args]
  (load-adapters)
  (load-commands)
  (load-listeners)
  (as-> (:adapter (get-config)) v
    (get-adapter v)
    (:function v)
    (v {:command-handler evaluate-message-for-command
        :listener-handler evaluate-message-for-listener})))

(comment

  (do (load-commands)
      (evaluate-message-for-command {:text "!help"}))
  
  (do (load-commands)
      (evaluate-message-for-command {:text "!help me"}))
  
  (do (load-commands)
      (evaluate-message-for-command {:text "!i will fail"}))
  
  (do (load-commands)
      (evaluate-message-for-command {:text "some random text"}))
  
  (do (load-listeners)
      (evaluate-message-for-listener {:text "some random text"}))
  
  (->listener-vals)
  
  
  
  
  
  )

