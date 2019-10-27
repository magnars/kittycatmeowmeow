(ns kittycatmeowmeow.migration
  (:require [cljs.core.async :refer [<!]]
            [dumdom.core :as dumdom :refer [defcomponent]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defonce status
  (atom
   {:current-question 1}))

(defn question-answered [question-number]
  (when (= question-number (:current-question @status))
    (swap! status assoc :current-question (inc question-number))))

(defcomponent quiz [{:keys [current-question]}]
  [:div
   [:h1 "Kitty's Migration Quiz!"]
   [:div
    [:p "What is the most common feeling when people migrate?"]
    [:button {:onClick #(question-answered 1)} "Overjoyed!"]
    [:button {:onClick #(question-answered 1)
              :className (when (> current-question 1)
                           "correct")} "Sad"]
    [:button {:onClick #(question-answered 1)} "Angry"]
    [:button {:onClick #(question-answered 1)} "Emotionless"]]
   (when (>= current-question 2)
     [:div
      [:p "How did people migrate far away in the old days?"]
      [:button {:onClick #(question-answered 2)
                :className (when (> current-question 2)
                             "correct")} "Boat"]
      [:button {:onClick #(question-answered 2)} "Walk"]
      [:button {:onClick #(question-answered 2)} "Plane"]
      [:button {:onClick #(question-answered 2)} "Car"]])
   (when (>= current-question 3)
     [:div
      [:p "How do most people migrate now?"]
      [:button {:onClick #(question-answered 3)} "Walk"]
      [:button {:onClick #(question-answered 3)} "Car"]
      [:button {:onClick #(question-answered 3)} "Boat"]
      [:button {:onClick #(question-answered 3)
                :className (when (> current-question 3)
                             "correct")} "Plane"]])
   (when (>= current-question 4)
     [:div
      [:p "What is another way to say migrating to another country?"]
      [:button {:onClick #(question-answered 4)} "Moving To Another Place"]
      [:button {:onClick #(question-answered 4)
                :className (when (> current-question 4)
                             "correct")} "External Migration"]
      [:button {:onClick #(question-answered 4)} "Internal Migration"]
      [:button {:onClick #(question-answered 4)} "Extreme Migration"]])
   (when (>= current-question 5)
     [:div
      [:p "What is another way to say migrating but you stay in the country"]
      [:button {:onClick #(question-answered 5)} "External Migration"]
      [:button {:onClick #(question-answered 5)} "Dangerous Migration"]
      [:button {:onClick #(question-answered 5)} "Closeby  Migration"]
      [:button {:onClick #(question-answered 5)
                :className (when (> current-question 5)
                             "correct")} "Internal Migration"]])])

(defn render [state container]
  (dumdom/render [quiz state] container))

(defn start! [container]
  (add-watch status ::me (fn [_ _ _ new-state]
                           (render new-state container)))
  (render @status container))

(swap! status update ::reload (fnil inc 0))
