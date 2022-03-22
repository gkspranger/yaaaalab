(ns yaaaalab.listeners.exception)

(defn throw-exception
  {:listener? true
   :pattern #"throw\s+exception"}
  [_message]
  (throw (Exception. "purposely thrown exception")))
