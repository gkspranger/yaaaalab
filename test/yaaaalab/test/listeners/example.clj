(ns yaaaalab.test.listeners.example)

(defn all-caps-exception
  {:yaaaalab.listener.listener? true
   :yaaaalab.listener.id :example-listener-exception
   :yaaaalab.listener.pattern #"EXCEPTION"}
  [_message]
  (/ 1 0))
