(ns yaaaalab.commands.exception)

(defn exception
  "exception - always throw an exception"
  {:command? true
   :group :exception
   :pattern #"^exception$"}
  [_message]
  (/ 1 0))
