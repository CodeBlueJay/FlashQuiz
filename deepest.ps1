# Use \\?\ prefix for long paths
$folder = "\\?\C:\Users\jayra\Documents\Flashcard-Quiz-Program\dist"
Remove-Item -LiteralPath $folder -Recurse -Force
Write-Host "Deleted folder: $folder"