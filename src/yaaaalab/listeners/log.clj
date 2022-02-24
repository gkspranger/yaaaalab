(ns yaaaalab.listeners.log)

(defn log
  {:listener? true
   :pattern #"^!(.+)"}
  [{match :match}]
  (println (first match)))
