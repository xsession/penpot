;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.common.colors
  (:refer-clojure :exclude [test])
  (:require
   [app.common.data :as d]
   [app.common.data.macros :as dm]
   [cuerdas.core :as str]
   #_[goog.color :as gcolor]))

(def black "#000000")
(def canvas "#E8E9EA")
(def default-layout "#DE4762")
(def gray-10 "#E3E3E3")
(def gray-20 "#B1B2B5")
(def gray-30 "#7B7D85")
(def gray-40 "#64666A")
(def gray-50 "#303236")
(def info "#59B9E2")
(def test "#fabada")
(def white "#FFFFFF")
(def primary "#31EFB8")
(def danger "#E65244")
(def warning "#FC8802")

;; new-css-system colors
(def new-primary "#91fadb")
(def new-danger "#ff4986")
(def new-warning "#ff9b49")
(def canvas-background "#1d1f20")

(def names
  {"aliceblue" "#f0f8ff"
   "antiquewhite" "#faebd7"
   "aqua" "#00ffff"
   "aquamarine" "#7fffd4"
   "azure" "#f0ffff"
   "beige" "#f5f5dc"
   "bisque" "#ffe4c4"
   "black" "#000000"
   "blanchedalmond" "#ffebcd"
   "blue" "#0000ff"
   "blueviolet" "#8a2be2"
   "brown" "#a52a2a"
   "burlywood" "#deb887"
   "cadetblue" "#5f9ea0"
   "chartreuse" "#7fff00"
   "chocolate" "#d2691e"
   "coral" "#ff7f50"
   "cornflowerblue" "#6495ed"
   "cornsilk" "#fff8dc"
   "crimson" "#dc143c"
   "cyan" "#00ffff"
   "darkblue" "#00008b"
   "darkcyan" "#008b8b"
   "darkgoldenrod" "#b8860b"
   "darkgray" "#a9a9a9"
   "darkgreen" "#006400"
   "darkgrey" "#a9a9a9"
   "darkkhaki" "#bdb76b"
   "darkmagenta" "#8b008b"
   "darkolivegreen" "#556b2f"
   "darkorange" "#ff8c00"
   "darkorchid" "#9932cc"
   "darkred" "#8b0000"
   "darksalmon" "#e9967a"
   "darkseagreen" "#8fbc8f"
   "darkslateblue" "#483d8b"
   "darkslategray" "#2f4f4f"
   "darkslategrey" "#2f4f4f"
   "darkturquoise" "#00ced1"
   "darkviolet" "#9400d3"
   "deeppink" "#ff1493"
   "deepskyblue" "#00bfff"
   "dimgray" "#696969"
   "dimgrey" "#696969"
   "dodgerblue" "#1e90ff"
   "firebrick" "#b22222"
   "floralwhite" "#fffaf0"
   "forestgreen" "#228b22"
   "fuchsia" "#ff00ff"
   "gainsboro" "#dcdcdc"
   "ghostwhite" "#f8f8ff"
   "gold" "#ffd700"
   "goldenrod" "#daa520"
   "gray" "#808080"
   "green" "#008000"
   "greenyellow" "#adff2f"
   "grey" "#808080"
   "honeydew" "#f0fff0"
   "hotpink" "#ff69b4"
   "indianred" "#cd5c5c"
   "indigo" "#4b0082"
   "ivory" "#fffff0"
   "khaki" "#f0e68c"
   "lavender" "#e6e6fa"
   "lavenderblush" "#fff0f5"
   "lawngreen" "#7cfc00"
   "lemonchiffon" "#fffacd"
   "lightblue" "#add8e6"
   "lightcoral" "#f08080"
   "lightcyan" "#e0ffff"
   "lightgoldenrodyellow" "#fafad2"
   "lightgray" "#d3d3d3"
   "lightgreen" "#90ee90"
   "lightgrey" "#d3d3d3"
   "lightpink" "#ffb6c1"
   "lightsalmon" "#ffa07a"
   "lightseagreen" "#20b2aa"
   "lightskyblue" "#87cefa"
   "lightslategray" "#778899"
   "lightslategrey" "#778899"
   "lightsteelblue" "#b0c4de"
   "lightyellow" "#ffffe0"
   "lime" "#00ff00"
   "limegreen" "#32cd32"
   "linen" "#faf0e6"
   "magenta" "#ff00ff"
   "maroon" "#800000"
   "mediumaquamarine" "#66cdaa"
   "mediumblue" "#0000cd"
   "mediumorchid" "#ba55d3"
   "mediumpurple" "#9370db"
   "mediumseagreen" "#3cb371"
   "mediumslateblue" "#7b68ee"
   "mediumspringgreen" "#00fa9a"
   "mediumturquoise" "#48d1cc"
   "mediumvioletred" "#c71585"
   "midnightblue" "#191970"
   "mintcream" "#f5fffa"
   "mistyrose" "#ffe4e1"
   "moccasin" "#ffe4b5"
   "navajowhite" "#ffdead"
   "navy" "#000080"
   "oldlace" "#fdf5e6"
   "olive" "#808000"
   "olivedrab" "#6b8e23"
   "orange" "#ffa500"
   "orangered" "#ff4500"
   "orchid" "#da70d6"
   "palegoldenrod" "#eee8aa"
   "palegreen" "#98fb98"
   "paleturquoise" "#afeeee"
   "palevioletred" "#db7093"
   "papayawhip" "#ffefd5"
   "peachpuff" "#ffdab9"
   "peru" "#cd853f"
   "pink" "#ffc0cb"
   "plum" "#dda0dd"
   "powderblue" "#b0e0e6"
   "purple" "#800080"
   "red" "#ff0000"
   "rosybrown" "#bc8f8f"
   "royalblue" "#4169e1"
   "saddlebrown" "#8b4513"
   "salmon" "#fa8072"
   "sandybrown" "#f4a460"
   "seagreen" "#2e8b57"
   "seashell" "#fff5ee"
   "sienna" "#a0522d"
   "silver" "#c0c0c0"
   "skyblue" "#87ceeb"
   "slateblue" "#6a5acd"
   "slategray" "#708090"
   "slategrey" "#708090"
   "snow" "#fffafa"
   "springgreen" "#00ff7f"
   "steelblue" "#4682b4"
   "tan" "#d2b48c"
   "teal" "#008080"
   "thistle" "#d8bfd8"
   "tomato" "#ff6347"
   "turquoise" "#40e0d0"
   "violet" "#ee82ee"
   "wheat" "#f5deb3"
   "white" "#ffffff"
   "whitesmoke" "#f5f5f5"
   "yellow" "#ffff00"
   "yellowgreen" "#9acd32"})

(defn hex?
  [v]
  (and (string? v)
       (re-seq #"^#[0-9A-Fa-f]{6}$" v)))

(def ^:private hex-color-re
  #"\#[0-9a-fA-F]{3,6}")
(def ^:private rgb-color-re
  #"(?:|rgb)\((\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\)")

;; FIXME: rename to proper name
(defn valid-hex-color?
  [color]
  (some? (re-matches hex-color-re color)))

(defn parse-rgb-color
  [color]
  (let [result (re-matches rgb-color-re color)]
    (when (some? result)
      (let [r (parse-long (nth result 1))
            g (parse-long (nth result 2))
            b (parse-long (nth result 3))]
        (when (and (<= 0 r 255) (<= 0 g 255) (<= 0 b 255))
          [r g b])))))

(defn valid-rgb-color?
  [color]
  (let [result (parse-rgb-color color)]
    (some? result)))

(defn normalize-hex
  [color]
  (if (not (valid-hex-color? color))
    (throw (ex-info "invalid hex color" {:color color}))
    (if (= (count color) 4)  ; of the form #RGB
      (-> color
          (str/replace #"\#(.)(.)(.)" "#$1$1$2$2$3$3")
          (str/lower))
      (str/lower color))))

;; FIXME: performance
(defn rgb->str
  [color]
  {:pre [(vector? color)]}
  (if (= (count color) 3)
    (apply str/format "rgb(%s,%s,%s)" color)
    (apply str/format "rgba(%s,%s,%s,%s)" color)))

(defn rgb->hsv
  [[red green blue]]
  (let [max (max (max red green) blue)
        min (min (min red green) blue)
        value max]
    (if (= min max)
      [0 0 value]
      (loop [delta (- max min)
             saturation (/ delta max)
             hue (if (= red max)
                   (* 60 (/ (- green blue) delta))
                   (if (= green max)
                     (+ 2 (* 60 (/ (- blue red) delta)))
                     (+ 4 (* 60 (/ (- red green) delta)))))]
        (if (< hue 0)
          (recur (max 0 (+ hue 360)) saturation value)
          (if (> hue 360)
            (recur (max 0 (- hue 360)) saturation value)
            [hue saturation value]))))))

(defn hsv->rgb
  [[h s brightness]]
  (let [red 0
        green 0
        blue 0]
    (if (= s 0)
      [brightness brightness brightness]
      (let [sextant (Math/floor (/ h 60))
            remainder (- (/ h 60) sextant)
            val1   (* brightness (- 1 s))
            val2   (* brightness (- 1 (* s remainder)))
            val3   (* brightness (- 1 (* s (- 1 remainder))))
            result (case sextant
                     1 [val2 brightness val1]
                     2 [val1 brightness val3]
                     3 [val1 val2 brightness]
                     4 [val3 val1 brightness]
                     5 [brightness val1 val2]
                     6 [brightness val3 val1]
                     0 [brightness val3 val1])]
        (mapv int result)))))

(defn hex->rgb
  [color]
  (try
    (let [rgb #?(:clj (Integer/parseInt (subs color 1) 16)
                 :cljs (js/parseInt (subs color 1) 16))
          r   (bit-shift-right rgb 16)
          g   (bit-and (bit-shift-right rgb 8) 255)
          b   (bit-and rgb 255)]
      [r g b])
    (catch #?(:clj Throwable :cljs :default) _cause
      [0 0 0])))

(defn int->hex
  "Convert integer to hex string"
  [v]
  #?(:clj  (Integer/toHexString v)
     :cljs (.toString v 16)))

(defn rgb->hex
  [[r g b]]
  (let [r (int r)
        g (int g)
        b (int b)]
    (if (or (not= r (bit-and r 255))
            (not= g (bit-and g 255))
            (not= b (bit-and b 255)))
      (throw (ex-info "not valid rgb" {:r r :g g :b b}))
      (let [rgb (bit-or (bit-shift-left r 16)
                        (bit-shift-left g 8) b)]

        ;; FIXME: add cljs variant here
        (if (< r 16)
          (str "#" (subs (int->hex (bit-or 0x1000000 rgb)) 1))
          (str "#" (int->hex rgb)))))))

(defn rgb->hsl
  [[r g b]]
  (let [norm-r (/ r 255.0)
        norm-g (/ g 255.0)
        norm-b (/ b 255.0)
        max    (max norm-r norm-g norm-b)
        min    (min norm-r norm-g norm-b)
        l      (/ (+ max min) 2.0)]
    (let [h (if (= max min) 0
                (if (= max norm-r)
                  (* 60 (/ (- norm-g norm-b) (- max min)))
                  (if (= max norm-g)
                    (+ 120 (* 60 (/ (- norm-b norm-r) (- max min))))
                    (+ 240 (* 60 (/ (- norm-r norm-g) (- max min)))))))
          s (if (and (> l 0) (<= l 0.5))
              (/ (- max min) (* 2 l))
              (/ (- max min) (- 2 (* 2 l))))]

      [(mod (+ h 360) 360) s l])))

(defn hex->hsv
  [v]
  (-> v hex->rgb rgb->hsv))

(defn hex->rgba
  [data opacity]
  (-> (hex->rgb data)
      (conj opacity)))

(defn hex->hsl [hex]
  (try
    (-> hex hex->rgb rgb->hsl)
    (catch #?(:clj Throwable :cljs :default) _e
      [0 0 0])))

(defn hex->hsla
  [data opacity]
  (-> (hex->hsl data)
      (conj opacity)))

(defn format-hsla
  [[h s l a]]
  (let [precision 2
        rounded-s (* 100 (d/format-precision s precision))
        rounded-l (* 100 (d/format-precision l precision))]

    (str/fmt "%s, %s%, %s%, %s" h rounded-s rounded-l a)))

;; (defn hsl->rgb
;;   [[h s l]]
;;   (gcolor/hslToRgb h s l))

(defn hue->rgb
  [v1 v2 v-h]
  (let [v-h (if (< v-h 0) (+ v-h 1) (if (> v-h 1) (- v-h 1) v-h))]
    (cond
      (< (* 6 v-h) 1) (+ v1 (* (- v2 v1) (* 6 v-h)))
      (< (* 2 v-h) 1) v2
      (< (* 3 v-h) 2) (+ v1 (* (- v2 v1) (- (/ 2 3) v-h) (* 6)))
      :else v1)))

(defn hsl->rgb
  [[h s l]]
  (let [r 0
        g 0
        b 0
        norm-h (/ h 360.0)]
    (if (= s 0)
      (let [gray (* l 255)]
        [gray gray gray])
      (let [temp1 0
            temp2 0
            temp1 (if (< l 0.5)
                    (* 2 l)
                    (- (+ l s) (* s l)))
            temp2 (if (< l 0.5)
                    (* l (+ 1 s))
                    (- l (* s l))
                    )]
        (let [r (int (* 255 (hue->rgb temp1 temp2 (+ norm-h (/ 1 3)))))
              g (int (* 255 (hue->rgb temp1 temp2 norm-h)))
              b (int (* 255 (hue->rgb temp1 temp2 (- norm-h (/ 1 3)))))]
          [r g b])))))

(defn hsl->hex
  [v]
  (-> v hsl->rgb rgb->hex))

(defn hsl->hsv
  [hsl]
  (-> hsl hsl->rgb rgb->hsv))

;; NOTE i dont know how this works because it does not exists on google closure
;; (defn hsl->hsv
;;   [[h s l]]
;;   (gcolor/hslToHsv h s l))

(defn hsv->hex
  [hsv]
  (-> hsv hsv->rgb rgb->hex))

(defn hsv->hsl
  [hsv]
  (-> hsv hsv->hex hex->hsl))

(defn expand-hex
  [v]
  (cond
    (re-matches #"^[0-9A-Fa-f]$" v)
    (str v v v v v v)

    (re-matches #"^[0-9A-Fa-f]{2}$" v)
    (str v v v)

    (re-matches #"^[0-9A-Fa-f]{3}$" v)
    (let [a (nth v 0)
          b (nth v 1)
          c (nth v 2)]
      (str a a b b c c))

    :else
    v))

(defn prepend-hash
  [color]
  (if (= "#" (subs color 0 1))
    color
    (str "#" color)))

(defn remove-hash
  [color]
  (if (str/starts-with? color "#")
    (subs color 1)
    color))

(defn gradient->css [{:keys [type stops]}]
  (let [parse-stop
        (fn [{:keys [offset color opacity]}]
          (let [[r g b] (hex->rgb color)]
            (str/fmt "rgba(%s, %s, %s, %s) %s" r g b opacity (str (* offset 100) "%"))))

        stops-css (str/join "," (map parse-stop stops))]

    (if (= type :linear)
      (str/fmt "linear-gradient(to bottom, %s)" stops-css)
      (str/fmt "radial-gradient(circle, %s)" stops-css))))

;; TODO: REMOVE `VALUE` WHEN COLOR IS INTEGRATED
(defn color->background [{:keys [color opacity gradient value]}]
  (let [color (or color value)
        opacity (or opacity 1)]
    (cond
      (and gradient (not= :multiple gradient))
      (gradient->css gradient)

      (not= color :multiple)
      (let [[r g b] (hex->rgb (or color value))]
        (str/fmt "rgba(%s, %s, %s, %s)" r g b opacity))

      :else "transparent")))

(defn color->format->background [{:keys [color opacity gradient]} format]
  (let [opacity (or opacity 1)]
    (cond
      (and gradient (not= :multiple gradient))
      (gradient->css gradient)

      (not= color :multiple)
      (case format
        :rgba (let [[r g b] (hex->rgb color)]
               (str/fmt "rgba(%s, %s, %s, %s)" r g b opacity))

        :hsla (let [[h s l] (hex->hsl color)]
                (str/fmt "hsla(%s, %s, %s, %s)" h (* 100 s) (* 100 l) opacity))

        :hex (str color (str/upper (d/opacity-to-hex opacity))))

      :else "transparent")))

(defn multiple? [{:keys [id file-id value color gradient]}]
  (or (= value :multiple)
      (= color :multiple)
      (= gradient :multiple)
      (= id :multiple)
      (= file-id :multiple)))

(defn color?
  [color]
  (and (string? color)
       (or (valid-hex-color? color)
           (valid-rgb-color? color)
           (contains? names color))))

(defn parse
  [color]
  (if (valid-hex-color? color)
    (str/lower color)
    (let [color (parse-rgb-color color)]
      (if (some? color)
        (rgb->hex color)
        (get names (str/lower color))))))

(def color-names
  (into [] (keys names)))

(def empty-color
  (into {} (map #(vector % nil)) [:color :id :file-id :gradient :opacity]))

(defn next-rgb
  "Given a color in rgb returns the next color"
  [[r g b]]
  (cond
    (and (= 255 r) (= 255 g) (= 255 b))
    (throw (ex-info "cannot get next color" {:r r :g g :b b}))

    (and (= 255 g) (= 255 b))
    [(inc r) 0 0]

    (= 255 b)
    [r (inc g) 0]

    :else
    [r g (inc b)]))
