(ns yaaaalab.test.commands.example)

(defn example-command-exception
  "example command exception - throws an exception from a command"
  {:command? true
   :group :example
   :pattern #"^example\s+command\s+exception$"}
  [_message]
  (/ 1 0))

(defn example-event-exception
  "example event exception - throws an exception from an event"
  {:command? true
   :group :example
   :pattern #"^example\sevent\s+exception$"}
  [{emit :event-emitter :as message}]
  (emit :example-event-exception
        (select-keys message [:channel :match :source :text :user])))

(defn example-view-exception
  "example view exception - throws an exception from a view"
  {:command? true
   :group :example
   :pattern #"^example\sview\s+exception$"}
  [{reply :message-responder
    render :view-renderer :as message}]
  (reply (render :example-exception
                 (select-keys message [:channel :match :source :text :user]))))

(defn example-view-unknown
  "example view unknown - calls unknown view"
  {:command? true
   :group :example
   :pattern #"^example\sview\s+unknown$"}
  [{reply :message-responder
    render :view-renderer :as message}]
  (reply (render :example-unkown-view
                 (select-keys message [:channel :match :source :text :user]))))
