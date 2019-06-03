(ns kittycatmeowmeow.math
  (:require [dumdom.core :as dumdom :refer [defcomponent]]))

(defonce container (js/document.getElementById "app"))

(defn create-addition-question []
  (let [first-number (+ 1 (rand-int 20))
        second-number (+ 1 (rand-int 20))
        correct-answer (+ first-number second-number)]
    {:operation "+"
     :first-number first-number
     :second-number second-number
     :correct-answer correct-answer
     :possible-answers (shuffle
                        [(rand-int 40)
                         correct-answer
                         (rand-int 56)
                         (str first-number second-number)])}))

(defonce status (atom {:remaining-lives 3
                       :questions-left 12
                       :hermione-img "good-luck.png"
                       :current-question (create-addition-question)}))

(defn check-answer [question answer]
  (swap! status assoc :hermione-img
   (if (= answer (:correct-answer question))
     "yay.png"
     "ohno.png")))

(defcomponent game [status]
  (let [question (:current-question status)]
    [:div
     [:img.right-img {:src (:hermione-img status)}]
     [:h1 "KittyCat­MeowMeow Math Game"]
     [:p "You have " (:remaining-lives status) " lives. There are " (:questions-left status) " questions left."]
     [:h2
      "What is "
      (:first-number question)
      (:operation question)
      (:second-number question)
      "?"]
     (for [answer (:possible-answers question)]
       [:button {:onClick #(check-answer question answer)}
        answer])]))

(defn render [state]
  (dumdom/render [game state] container))

(add-watch status ::me (fn [_ _ _ new-state]
                         (render new-state)))

(swap! status update ::reload (fnil inc 0))
