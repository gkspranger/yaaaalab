(ns yaaaalab.core
  (:gen-class))

(def commands (atom{}))
(defn sorted-command-keys [] (sort (keys @commands)))
(defn get-command [command] (get @commands command))

(defn default-response
  [{:keys [message user]}]
  (str "I'm sorry " user ", I don't understand the message:" \newline
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

(defn add-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:description command-meta)
            :group (:group command-meta)
            :function command})))

(defn -main
  [& _args]
  nil)

(comment

  (do
    (add-command #'echo)
    (evaluate-chat chat1))

  (let [groups (->> (sorted-command-keys)
                    (map #(:group (get @commands %)))
                    set)])

  (->> (sorted-command-keys)
       (map #(:group (get @commands %)))
       set
       (map (fn [x] [x []]))
       (into {}))

  (->> @commands
       (map #(:group (second %)))
       set
       (reduce #(assoc %1 %2 []) {}))




  @commands

  (-> @commands)



  (do
    (add-command #'echo)
    (add-command #'echo2)
    (evaluate-chat chat1))

  (evaluate-chat {:message "help"
                  :user "gkspranger"
                  :channel "general"
                  :metadata {}})

  (let [chat1 {:message "echo hello"
              :user "gkspranger"
              :source "shell"
              :metadata {:channel "general"}}])
  
  )

