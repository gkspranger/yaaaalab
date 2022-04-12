(ns yaaaalab.commands.help
  (:require [yaaaalab.config :refer [->config]]
            [yaaaalab.command :refer [->commands]]))

(defn ->command-descriptions-by-group
  []
  (reduce #(let [group-name (name (:yaaaalab.command.group %2))
                 command-description (str (:yaaaalab.bot.prefix (->config))
                                          (:yaaaalab.command.description %2))]
             (assoc %1 group-name (sort (conj (%1 group-name)
                                              command-description))))
          {}
          (->commands)))

(defn help
  "help - list all available commands"
  {:yaaaalab.command.command? true
   :yaaaalab.command.group :help
   :yaaaalab.command.id :yaaaalab.command.help
   :yaaaalab.command.pattern #"^help$"}
  [{:keys [channel user]
    reply :message-responder
    send :message-sender
    render :view-renderer :as _message}]
  (send channel (str "Hi, " user "!"))
  (reply (render :yaaaalab.view.help {:items (->command-descriptions-by-group)})))
