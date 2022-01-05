(ns yaaaalab.core
  (:gen-class))

(def commands {"world" {:pattern #"world"
                        :response (fn [_matches]
                                    "world hello")}
               "hello\\s+(\\w+)" {:pattern #"hello\s+(\w+)"
                                  :response (fn [{:keys [message user channel]} [_match word & _rest]]
                                              (str "you are " user
                                                   "you said " message
                                                   "which contains " word
                                                   "in the channel " channel))}})

(def chat1 {:message "hello world and everyone, i hope you are doing well"
           :user "gkspranger"
           :channel "general"
           :metadata {}})

(def chat2 {:message "this will not match on anything"
            :user "gkspranger"
            :channel "general"
            :metadata {}})

(defn evaluate-chat-command
  [chat command]
  (when command
    (re-find (:pattern (get commands command)) (:message chat))))

(defn default-response
  [{:keys [message user]}]
  (str "i'm sorry " user ", i don't understand the message: " message))

(defn compute-response
  [chat command]
  (if command
    (let [matches (evaluate-chat-command chat command)
          respond-with (:response (get commands command))]
      (respond-with chat matches))
    (default-response chat)))

(defn dispatch-chat
  [chat]
  (->> (sort (keys commands))
       (filter #(evaluate-chat-command chat %))
       (first)
       (compute-response chat)
       (assoc chat :response)))

(defn -main
  [& _args]
  (println "hello world"))

(comment
  
  (sort (map str (keys commands)))

  (get commands "hello")

  (dispatch-chat chat1)
  (dispatch-chat chat2)
  
  )

