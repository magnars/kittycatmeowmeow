(ns kittycatmeowmeow.math
  (:require [cljs.core.async :refer [<! timeout]]
            [dumdom.core :as dumdom :refer [defcomponent]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

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
(defonce status (atom nil))

(defn start-game []
  (reset! status
          {:remaining-lives 3
           :questions-left 12
           :hermione-img "good-luck.png"
           :current-question (create-addition-question)}))

(defn check-answer [question answer]
  (let [correct-answer? (= answer (:correct-answer question))]
    (go
      (swap! status assoc
             :hermione-img (if correct-answer?
                             "yay.png"
                             "ohno.png")
             :show-correct-answer? true)
      (<! (timeout (if correct-answer? 800 2000)))
      (cond
        (and (= 1 (:questions-left @status))
             correct-answer?)
        (reset! status {:won-the-game!!!!! true
                        :hermione-img "you-did-it.gif"})

        (and (= 1 (:remaining-lives @status))
             (not correct-answer?))
        (reset! status {:lost-the-game!!!!!!!!!!!! true
                        :hermione-img "oops.gif"})

        :else
        (swap! status #(-> %
                           (assoc :hermione-img "good-luck.png")
                           (assoc :current-question (create-addition-question))
                           (assoc :show-correct-answer? false)
                           (cond-> correct-answer?
                             (update :questions-left dec))
                           (cond-> (not correct-answer?)
                             (update :remaining-lives dec))
                           ))))))

(defcomponent game [status]
  (let [question (:current-question status)]
    [:div
     [:img.right-img {:src (:hermione-img status)}]
     [:h1 "KittyCat­MeowMeow Math Game"]
     (when (:won-the-game!!!!! status)
       [:p "You did it! Congratulations! You are really good at math!"])
     (when (:lost-the-game!!!!!!!!!!!! status)
       [:p "Oh, no! You lost the game! Maybe you want to try again?"])
     (when question
       [:div
        [:p
         (if (= 1 (:remaining-lives status))
           [:span "This is your last life! Don't get wrong! "]
           [:span "You have " (:remaining-lives status) " lives. "])
         (if (= 1 (:questions-left status))
           [:span "This is the final question!"]
           [:span "There are " (:questions-left status) " questions left."])]
        [:h2
         "What is "
         (:first-number question)
         (:operation question)
         (:second-number question)
         "?"]
        (for [answer (:possible-answers question)]
          [:button {:className (when (and (:show-correct-answer? status)
                                          (= answer (:correct-answer question)))
                                 "correct")
                    :onClick #(check-answer question answer)}
           answer])])
     (when (not question)
       [:button {:onClick start-game}
        "Play Again"])

     ]))

(defn render [state container]
  (dumdom/render [game state] container))

(defn start! [container]
  (start-game)
  (add-watch status ::me (fn [_ _ _ new-state]
                           (render new-state container)))
  (render @status container))

(swap! status update ::reload (fnil inc 0))
