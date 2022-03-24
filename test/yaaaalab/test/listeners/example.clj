(ns yaaaalab.test.listeners.example)

(defn all-caps-exception
  {:listener? true
   :pattern #"EXCEPTION"}
  [_message]
  (/ 1 0))
