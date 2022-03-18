(ns yaaaalab.commands.help
  (:require [yaaaalab.command :refer [->command-descriptions-by-group]]))

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{:keys [channel user]
    reply :message-responder
    send :message-sender
    emit :event-emitter
    render :view-renderer :as message}]
  (send channel (str "Hi, " user "!"))
  (reply (render :help {:items (->command-descriptions-by-group)}))
  (emit :on-command message))
