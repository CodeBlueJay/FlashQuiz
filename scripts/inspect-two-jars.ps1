Add-Type -AssemblyName System.IO.Compression.FileSystem
$jars = @('build\\libs\\Flashcard-Quiz-Program.jar','build\\libs\\FlashcardQuiz.jar')
foreach ($jar in $jars) {
    if (-not (Test-Path $jar)) { Write-Host "Missing: $jar"; continue }
    Write-Host "--- $jar ---"
    $zip = [System.IO.Compression.ZipFile]::OpenRead($jar)
    $zip.Entries | ForEach-Object { Write-Host $_.FullName }
    $zip.Dispose()
    Write-Host ""
}
