(ns major-system.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.math.combinatorics :as combo :refer [cartesian-product]]))

;; -------------------------
;; Major System Functionality

(def major-system
  {0 ["S"]
   1 ["T" "D"]
   2 ["N"]
   3 ["M"]
   4 ["R"]
   5 ["L"]
   6 ["SH" "CH"]
   7 ["K" "G"]
   8 ["F" "V"]
   9 ["P" "B"]})

(defn convert-to-stem
  "Converts a number to all possible mnemonic stems using the Major System, without including vowels or filtering to valid words."
  [input]
  (let [number (cond (string? input) input (number? input) (str input))
        numbers (map int (re-seq #"\d" number))
        values (for [n numbers] (get major-system n))]
    (->> values
         (apply cartesian-product)
         (map (partial apply str))
         sort)))

;; -------------------------
;; Views

(def app-state (atom {:number 802}))

(defn major-input []
  [:div
   [:p "Put in your number here:"]
   [:p [:input {:placeholder (:number @app-state)
                :type "text"
                :on-change #(swap! app-state assoc :number (-> % .-target .-value))}]]])

(defn major-output []
  (let [n (:number @app-state)])
  [:div
   [:p "Here are all possible combinations of that number for the Major System:"]
   (for [v (convert-to-stem (:number @app-state))]
     [:p v])])

(defn home-page []
  [:div [:h2 "Welcome to major-site"]
   ;;   major-system
   [major-input]
   [major-output]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
