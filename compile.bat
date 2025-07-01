@echo off
echo Compiling Recipe Organizer Application...

if not exist bin mkdir bin

:: First attempt to compile all files together
javac -d bin src\com\recipeorganizer\util\DBConnection.java src\com\recipeorganizer\model\Recipe.java src\com\recipeorganizer\dao\RecipeDAO.java src\com\recipeorganizer\ui\RecipeDialog.java src\com\recipeorganizer\ui\RecipeOrganizerApp.java

if %errorlevel% equ 0 (
    echo Compilation successful.
    echo To run the application, use run.bat
) else (
    echo First compilation attempt failed. Trying with explicit classpath...
    
    :: Try with explicit classpath including the current directory
    javac -d bin -cp "lib\mysql-connector-j-9.3.0.jar;." src\com\recipeorganizer\util\DBConnection.java src\com\recipeorganizer\model\Recipe.java src\com\recipeorganizer\dao\RecipeDAO.java src\com\recipeorganizer\ui\RecipeDialog.java src\com\recipeorganizer\ui\RecipeOrganizerApp.java
    
    if %errorlevel% equ 0 (
        echo Compilation successful with explicit classpath.
        echo To run the application, use run.bat
    ) else (
        echo Both compilation attempts failed. Trying to compile files individually...
        
        :: Try compiling files individually in dependency order
        echo Compiling DBConnection.java...
        javac -d bin src\com\recipeorganizer\util\DBConnection.java
        echo Compiling Recipe.java...
        javac -d bin -cp bin src\com\recipeorganizer\model\Recipe.java
        echo Compiling RecipeDAO.java...
        javac -d bin -cp "bin;lib\mysql-connector-j-9.3.0.jar" src\com\recipeorganizer\dao\RecipeDAO.java
        echo Compiling RecipeDialog.java...
        javac -d bin -cp bin src\com\recipeorganizer\ui\RecipeDialog.java
        echo Compiling RecipeOrganizerApp.java...
        javac -d bin -cp bin src\com\recipeorganizer\ui\RecipeOrganizerApp.java
        
        echo Individual compilation complete. Check for any errors above.
        echo If successful, you can run the application using run.bat
    )
)