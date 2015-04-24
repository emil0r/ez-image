# ez-image

Easy image manipulation. Wrapper around imgscalr. No need for external libraries.

## Dependancy
```clojure
[ez-image "1.0.3"]
```

## Usage

```clojure
(:require [ez-image.core :as ez-image])

(def img (ez-image/convert "path/to/my/image.jpg" [:constrain 600 400])) ;; will give back a BufferedImage object
(ez-image/save! img "path/to/my/new-image.jpg")

;; alternatively you can set up a simple cache
(ez-image/setup! {:save-path "path/to/cache/directory/" 
                  :web-path "/web-path/"})
;; it's also possible to set up a base directory where files will be looked for
;; this is a good option if you only have the relative paths and never the full path
(ez-image/setup! {:save-path "path/to/cache/directory/" 
                  :web-path "/web-path/"
                  :base-dir "/path/to/directory/with/images/"
                  :sep "/" ;; default separator for the paths is /, use this option to change it
                  })

;; you can chain your commands
(ez-image/cache "path/to/my/image.jpg" [:constrain 600 400] [:crop 200 200]) ;; will give back "/web-path/<md5-sum>.jpg"
```

Original image. Weighing in at 778kb at 1920x1080 pixels.  
![](https://raw.github.com/emil0r/ez-image/screenshots/left-right.jpg)

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
| [:constrain 100] | [:constrain 200 100] |
| ---------------- | -------------------- |
| ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-constrain-100.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-constrain-200x100.jpg) |


### distort
```clojure
;; image will be distorted to a box of 100x100 pixels
(ez-image/convert img [:distort 100])

;; image will be distorted to a box of 200x100 pixels
(ez-image/convert img [:distort 200 100])
```
| [:distort 100] | [:distort 200 100] |
| -------------- | ------------------ |
| ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-distort-100.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-distort-200x100.jpg) |


### crop
```clojure
;; image will be cropped to a box
(ez-image/convert img [:crop])

;; image will be cropped from the top left to a box of 200x100 pixels
(ez-image/convert img [:crop 200 100])

;; image will be cropped from the point 200,300 to a box of 200x100 pixels
(ez-image/convert img [:crop 200 300 200 100])
```
| [:crop 200 100] | [:crop 200 300 200 100] |
| --------------- | ----------------------- |
| ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-crop-200x100.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-crop-200x300-200x100.jpg) |


### pad
```clojure
;; image will be padded with a padding of 50 pixels. the colour will be black
(ez-image/convert img [:pad 50])

;; image will be padded with a padding of 50 pixels. the colour will be red
(ez-image/convert img [:pad 50 Color/RED])

;; image will be padded with a padding of 50 pixels. the colour will be green
(ez-image/convert img [:pad 50 [0 255 0]])

;; image will be padded with a padding of 50 pixels. the colour will be blue and have an alpha of 50%
(ez-image/convert img [:pad 50 [0 0 255 128])
```

[:constrain 200] run on all of the paddings to limit the size  

| [:pad 50] | [:pad 50 Color/RED] | [:pad 50 [0 255 0]] | [:pad 50 [0 0 255 128]] |
| --------- | ------------------- | ------------------- | ----------------------- |
| ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-pad-50.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-pad-50-red.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-pad-50-green.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-pad-50-blue-alpha-50.jpg) |


### rotate
```clojure
;; image will be rotated 90 degrees clockwise
(ez-image/convert img [:rotate :cw-90])

;; image will be rotated 180 degrees clockwise
(ez-image/convert img [:rotate :cw-180])

;; image will be rotated 270 degrees clockwise
(ez-image/convert img [:rotate :cw-270])

;; you can still use :rotate to flip the image, but it is 
;; advised to use :flip as described below instead
```

[:constrain 200] run on all of the rotations to limit the size  

| [:rotate :cw-90] | [:rotate :cw-180] | [:rotate :cw-270] |
| ---------------- | ----------------- | ----------------- |
| ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-rotate-cw-90.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-rotate-cw-180.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-rotate-cw-270.jpg) |

### flip
```clojure
;; image will be flipped horizontally
(ez-image/convert img [:flip :horz])

;; image will be flipped vertically
(ez-image/convert img [:flip :vert])
```


[:constrain 200] run on all of the rotations to limit the size  

| [:flip :horz] | [:flip :vert] |
| ---------------- | ----------------- |
|  ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-rotate-flip-horz.jpg) | ![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-rotate-flip-vert.jpg) |


### chaining
```clojure
;; the image will have the following commands applied to it
(ez-image/convert img [:constrain 500] [:distort 700 800] [:crop 400 500] [:rotate :cw-90] [:pad 200])
```
![](https://raw.github.com/emil0r/ez-image/screenshots/left-right-chained.jpg)

## License

Copyright © 2013 Emil Bengtsson

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


Coram Deo
