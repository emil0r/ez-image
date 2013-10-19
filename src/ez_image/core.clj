(ns ez-image.core
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [ez-image.method :as method]
            [ez-image.mode :as mode]
            [ez-image.rotation :as rotation]
            [me.raynes.fs :as fs])
  (:use [digest :only [md5]])
  (:import [java.io File]
           [java.awt Color]
           [javax.imageio ImageIO]
           [org.imgscalr Scalr]))


(def ^:private -cache (atom {}))
(defn setup! [save-path web-path]
  (swap! -cache assoc :save-path save-path)
  (swap! -cache assoc :web-path web-path))

(defn- extension [path]
  (last (s/split path #"\.")))
(defn- image-io [path]
  (-> path io/as-file ImageIO/read))
(defn save! [image path]
  (ImageIO/write image (extension path) (File. path)))

(defn clear!
  ([]
     (reset! -cache {}))
  ([filename]
     (swap! -cache dissoc filename)))

(defn delete!
  ([]
     (if-let [save-path (:save-path @-cache)]
       (do
         (fs/delete-dir save-path)
         (fs/mkdir save-path)
         (clear!))))
  ([filename]
     (if-let [path (:save-path (get @-cache filename))]
       (do
         (fs/delete path)
         (clear! filename)))))

(defn- add-to-cache [digested-filename]
  (let [to-cache {:save-path (str (:save-path @-cache) digested-filename)
                  :web-path (str (:web-path @-cache) digested-filename)}]
    (swap! -cache assoc digested-filename to-cache)
    to-cache))

(defn- generate-filename [filename commands]
  (str (md5 (apply str filename commands)) "." (extension filename)))

(defn- try-cache-values []
  (cond
   (nil? (get @-cache :save-path)) (throw (Exception. ":save-path not set"))
   (nil? (get @-cache :web-path)) (throw (Exception. ":web-path not set"))
   :else true))

(defn- constrain
  ([img size]
     (Scalr/resize img size nil))
  ([img width height]
     (Scalr/resize img width height nil))
  ([img method width height]
     (Scalr/resize img method width height nil)))

(defn- distort
  ([img size]
     (Scalr/resize img mode/fit-exact size nil))
  ([img width height]
     (Scalr/resize img mode/fit-exact width height nil)))

(defn- crop
  ([img width height]
     (Scalr/crop img width height nil))
  ([img x y width height]
     (Scalr/crop img x y width height nil)))

(defn- pad
  ([img padding]
     (Scalr/pad img padding nil))
  ([img padding color]
     (if (vector? color)
       (let [[r g b a] color]
         (if (nil? a)
           (Scalr/pad img padding (Color. r g b) nil)
           (Scalr/pad img padding (Color. r g b a) nil)))
       (Scalr/pad img padding color nil))))

(defn rotate [img rotation]
  (case rotation
    :cw-90 (Scalr/rotate img rotation/cw-90 nil)
    :cw-180 (Scalr/rotate img rotation/cw-180 nil)
    :cw-270 (Scalr/rotate img rotation/cw-270 nil)
    :flip-horz (Scalr/rotate img rotation/flip-horz nil)
    :flip-vert (Scalr/rotate img rotation/flip-vert nil)
    img))

(defn flip [img orientation]
  (case orientation
    :horz (Scalr/rotate img rotation/flip-horz nil)
    :vert (Scalr/rotate img rotation/flip-vert nil)
    img))

(defmulti ^:private command (fn [_ c] (first c)))
(defmethod ^:private command :constrain [img c]
  (apply constrain img (rest c)))
(defmethod ^:private command :distort [img c]
  (apply distort img (rest c)))
(defmethod ^:private command :crop [img c]
  (apply crop img (rest c)))
(defmethod ^:private command :pad [img c]
  (apply pad img (rest c)))
(defmethod ^:private command :rotate [img c]
  (apply rotate img (rest c)))
(defmethod ^:private command :flip [img c]
  (apply flip img (rest c)))
(defmethod ^:private command :default [img _]
  img)

(defn convert [filename & commands]
  (loop [img (image-io filename)
         [c & commands] commands]
    (if (nil? c)
      img
      (recur (command img c) commands))))

(defn cache [filename & commands]
  (if (try-cache-values)
    (let [digested-filename (generate-filename filename commands)]
      (if-let [cached (get @-cache digested-filename)]
        (:web-path cached)
        (if (fs/exists? (str (:save-path @-cache) digested-filename))
          (:web-path (add-to-cache digested-filename))
          (if-let [img (apply convert filename commands)]
            (do
              (save! img (str (:save-path @-cache) digested-filename))
              (:web-path (add-to-cache digested-filename)))))))))

