# Labwork KMP/CMP SwapCampus — Étapes 0 et 1

Projet : `SwapCampus_GeoffroyKevan`  
Date : 2026-11-10  
Référence : *2627_Labwork_KMP_CMP_SwapCampus_En.pdf*

---

## Étape 0 — Point de départ et cible

### Checkpoint (avant migration)

| Vérification | Résultat |
|--------------|----------|
| `./gradlew :androidApp:assembleDebug` | OK |
| `./gradlew :shared:test` | OK |
| App Android complète (navigation + écrans) | OK — `MainActivity` → `AppNavigation()` |
| Module iOS (`iosApp`, `compileKotlinIosSimulatorArm64`) | **Absent** |
| `commonMain` / `androidMain` / `iosMain` dans `:composeApp` | **Absent** — tout est dans `composeApp/src/main/java` (équivalent *androidMain* du polycopié) |
| Login partagé iOS + `App()` polycopié | **Absent** — login dans le même module Android que le reste |

**Conclusion étape 0 :** la base **Android** fonctionne, mais il manque le **squelette KMP + iOS** du tutoriel démo (login migré, `iosApp`, `:shared` multiplateforme). Les étapes 2–9 du lab supposent ce point de départ ; il faudra soit repartir du projet ISEP fourni après la démo, soit convertir ce dépôt (KMP sur `:shared` et `:composeApp`, cible iOS, Compose Resources) **avant** d’enchaîner sur l’étape 2.

### État actuel (inventaire des modules)

| Zone | Contenu actuel | Plateforme |
|------|----------------|------------|
| `:shared` | Domaine, `AuthRepository`, `ListingRepository`, `DateUtils.today()` via `java.time` | JVM (Android uniquement via `:composeApp`) |
| `composeApp/src/main` | UI complète : écrans, `AppNavigation`, `Common.kt` (carte + i18n minimal), thème, ViewModels | Android |
| `:androidApp` | `MainActivity`, `res/values/strings.xml` (`app_name` seul) | Android |
| `:iosApp` | — | — |

Correspondance avec le polycopié :

| Dossier polycopié | Équivalent dans ce dépôt |
|-------------------|---------------------------|
| `androidMain` de `:composeApp` | `composeApp/src/main/java/...` |
| `commonMain` de `:composeApp` | *À créer* |
| `iosMain` + `iosApp` | *À créer* |
| `commonMain` de `:shared` | *À créer* — aujourd’hui `shared/src/main/kotlin` (JVM) |

### Cible en fin de lab (rappel polycopié)

| Dossier | Contenu final attendu |
|---------|-------------------------|
| `commonMain` (`:composeApp`) | Toute l’UI, ViewModels, navigation, labels, textes (`composeResources`), déclarations `expect` |
| `androidMain` | `actual` formats + sélecteur de langue, `AppLanguage`, thème dynamique Android, `Platform.android.kt` |
| `iosMain` | `actual` formats + sélecteur (message iOS), thème, `MainViewController.kt` |
| `commonMain` (`:shared`) | Domaine, repositories, `todayDate()` multiplateforme |
| `:androidApp` | `MainActivity` qui appelle `App()` |
| `:iosApp` | `ContentView.swift` → `MainViewController()` |

---

## Étape 1 — Inventaire des blocages vers `commonMain`

### Commande (adaptée à ce dépôt)

Le polycopié cible `composeApp/src/androidMain/kotlin`. Ici :

```bash
grep -rnE "R\.string|import android\.|androidx\.(appcompat|annotation|navigation)|import java\." \
  composeApp/src/main
```

Sortie enregistrée dans [`labwork/grep-etape1-composeApp.txt`](../labwork/grep-etape1-composeApp.txt).

Complément utile (`:shared` et thème) :

```bash
grep -rnE "import java\.|java\.time" shared/src
grep -rnE "LocalContext|import android\." composeApp/src/main
```

### Résultat `grep` — `composeApp/src/main`

| Fichier | Ligne | Texte détecté | Interprétation |
|---------|-------|---------------|----------------|
| `ui/navigation/AppNavigation.kt` | 18–24 | `import androidx.navigation.*` | Navigation **AndroidX** — remplacer par JetBrains Navigation + routes `@Serializable` (étape 7) |
| `ui/theme/Theme.kt` | 3–4 | `import android.app.Activity`, `import android.os.Build` | APIs Android pour Material You — `expect/actual` thème (démo + fin de lab) |

**Aucune** occurrence de `R.string`, `AppCompat`, `androidx.annotation`, ni `import java.*` dans `composeApp/src/main` (bonne nouvelle pour un move mécanique des écrans).

### Autres blocages hors `grep` polycopié (constatés dans le code)

