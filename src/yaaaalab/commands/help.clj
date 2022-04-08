(ns yaaaalab.commands.help
  (:require [yaaaalab.config :refer [->config]]
            [yaaaalab.command :refer [->commands]]))

(defn ->command-descriptions-by-group
  []
  (reduce #(let [group-name (name (:group %2))
                 command-description (str (:yaaaalab.bot.prefix (->config))
                                          (:description %2))]
             (assoc %1 group-name (sort (conj (%1 group-name)
                                              command-description))))
          {}
          (->commands)))

(defn help
  "help - list all available commands"
  {:command? true
   :group :help
   :pattern #"^help$"}
  [{:keys [channel user]
    reply :message-responder
    send :message-sender
    render :view-renderer :as _message}]
  (send channel (str "Hi, " user "!"))
  (reply (render :help {:items (->command-descriptions-by-group)})))
