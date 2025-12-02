# Script PowerShell pour lancer le Puzzle Game
# Design Patterns Project

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "  Puzzle Game - Design Patterns" -ForegroundColor Yellow
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Fonction pour vérifier les prérequis
function Test-Prerequisites {
    Write-Host "Vérification des prérequis..." -ForegroundColor Green
    
    # Vérifier Java
    try {
        $javaVersion = java -version 2>&1 | Select-String "version"
        Write-Host "✓ Java trouvé: $javaVersion" -ForegroundColor Green
    }
    catch {
        Write-Host "✗ Java non trouvé. Installez JDK 17 ou supérieur." -ForegroundColor Red
        return $false
    }
    
    # Vérifier Maven
    try {
        $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
        Write-Host "✓ Maven trouvé: $mavenVersion" -ForegroundColor Green
    }
    catch {
        Write-Host "✗ Maven non trouvé. Installez Maven 3.6 ou supérieur." -ForegroundColor Red
        return $false
    }
    
    Write-Host ""
    return $true
}

# Fonction principale
function Start-Game {
    param(
        [switch]$Clean,
        [switch]$Compile,
        [switch]$Run,
        [switch]$Package,
        [switch]$ShowLog
    )
    
    if (-not (Test-Prerequisites)) {
        Write-Host "Prérequis manquants. Installation interrompue." -ForegroundColor Red
        return
    }
    
    # Clean
    if ($Clean) {
        Write-Host "Nettoyage du projet..." -ForegroundColor Yellow
        mvn clean
        if (Test-Path "game.log") {
            Remove-Item "game.log" -Force
            Write-Host "✓ game.log supprimé" -ForegroundColor Green
        }
        Write-Host ""
    }
    
    # Compile
    if ($Compile -or $Run) {
        Write-Host "Compilation du projet..." -ForegroundColor Yellow
        mvn compile
        if ($LASTEXITCODE -ne 0) {
            Write-Host "✗ Erreur de compilation" -ForegroundColor Red
            return
        }
        Write-Host "✓ Compilation réussie" -ForegroundColor Green
        Write-Host ""
    }
    
    # Package
    if ($Package) {
        Write-Host "Création du JAR exécutable..." -ForegroundColor Yellow
        mvn package
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ JAR créé: target/puzzle-game-1.0.0.jar" -ForegroundColor Green
        }
        Write-Host ""
    }
    
    # Run
    if ($Run) {
        Write-Host "Lancement du jeu..." -ForegroundColor Yellow
        Write-Host ""
        Write-Host "╔════════════════════════════════════════╗" -ForegroundColor Cyan
        Write-Host "║      CONTRÔLES DU JEU                 ║" -ForegroundColor Cyan
        Write-Host "╠════════════════════════════════════════╣" -ForegroundColor Cyan
        Write-Host "║  ENTER  : Démarrer                    ║" -ForegroundColor White
        Write-Host "║  ← →    : Déplacer                    ║" -ForegroundColor White
        Write-Host "║  ↓      : Descente rapide             ║" -ForegroundColor White
        Write-Host "║  ↑      : Rotation                    ║" -ForegroundColor White
        Write-Host "║  ESPACE : Chute instantanée           ║" -ForegroundColor White
        Write-Host "║  P      : Pause                       ║" -ForegroundColor White
        Write-Host "║  ESC    : Quitter                     ║" -ForegroundColor White
        Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Cyan
        Write-Host ""
        
        Start-Sleep -Seconds 2
        mvn javafx:run
    }
    
    # Show Log
    if ($ShowLog) {
        if (Test-Path "game.log") {
            Write-Host "Contenu du fichier de log:" -ForegroundColor Yellow
            Write-Host ""
            Get-Content "game.log" -Tail 50
        }
        else {
            Write-Host "Aucun fichier de log trouvé. Lancez le jeu d'abord." -ForegroundColor Red
        }
    }
}

# Menu interactif
function Show-Menu {
    Clear-Host
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host "  Puzzle Game - Design Patterns" -ForegroundColor Yellow
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Lancer le jeu (compile + run)" -ForegroundColor White
    Write-Host "2. Compiler uniquement" -ForegroundColor White
    Write-Host "3. Nettoyer le projet" -ForegroundColor White
    Write-Host "4. Créer un JAR exécutable" -ForegroundColor White
    Write-Host "5. Voir les logs" -ForegroundColor White
    Write-Host "6. Tout nettoyer et relancer" -ForegroundColor White
    Write-Host "7. Quitter" -ForegroundColor White
    Write-Host ""
    
    $choice = Read-Host "Choisissez une option (1-7)"
    
    switch ($choice) {
        "1" {
            Start-Game -Compile -Run
        }
        "2" {
            Start-Game -Compile
        }
        "3" {
            Start-Game -Clean
        }
        "4" {
            Start-Game -Package
        }
        "5" {
            Start-Game -ShowLog
        }
        "6" {
            Start-Game -Clean -Compile -Run
        }
        "7" {
            Write-Host "Au revoir!" -ForegroundColor Yellow
            return
        }
        default {
            Write-Host "Option invalide" -ForegroundColor Red
            Start-Sleep -Seconds 1
        }
    }
    
    if ($choice -ne "7") {
        Write-Host ""
        Write-Host "Appuyez sur une touche pour continuer..." -ForegroundColor Gray
        $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
        Show-Menu
    }
}

# Point d'entrée
if ($args.Count -eq 0) {
    Show-Menu
}
else {
    # Mode ligne de commande
    switch ($args[0]) {
        "run" { Start-Game -Compile -Run }
        "compile" { Start-Game -Compile }
        "clean" { Start-Game -Clean }
        "package" { Start-Game -Package }
        "log" { Start-Game -ShowLog }
        "rebuild" { Start-Game -Clean -Compile -Run }
        default {
            Write-Host "Usage: .\run.ps1 [run|compile|clean|package|log|rebuild]" -ForegroundColor Yellow
        }
    }
}
