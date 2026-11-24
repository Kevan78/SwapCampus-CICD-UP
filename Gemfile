source "https://rubygems.org"

gem "fastlane"

# Fastlane plugins (firebase_app_distribution) are declared in fastlane/Pluginfile
plugins_path = File.join(File.dirname(__FILE__), "fastlane", "Pluginfile")
eval_gemfile(plugins_path) if File.exist?(plugins_path)
