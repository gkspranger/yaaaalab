(ns yaaaalab.listeners.example)

(defn exception-in-all-caps
  {:listener? true
   :example? true
   :pattern #"EXCEPTION"}
  [_message]
  (throw (Exception. "purposely throws exception")))
