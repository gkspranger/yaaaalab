(ns yaaaalab.listeners.log)

(defn log
  {:listener? true
   :pattern #"^!(.+)"}
  [{match :match}]
  (println (str "command: " (first match))))
