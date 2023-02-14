# Pixabay Images Viewer

## Overview
Images search engine based on Pixabay API.

Search and preview images in both online and offline modes.


## App
* Min. deployment target Android API 24
* Kotlin source code
* Offline handling
* Multi module MVVM architecture


## Screenshots

<p align="center">
<img src="https://github.com/SembaMax/pixabay-images-app/blob/main/screenshots/main_light.jpg" width="256" />
&nbsp;
<img src="https://github.com/SembaMax/pixabay-images-app/blob/main/screenshots/detail_light.jpg" width="256" />
&nbsp;
</p>
  
<p align="center">
<img src="https://github.com/SembaMax/pixabay-images-app/blob/main/screenshots/main_dark.jpg" width="256" />
&nbsp;
<img src="https://github.com/SembaMax/pixabay-images-app/blob/main/screenshots/detail_dark.jpg" width="256" />
</p>


## How to pull the repository

	git clone git@github.com:SembaMax/pixabay-images-app.git
	gradle clean build
  

## Build Requirements

- JDK11
- Supports minimum API 24
- Kotlin 1.7.21+

## Configuration

1. Signup or Login into [Pixabay](https://pixabay.com/api/docs/#api_search_images) for getting API_KEY
2. Add `PIXABAY_API_KEY="Your_Api_Key"` to `local.properties` file.

```kotlin
  defaultConfig {
        buildConfigField("String", "PIXABAY_API_KEY", "\"${properties.getProperty("PIXABAY_API_KEY")}\"")
    }
 ```


## Features

- Jetpack Compose
- Compose Navigation
- Material Design 3
- Themes
- Flows
- Retrofit2
- Timber
- Room
- Hilt
- Coroutines
- Coil
- Gradle kts
- Modularization


## Android Keystores

[Keystores Folder](https://github.com/SembaMax/pixabay-images-app/tree/main/certifactes)

#### debug.jks

path:  /certifactes/debug.jks

    keytool -list -v -keystore debug.jks -alias debug -storepass "yDgU54VF7#brJh*A" -keypass "yDgU54VF7#brJh*A" 
    SHA1: BE:92:C6:81:AC:34:7D:F8:AE:97:E9:00:3F:46:80:8D:3A:73:46:DC

#### release.jks

path:  /certifactes/release.jks

    keytool -list -v -keystore release.jks -alias release -storepass "Yo#Hsd4mD0QF3E^i" -keypass "Yo#Hsd4mD0QF3E^i"
    SHA1: E4:44:34:BD:93:BB:63:1B:9A:EC:3C:52:84:38:BB:16:A1:3E:53:C0
    
