$ver='8.5'
$zip = "https://services.gradle.org/distributions/gradle-$ver-bin.zip"
$destRoot = Join-Path $env:USERPROFILE 'gradle'
$dest = Join-Path $destRoot "gradle-$ver"
$zipfile = Join-Path $env:TEMP "gradle-$ver.zip"
Write-Host "Attempting download from $zip"
try {
    if (-not (Test-Path $dest)) {
        Invoke-WebRequest -Uri $zip -OutFile $zipfile -UseBasicParsing -Verbose
        Expand-Archive -LiteralPath $zipfile -DestinationPath $destRoot -Force
        Remove-Item $zipfile -ErrorAction SilentlyContinue
        Write-Host "Downloaded to $dest"
    } else {
        Write-Host "Gradle already present at $dest"
    }
} catch {
    Write-Host "Download/Extract failed:"
    Write-Host $_.Exception.Message
    exit 2
}
$userPath=[Environment]::GetEnvironmentVariable("PATH","User")
if ($userPath -notlike "*gradle-$ver*") {
    [Environment]::SetEnvironmentVariable("PATH", "$userPath;$dest\bin", "User")
    Write-Host "Added gradle to user PATH"
} else {
    Write-Host "User PATH already contains gradle"
}
Write-Host "Verifying gradle binary (direct call)"
& "$dest\bin\gradle.bat" -v
