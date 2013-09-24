(ns ez-image.mode
  (:import [org.imgscalr Scalr$Mode]))

(def automatic
  Scalr$Mode/AUTOMATIC)

(def fit-exact
  Scalr$Mode/FIT_EXACT)

(def fit-to-height
  Scalr$Mode/FIT_TO_HEIGHT)

(def fit-to-width
  Scalr$Mode/FIT_TO_WIDTH)
