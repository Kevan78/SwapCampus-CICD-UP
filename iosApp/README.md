# iosApp

1. Ouvrir le projet dans **Android Studio** (cible iOS) ou Xcode après export du framework `ComposeApp`.
2. `Product > Clean Build Folder`, puis compiler `:composeApp:embedAndSignAppleFrameworkForXcode` si nécessaire.
3. Point d’entrée Kotlin : `MainViewController()` dans `composeApp` (`iosMain`).
4. La langue suit les **Réglages** du simulateur (voir `LanguageSelector` côté iOS).
