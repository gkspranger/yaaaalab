(ns yaaaalab.listener
  (:require [yaaaalab.event :refer [emit]]
            [yaaaalab.namespace
             :refer [all-namespaces filter-namespaces
                     filter-namespace-mappings load-namespaces]]))

(def listeners (atom []))

(defn ->listeners
  []
  @listeners)

(defn ->listener-namespaces
  [namespaces]
  (filter-namespaces #".+\.listeners\..+" namespaces))

(defn listener?
  [mapping]
  (if (:yaaaalab.listener.listener? (meta mapping))
    true
    false))

(defn load-listener
  [listener]
  (let [{pattern :yaaaalab.listener.pattern
         id :yaaaalab.listener.id} (meta listener)]
    (swap! listeners conj {:yaaaalab.listener.pattern pattern
                           :yaaaalab.listener.id id
                           :yaaaalab.listener.function listener})))

(def ->namespace-listener-mappings (partial filter-namespace-mappings
                                            listener?))

(defn load-listeners
  []
  (reset! listeners [])
  (let [listener-namespaces (->listener-namespaces (all-namespaces))
        loaded-listener-namespaces (load-namespaces listener-namespaces)
        listeners (flatten (map ->namespace-listener-mappings
                                loaded-listener-namespaces))]
    (last (map load-listener listeners))))

(defn ->listener-message-pattern-match
  [{:keys [text] :as _message}
   {pattern :yaaaalab.listener.pattern :as listener}]
  (when-let [match (re-find pattern text)]
    (assoc listener :match match)))

(defn filter-matched-listeners
  [message]
  (->> (->listeners)
       (map #(->listener-message-pattern-match message %))
       (remove empty?)))

(defn ->message-w-id
  [id message]
  (with-meta
    message
    (merge (meta message)
           {:yaaaalab.listener.id id})))

(defn apply-listener
  [message
   {match :match
    listener-id :yaaaalab.listener.id
    apply-listener-function :yaaaalab.listener.function :as _matched-listener}]
  (let [message-w-match (assoc message :match match)
        message-w-id (->message-w-id listener-id message-w-match)]
    (try
      (emit :yaaaalab.event.known-listener message-w-id)
      (apply-listener-function message-w-id)
      (catch Exception exception
        (emit :yaaaalab.event.listener-exception {:message message-w-id
                                                  :exception exception})))))

(defn evaluate-message-and-apply-matched-listeners
  [message]
  (let [matched-listeners (filter-matched-listeners message)
        no-matching-listeners? (and (coll? matched-listeners)
                                    (empty? matched-listeners))]
    (if no-matching-listeners?
      (emit :yaaaalab.event.no-matching-listeners message)
      (run! #(apply-listener message %) (filter-matched-listeners message)))))

(comment

  (load-listeners))
