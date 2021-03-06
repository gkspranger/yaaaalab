(ns yaaaalab.core
  (:require [yaaaalab.adapter :refer [->adapter load-adapters]]
            [yaaaalab.command :refer [load-commands] :as cmd]
            [yaaaalab.event :refer [emit load-events]]
            [yaaaalab.listener :refer [load-listeners] :as lsnr]
            [yaaaalab.view :refer [load-views render]]
            [yaaaalab.config :refer [->config load-configs]]
            [taoensso.timbre :refer [default-config set-config!]])
  (:gen-class))

(def message-handlers
  {:message-evaluator (fn [message]
                        (cmd/evaluate-message-and-apply-matched-commands message)
                        (lsnr/evaluate-message-and-apply-matched-listeners message))
   :event-emitter emit
   :view-renderer render})

(defn set-log-level
  []
  (let [log-level (:yaaaalab.logging.level (->config))
        log-config (assoc default-config :min-level log-level)]
    (set-config! log-config)))

(defn -main
  [& _args]
  (load-configs)
  (set-log-level)
  (load-adapters)
  (load-commands)
  (load-events)
  (load-listeners)
  (load-views)
  (let [adapter (:yaaaalab.adapter.id (->config))
        apply-adapter-function (:function (->adapter adapter))]
    (apply-adapter-function message-handlers)))
