#!/bin/bash
convert -background none $1 -thumbnail x48 -gravity center -background transparent -extent 48x48 App/src/main/res/drawable-mdpi/ic_launcher.png
convert -background none $1 -thumbnail x72 -gravity center -background transparent -extent 72x72 App/src/main/res/drawable-hdpi/ic_launcher.png
convert -background none $1 -thumbnail x96 -gravity center -background transparent -extent 96x96 App/src/main/res/drawable-xhdpi/ic_launcher.png
convert -background none $1 -thumbnail x144 -gravity center -background transparent -extent 144x144 App/src/main/res/drawable-xxhdpi/ic_launcher.png
convert -background none $1 -thumbnail x192 -gravity center -background transparent -extent 192x192 App/src/main/res/drawable-xxxhdpi/ic_launcher.png
