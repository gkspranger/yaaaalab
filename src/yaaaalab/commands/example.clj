(ns yaaaalab.commands.example)

(defn example-exception
  "example exception - throws an exception"
  {:command? true
   :example? true
   :group :example
   :pattern #"^example\s+exception$"}
  [_message]
  (/ 1 0))
