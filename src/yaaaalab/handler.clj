(ns yaaaalab.handler
  (:require [yaaaalab.command :refer [filter-matched-commands]]
            [yaaaalab.listener :refer [filter-matched-listeners]]
            [yaaaalab.event :refer [emit]]))

(defn ->handler-events
  [handler-function]
  (let [handler-function-meta (meta handler-function)
        handler-type (cond
                       (:command? handler-function-meta) "command"
                       (:listener? handler-function-meta) "listener")
        known-handler (keyword (str "known-" handler-type))
        handler-exception (keyword (str handler-type "-exception"))]
    {:known-handler known-handler
     :handler-exception handler-exception}))

(defn apply-handler
  [message
   {match :match
    apply-handler-function :function :as _matched-handler}]
  (let [message-w-match (assoc message :match match)
        {:keys [known-handler handler-exception]
         :as _handler-events} (->handler-events apply-handler-function)]
    (try
      (apply-handler-function message-w-match)
      (emit known-handler message)
      (catch Exception exception
        (emit handler-exception {:message message-w-match
                                 :exception exception})))))

(defn evaluate-message-for-commands
  [message]
  (let [matched-commands (filter-matched-commands message)
        unknown-command? (and (coll? matched-commands)
                              (empty? matched-commands))]
    (if unknown-command?
      (emit :unknown-command message)
      (run! #(apply-handler message %) matched-commands))))

(defn evaluate-message-for-listeners
  [message]
  (run! #(apply-handler message %) (filter-matched-listeners message)))
