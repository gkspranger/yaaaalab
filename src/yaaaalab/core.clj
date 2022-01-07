(ns yaaaalab.core
  (:gen-class))

(def commands (atom{}))
(defn sorted-command-keys [] (sort (keys @commands)))
(defn get-command [command] (get @commands command))

(defn default-response
  [{:keys [message user]}]
  (str "I'm sorry " user ", I don't understand your message:" \newline
       message))

(defn find-command-pattern
  [chat command]
  (when command
    (re-find (:pattern (get-command command)) (:message chat))))

(defn compute-response
  [chat command]
  (if command
    (let [match (find-command-pattern chat command)
          command-function (:function (get-command command))
          command-symbol (symbol command-function)]
      ((resolve command-symbol) (assoc chat :match match)))
    (default-response chat)))

(defn evaluate-chat
  [chat]
  (->> (sorted-command-keys)
       (filter #(find-command-pattern chat %))
       (first)
       (compute-response chat)
       (assoc chat :response)))

(defn echo
  {:group :echo
   :pattern #"echo\s+(.+)"
   :description "echo * - this will echo whatever you told it to"}
  [{:keys [match] :as _chat}]
  (let [[_ words] match]
    (str "echoing " words)))

(defn add-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:description command-meta)
            :function command})))

(def chat1 {:message "echo hello"
            :user "gkspranger"
            :channel "general"
            :metadata {}})

(def chat2 {:message "this will not match on anything"
            :user "gkspranger"
            :channel "general"
            :metadata {}})

(defn -main
  [& _args]
  (println "hello world"))

(comment
  
  (do
    (add-command #'echo)
    (evaluate-chat chat1))
  
  (do
    (add-command #'echo)
    (evaluate-chat chat2))

  )