| Fichier / zone | Blocage | Impact iOS / `commonMain` |
|----------------|---------|---------------------------|
| `shared/.../DateUtils.kt` | `java.time.LocalDate.now()` | `:shared` doit devenir KMP avec `todayDate()` expect/actual ou `kotlinx-datetime` + `Clock` |
| `ui/theme/Theme.kt` | `LocalContext`, `dynamicDarkColorScheme` | Thème : partie en `androidMain`, schémas fixes en `commonMain` |
| ViewModels (`LoginViewModel`, `ListingsViewModel`, `ListingDetailViewModel`, `ListingFormViewModel`, `MyListingsViewModel`) | `androidx.lifecycle.ViewModel`, `viewmodel.compose.viewModel` | Remplacer par **lifecycle JetBrains** + `viewModel(factory = …)` (comme `AuthViewModel` dans la démo) |
| Textes UI | Chaînes en dur EN/FR (`Common.kt`, écrans, `AppNavigation`) | Pas de `R.string` mais pas i18n FR/EN du polycopié → **Compose Resources** + `Labels.kt` (étape 4) |
| `Common.kt` | `String.format("%.2f €", …)` pour les prix | Remplacer par `expect/actual` `formatPrice` / `formatDate` (étape 3) |
| `ProfileScreen` | Pas de sélecteur de langue | Créer `expect fun LanguageSelector()` (étape 5) |
| `composeApp/build.gradle.kts` | Plugin Android library, deps AndroidX Compose/Navigation | Reconfigurer module **KMP** avec `commonMain` / `androidMain` / `iosMain` |

---

## Tableau feuille de route (polycopié — rempli pour ce projet)

| Fichiers / famille | Ce qui bloque le déplacement | Solution prévue |
|--------------------|------------------------------|-----------------|
| `ListingsViewModel`, `ListingsUiState` (+ `MyListingsViewModel` dans le même fichier) | `androidx.lifecycle` (ViewModel Android) | Déplacer le code métier tel quel, puis **adapter les imports** lifecycle JetBrains (étape 2) ; fusionner / extraire `UiMessage` si besoin pour coller au polycopié |
| `LoginViewModel` (dans `LoginScreen.kt`), ViewModels dans `ListingDetailScreen` / `ListingFormScreen` | Idem `androidx.lifecycle` | Même traitement que ci-dessus ; éventuellement extraire vers `ui/viewmodel/` |
| `Labels.kt` (polycopié) | **Fichier absent** — libellés dans `Common.kt` (`displayName()`, statuts) et textes dispersés | Créer `Labels.kt` en `commonMain` + **Compose Resources** (étape 4) |
| `Formatters.kt` (polycopié) | **Absent** — `toPrice()` dans `Common.kt` | **expect/actual** `Formatters.kt` (étape 3) |
| `AppLanguage.kt` + sélecteur profil | **Absent** | **expect/actual** `LanguageSelector` ; `AppLanguage` reste côté Android (étape 5) |
| `ui/components/*` (polycopié) | Pas de dossier dédié — `ListingCard`, chips, etc. dans `Common.kt` | Déplacer / scinder vers `commonMain` après i18n ; **déplacer tel quel** côté Compose (pas de `R`) |
| `ui/screens/*` | Textes en dur ; lifecycle Android dans ViewModels embarqués | Déplacer écrans en `commonMain` (étape 6) ; corriger lifecycle + `Res.string` |
| `Routes` / `SwapCampusApp` (polycopié) | `AppNavigation.kt` : routes string + `androidx.navigation` | Rester en *android-only* jusqu’à l’étape 7, puis routes `@Serializable` + `App()` commun |
| `res/values/strings.xml` | Quasi vide (`app_name` dans `:androidApp` seulement) | Migrer **tous** les textes vers `composeResources` (étape 4) ; ne pas supprimer `strings.xml` avant fin étape 7 |
| `Theme.kt`, `Color.kt`, `Type.kt` | `android.os.Build`, `LocalContext`, dynamic color | **expect/actual** thème (déjà vu en démo) ; couleurs/typo en commun si possible |
| `AppContainer.kt` | Aucune dépendance Android | **Déplacer tel quel** vers `commonMain` (ou aligner sur injection Koin — annexe) |
| `:shared` (domaine + data) | `java.time` dans `DateUtils` | Passer `:shared` en **KMP** ; `today()` multiplateforme |

---

## Prochaines actions recommandées

1. **Prérequis KMP** (hors numérotation du lab mais nécessaire ici) : convertir `:composeApp` et `:shared`, ajouter `iosApp`, valider login iOS comme dans la démo.
2. **Étape 2** : déplacer les ViewModels sans logique Android vers `commonMain` + lifecycle JetBrains.
3. Garder ce fichier et [`labwork/grep-etape1-composeApp.txt`](../labwork/grep-etape1-composeApp.txt) comme référence pendant le TP.

---

## Branche de travail

Créer une branche dédiée avant l’étape 2, par exemple :

```bash
git checkout -b labwork/kmp-cmp-migration
```
