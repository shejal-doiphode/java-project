@echo off
echo Starting Recipe Organizer Application...

:: Run with classpath including both bin directory and MySQL connector
java -cp "bin;lib\mysql-connector-j-9.3.0.jar;." com.recipeorganizer.ui.RecipeOrganizerApp

if %errorlevel% neq 0 (
    echo Application exited with errors.
    pause
)