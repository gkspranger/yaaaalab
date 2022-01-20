(ns yaaaalab.command
  (:require [clojure.spec.alpha :as spec]
            [clojure.test :as test]))

(def commands (atom {}))
(defn sorted-keys [] (sort (keys @commands)))
(defn fetch [command] (get @commands command))

(spec/def ::command test/function?)
(spec/def ::pattern spec/regex?)
(spec/def ::group keyword?)
(spec/def ::description string?)

(defn add-command
  [command]
  (let [command-meta (meta command)
        pattern (:pattern command-meta)]
    (swap! commands assoc (str pattern)
           {:pattern pattern
            :description (:description command-meta)
            :group (:group command-meta)
            :function command})))

(defn add
  [command & commands]
  (last (map add-command (conj commands command))))

(comment
  
  (let [command1 (with-meta (fn [_] "hello") {:pattern #"hello"
                                              :group :hello
                                              :description "this is the description"})
        command2 (with-meta (fn [_] "hello123") {:pattern #"hello123"
                                                 :group :hello
                                                 :description "this is the description 123"})]
    ;(add-command command)
    ;(add command1)
    (add command1 command2)

    )
  
  )