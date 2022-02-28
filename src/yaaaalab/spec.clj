(ns yaaaalab.spec
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::command-group keyword?)
(spec/def ::command-pattern #(instance? java.util.regex.Pattern %))
