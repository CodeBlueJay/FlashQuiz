$src = 'C:\Users\jayra\Documents\Flashcard-Quiz-Program\javafx-backup\javafx-sdk-17.0.17\bin'
$dst = 'C:\Users\jayra\Documents\Flashcard-Quiz-Program\dist\Flashcards\bin'
if (-not (Test-Path $src)) {
    Write-Host "ERROR: source JavaFX bin not found: $src"
    exit 2
}
if (-not (Test-Path $dst)) {
    New-Item -ItemType Directory -Path $dst | Out-Null
}
if (Get-Command robocopy -ErrorAction SilentlyContinue) {
    robocopy $src $dst "*.dll" "*.lib" /E /NFL /NDL /NJH /NJS | Out-Null
} else {
    Get-ChildItem -Path $src -Filter '*.dll' -File -Recurse | ForEach-Object { Copy-Item -Path $_.FullName -Destination $dst -Force }
    Get-ChildItem -Path $src -Filter '*.lib' -File -Recurse | ForEach-Object { Copy-Item -Path $_.FullName -Destination $dst -Force }
}
Write-Host 'Copy complete.'
Get-ChildItem -Path $dst -Filter 'prism_*.dll' -File | ForEach-Object { Write-Host $_.Name }
