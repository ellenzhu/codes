#!/bin/bash
convert -background none $1 -resize 22x22 -gravity center -background transparent -extent 24x24 App/src/main/res/drawable-mdpi/ic_stat_notify.png
convert -background none $1 -resize 33x33 -gravity center -background transparent -extent 36x36 App/src/main/res/drawable-hdpi/ic_stat_notify.png
convert -background none $1 -resize 44x44 -gravity center -background transparent -extent 48x48 App/src/main/res/drawable-xhdpi/ic_stat_notify.png
convert -background none $1 -resize 66x66 -gravity center -background transparent -extent 72x72 App/src/main/res/drawable-xxhdpi/ic_stat_notify.png
convert -background none $1 -resize 88x88 -gravity center -background transparent -extent 96x96 App/src/main/res/drawable-xxxhdpi/ic_stat_notify.png
