# ez-image

Easy image manipulation. Wrapper around imgscalr. No need for external libraries.

## Usage

```clojure
(:require [ez-image.core :as ez-image])

(def img (ez-image/convert "path/to/my/image.jpg" [:constrain 600 400])) ;; will give back a BufferedImage object
(ez-image/save! img "path/to/my/new-image.jpg")

;; alternatively you can set up a simple cache
(ez-image/setup! ["path/to/cache/directory/" "/web-path/"])
;; you can chain your commands
(ez-image/cache "path/to/my/image.jpg" [:constrain 600 400] [:crop 200 200]) ;; will give back "/web-path/<md5-sum>.jpg"
```

### constrain
```clojure
;; image will be constrained to a box of 100x100 pixels
(ez-image/convert img [:constrain 100])

;; image will be constrained to a box of 200x100 pixels
(ez-image/convert img [:constrain 200 100])

;; image will be constrained to a box of 200x100 pixles
;; mode is imported from ez-image.mode and holds automatic, speed, balanced, quality and ultra-quality
(ez-image/convert img [:constrain mode 200 100])
```

### distort
```clojure
;; image will be distorted to a box of 100x100 pixels
(ez-image/convert img [:distort 100])

;; image will be distorted to a box of 200x100 pixels
(ez-image/convert img [:distort 200 100])
```

### crop
```clojure
;; image will be cropped from the top left to a box of 200x100 pixels
(ez-image/convert img [:crop 200 100])

;; image will be cropped from the point 200,300 to a box of 200x100 pixels
(ez-image/convert img [:crop 200 300 200 100])
```

### pad
```clojure
;; image will be padded with a padding of 50 pixels. the colour will be black
(ez-image/convert img [:pad 50])

;; image will be padded with a padding of 50 pixels. the colour will be red
(ez-image/convert img [:pad 50 Color/Red])

;; image will be padded with a padding of 50 pixels. the colour will be green
(ez-image/convert img [:pad 50 [0 255 0]])

;; image will be padded with a padding of 50 pixels. the colour will be blue and have an alpha of 50%
(ez-image/convert img [:pad 50 [0 255 0 128])
```

### rotate
```clojure
;; image will be rotated 90 degrees clockwise
(ez-image/convert img [:rotate :cw-90])

;; image will be rotated 180 degrees clockwise
(ez-image/convert img [:rotate :cw-180])

;; image will be rotated 270 degrees clockwise
(ez-image/convert img [:rotate :cw-270])

;; image will be flipped horizontally
(ez-image/convert img [:rotate :flip-horz])

;; image will be flipped vertically
(ez-image/convert img [:rotate :flip-vert])
```

### chaining
```clojure
;; the image will have the following commands applied to it
(ez-image/convert img [:constrain 500] [:distort 700 800] [:crop 400 500] [:rotate :cw-90] [:pad 200])
```

## License

Copyright © 2013 Emil Bengtsson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
