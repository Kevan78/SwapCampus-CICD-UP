# Exercice CI/CD — SwapCampus

Le but est que GitHub teste et compile l'application automatiquement à chaque push.

| Partie | À faire | Temps |
|--------|---------|-------|
| 0 | Forker le dépôt et activer Actions | 2 min |
| 1 | Compléter le workflow de CI | 10 min |
| 2 | Rouge puis vert | 8 min |
| Bonus | Recevoir l'app sur son téléphone | à la maison |

## Partie 0 — Fork

1. Cliquer sur **Fork** en haut à droite.
2. Dans votre fork, ouvrir l'onglet **Actions** et activer les workflows.

Pour modifier un fichier : l'ouvrir sur GitHub, cliquer sur le crayon, puis **Commit changes**.

## Partie 1 — La CI

Ouvrir `.github/workflows/android-ci.yml` et compléter les 4 TODO :

| TODO | Consigne |
|------|----------|
| 1 | Lancer le workflow à chaque push sur la branche `main` |
| 2 | Installer Java 25 avec l'action `actions/setup-java@v5` |
| 3 | Lancer les tests unitaires du module `shared` |
| 4 | Construire l'APK de debug du module `androidApp` |

Faire un commit, puis ouvrir l'onglet **Actions**.

**Terminé quand :** le run est vert et l'APK `swapcampus-debug` est téléchargeable en bas de la page du run.

<details><summary>Solution</summary>

TODO 1 :
```yaml
on:
  push:
    branches: [main]
  workflow_dispatch:
```

TODO 2 :
```yaml
      - name: Set up JDK 25
        uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: 25
```

TODO 3 et 4 :
```yaml
      - name: Run unit tests
        run: ./gradlew :shared:testDebugUnitTest

      - name: Build debug APK
        run: ./gradlew :androidApp:assembleDebug
```
</details>

## Partie 2 — Rouge puis vert

Nouvelle règle : une annonce ne peut pas coûter plus de 5000 €. Le test qui vérifie cette règle existe déjà, mais il est désactivé.

1. Dans `shared/src/commonTest/.../data/ListingPriceLimitTest.kt`, supprimer la ligne `@Ignore(...)`. Faire un commit.
2. Observer le run dans **Actions** : trouver quel test échoue et pourquoi.
3. Dans `shared/src/commonMain/.../data/ListingRepository.kt`, ajouter la règle dans la fonction `checkRules()`. Faire un commit.

**Terminé quand :** le run repasse au vert.

<details><summary>Solution</summary>

Le test `priceAboveLimitIsRejected` échoue car l'application accepte encore une annonce à 5000,01 €. Dans `checkRules()`, après la vérification `price <= 0.0`, ajouter :

```kotlin
if (price > 5000.0) {
    throw IllegalArgumentException("The price cannot exceed 5000 €")
}
```
</details>

## Bonus — l'app sur votre téléphone

1. Compléter les TODO de `fastlane/Fastfile` et de `.github/workflows/android-cd-firebase.yml`.
2. Créer un projet Firebase, y ajouter une app Android `com.example.swapcampus_geoffroykevan` et un groupe de testeurs `classmates` dans App Distribution.
3. Créer un compte de service Google Cloud avec le rôle *Firebase App Distribution Admin* et télécharger sa clé JSON.
4. Ajouter dans GitHub (*Settings → Secrets and variables → Actions*) les secrets `FIREBASE_APP_ID` et `FIREBASE_SERVICE_ACCOUNT_JSON`.
5. Lancer le workflow **CD - Firebase App Distribution** depuis l'onglet Actions.

**Terminé quand :** vous recevez un e-mail de Firebase et installez l'app avec Firebase App Tester (Android 16 minimum).

<details><summary>Solution</summary>

Dans `fastlane/Fastfile` :

```ruby
  lane :test do
    gradle(tasks: [":shared:testDebugUnitTest", ":androidApp:testDebugUnitTest"])
  end

  lane :build_release do
    gradle(task: ":androidApp:assembleRelease")
  end

  lane :distribute do
    test
    build_release

    notes = ENV.fetch("RELEASE_NOTES", "").strip
    if notes.empty?
      notes = changelog_from_git_commits(
        commits_count: 5,
        pretty: "- %s (%an)",
        merge_commit_filtering: "exclude_merges"
      )
    end

    apk = File.exist?(RELEASE_APK) ? RELEASE_APK : lane_context[SharedValues::GRADLE_APK_OUTPUT_PATH]

    firebase_app_distribution(
      app: ENV.fetch("FIREBASE_APP_ID"),
      service_credentials_file: ENV.fetch("FIREBASE_SERVICE_ACCOUNT_PATH"),
      android_artifact_type: "APK",
      android_artifact_path: apk,
      groups: ENV.fetch("FIREBASE_TESTER_GROUPS", "classmates"),
      release_notes: notes
    )
  end
```

Dans `.github/workflows/android-cd-firebase.yml` :

```yaml
    env:
      FIREBASE_APP_ID: ${{ secrets.FIREBASE_APP_ID }}
      FIREBASE_SERVICE_ACCOUNT_JSON: ${{ secrets.FIREBASE_SERVICE_ACCOUNT_JSON }}
```
```yaml
      - name: Set up Ruby + Fastlane
        uses: ruby/setup-ruby@v1
        with:
          ruby-version: "3.3"
          bundler-cache: true

      - name: Write Firebase service account to a temp file
        run: |
          echo "$FIREBASE_SERVICE_ACCOUNT_JSON" > "$RUNNER_TEMP/firebase-sa.json"
          echo "FIREBASE_SERVICE_ACCOUNT_PATH=$RUNNER_TEMP/firebase-sa.json" >> "$GITHUB_ENV"
```
```yaml
      - name: Fastlane - test, build release, upload to Firebase
        run: bundle exec fastlane android distribute
        env:
          RELEASE_NOTES: ${{ github.event.inputs.release_notes }}
```
</details>
