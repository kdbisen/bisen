' Create Desktop Shortcut for BISEN API Tester (Windows)
' Double-click this file to create a desktop shortcut

Set oWS = WScript.CreateObject("WScript.Shell")
sLinkFile = oWS.SpecialFolders("Desktop") & "\BISEN API Tester.lnk"
Set oLink = oWS.CreateShortcut(sLinkFile)

' Get the directory where this script is located
ScriptDir = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)

oLink.TargetPath = ScriptDir & "\start-windows.bat"
oLink.WorkingDirectory = ScriptDir
oLink.Description = "BISEN API Tester - Powerful API Testing Tool"
oLink.IconLocation = "shell32.dll,137" ' Use a default Windows icon
oLink.WindowStyle = 1 ' Normal window
oLink.Save

WScript.Echo "Desktop shortcut created successfully!" & vbCrLf & vbCrLf & "You can now start BISEN API Tester from your desktop."

