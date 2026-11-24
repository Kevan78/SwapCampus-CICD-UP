# Labwork — Étape 9 (comparaison Android / iOS)

## Lignes de code spécifiques plateforme

```bash
wc -l composeApp/src/androidMain/kotlin/**/*.kt composeApp/src/iosMain/kotlin/**/*.kt 2>/dev/null
```

| Zone | Rôle |
|------|------|
| `androidMain` | `Formatters.android.kt`, `LanguageSelector.android.kt`, `Theme.android.kt` (Material You) |
| `iosMain` | `Formatters.ios.kt`, `LanguageSelector.ios.kt`, `Theme.ios.kt`, `MainViewController.kt` |

## Observations attendues

| Aspect | Android | iOS |
|--------|---------|-----|
| Couleurs du thème | Dynamic Material You (API 31+) | Schémas fixes Purple |
| Retour arrière | Bouton + predictive back | Gesture système |
| Sélecteur de langue | `AppCompatDelegate` in-app | Message → Réglages iPhone |
| Prix / dates | `java.text` + locale | `NSNumberFormatter` / `NSDateFormatter` |

## Ajouter l’espagnol

Un seul dossier `composeResources/values-es/` + entrées dans `Labels.kt` — **pas** de fichier par plateforme.

## Partage photo (annexe)

Il faudrait un `expect/actual` pour ouvrir le partage système (`Intent` Android / `UIActivityViewController` iOS).
