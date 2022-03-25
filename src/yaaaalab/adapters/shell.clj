(ns yaaaalab.adapters.shell)

(def source "shell")
(def channel "default")

(defn send-message
  [channel text]
  (when text
    (println (str source "/" channel "/bot> " text))))

(def reply-to-message (partial send-message channel))

(defn ->base-message
  [{:keys [event-emitter view-renderer] :as _message-handlers}]
  {:user "user"
   :source source
   :channel channel
   :message-responder (var reply-to-message)
   :message-sender (var send-message)
   :event-emitter event-emitter
   :view-renderer view-renderer})

(defn initialize
  {:adapter? true
   :id :shell}
  [{evaluate-message :message-evaluator :as message-handlers}]
  (let [base-message (->base-message message-handlers)]
    (while true
      (print (str source "/" channel "/user> "))
      (flush)
      (evaluate-message (merge base-message {:text (read-line)})))))
