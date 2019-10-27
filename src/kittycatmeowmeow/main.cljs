(ns kittycatmeowmeow.main
  (:require kittycatmeowmeow.math
            kittycatmeowmeow.migration))

(defonce start-the-app
  (do
    (when-let [container (js/document.getElementById "math")]
      (kittycatmeowmeow.math/start! container))
    (when-let [container (js/document.getElementById "migration")]
      (kittycatmeowmeow.migration/start! container))
    :started))

