(ns kittycatmeowmeow.main
  (:require kittycatmeowmeow.math))

(defonce start-the-app
  (do
    (when-let [container (js/document.getElementById "math")]
      (kittycatmeowmeow.math/start! container))
    :started))

