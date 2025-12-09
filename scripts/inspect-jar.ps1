Add-Type -AssemblyName System.IO.Compression.FileSystem
$jar = 'build\\libs\\FlashcardQuiz.jar'
if (-not (Test-Path $jar)) {
    Write-Host "Jar not found: $jar"
    exit 1
}
$zip = [System.IO.Compression.ZipFile]::OpenRead($jar)
Write-Host 'Entries in jar:'
foreach ($e in $zip.Entries) { Write-Host $e.FullName }
$manifestEntry = $zip.GetEntry('META-INF/MANIFEST.MF')
if ($manifestEntry -ne $null) {
    Write-Host "`n---- MANIFEST ----"
    $r = $manifestEntry.Open()
    $sr = New-Object System.IO.StreamReader($r)
    $content = $sr.ReadToEnd()
    $sr.Close(); $r.Close()
    Write-Host $content
} else {
    Write-Host 'No MANIFEST.MF in jar'
}
$zip.Dispose()
