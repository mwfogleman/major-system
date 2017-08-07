(ns major-system.core
  (:require [reagent.core :as r]
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
  (let [numbers (map int (re-seq #"\d" input))
        values (for [n numbers] (get major-system n))]
    (->> values
         (apply cartesian-product)
         (map (partial apply str))
         sort)))

;; -------------------------
;; Views

(def app-state (r/atom "802"))

(defn explanation []
  [:div
   [:p "I recently read and loved " [:a {:href "https://en.wikipedia.org/wiki/Joshua_Foer"} "Joshua Foer"] "'s " [:a {:href "https://www.goodreads.com/book/show/6346975-moonwalking-with-einstein"} "Moonwalking with Einstein"] ". The book is \"participatory journalism,\" in which the author explores the topic of human memory through the experience of training for memorization competitions. The book explores many facets of memory, its history and its implications, and has a few unexpected twists and turns."]
   [:p "As a reader, you are also a participant in the author's training. Foer teaches you the basics of powerful techniques for memorization like " [:a {:href "https://en.wikipedia.org/wiki/Method_of_loci"} "memory palaces"] ". Before reading the book, I had been familiar with memory palaces, but I understood the technique better, and also learned several new-to-me techniques. One of the new-for-me techniques covered in the book is the \"" [:a {:href "https://en.wikipedia.org/wiki/Major_system"} "Major System"]  ".\" Foer explains:"] 
   [:blockquote
    [:p "I started trying to use my memory in everyday life, even when I wasn’t practicing for the handful of arcane events that would be featured in the championship. Strolls around the neighborhood became an excuse to memorize license plates. I began to pay a creepy amount of attention to name tags. I memorized my shopping lists. I kept a calendar on paper, and also one in my mind. Whenever someone gave me a phone number, I installed it in a special memory palace."]
    [:p "Remembering numbers proved to be one of the real world applications of the memory palace that I relied on almost every day. I used a technique known as the “Major System,” invented around 1648 by Johann Winkelmann, which is nothing more than a simple code to convert numbers into phonetic sounds. Those sounds can then be turned into words, which can in turn become images for a memory palace. The code works like this:"]
    [:p [:center [:img {:src "http://www.mwfogleman.com/images/major.png"}]]]
    [:p "The number 32, for example, would translate into MN, 33 would be MM, and 34 would be MR. To make those consonants meaningful, you’re allowed to freely intersperse vowels. So the number 32 might turn into an image of a man, 33 could be your mom, and 34 might be the Russian space station Mir. Similarly, the number 86 might be a fish, 40 a rose, and 92 a pen. You might visualize 3,219 as a man (32) playing a tuba (19), or maybe a person from Manitoba (3,219). Likewise, 7,879 would translate to KFKP, which might turn into a single image of a coffee cup, or two images of a calf and a cub. The advantage of the Major System is that it’s straightforward, and you can begin using it right out of the box. (When I first learned it, I immediately memorized my credit card and bank account numbers.) But nobody wins any international memory competitions with the Major System."]]
   [:p "After first reading about the Major System, my girlfriend and I tried it on a long car ride, as a kind of game or puzzle. The first obstacle  we hit was that we needed to memorize the numbers, because the number-sound combinations are, as far as I can tell, arbitrary. Once we did that, we started practicing with our phone numbers. But the conversion from numbers to sounds to words was slow and painstaking, at least to start. So even though the major system is an especially practical memory tool, I haven't made as much use of it since then as I might."]
   [:p "However, I recently realized I could create a computer program to turn numbers into possible combinations of consonants. You still have to insert vowels and pick one that will turn into a memorable phrase, but it does some of the hard work for you, which might be useful for familiarizing yourself with the Major System. I've also embedded the program into this page, so you can get real-time feedback for major system combinations. Try putting in an important number, like a phone number or PIN."]
   [:p "(For those of you who are interested: the logic is about 10 lines of " [:a {:href "http://clojurescript.org/"} "ClojureScript"] ", and uses " [:a {:href "https://en.wikipedia.org/wiki/Cartesian_product"} "Cartesian Products"] "; it is then embedded in this page with the dead-simple React wrapper " [:a {:href "https://reagent-project.github.io/"} "Reagent"] ".)"]])

(defn major-input []
  [:div
   [:p "Put in a number here:"]
   [:p [:center [:input {:placeholder @app-state
                         :type "text"
                         :on-change #(reset! app-state (-> % .-target .-value))}]]]])

(defn major-output []
  (let [n @app-state]
    [:div
     [:p "Here are all possible conversions of that number into the consonant sounds that are used in the Major System:"]
     (for [v (convert-to-stem n)]
       [:p v])]))

(defn home-page []
  [:div
   [:h2 "The Major System"]
   [:h3 "by " [:a {:href "http://www.mwfogleman.com"} "Michael Fogleman"]]
   [explanation]
   [major-input]
   [major-output]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
