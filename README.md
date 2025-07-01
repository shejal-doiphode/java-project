# Food Recipe Organizer

A Java desktop application for organizing and managing your food recipes with a nice GUI and MySQL database connectivity.

## Features

- Add, edit, and delete recipes
- Organize recipes by categories
- Mark recipes as favorites
- Search for recipes by name or category
- View detailed recipe information including ingredients, instructions, and notes
- Filter recipes by category

## Requirements

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL Connector/J (JDBC driver for MySQL)

## Setup Instructions

### Database Setup

1. Install and start MySQL Server if you haven't already.
2. Create a database and the necessary tables using the provided SQL script:
   ```
   mysql -u root -p < db_setup.sql
   ```
   Or you can copy and paste the contents of `db_setup.sql` into your MySQL client.

### Configure Database Connection

1. Open `src/com/recipeorganizer/util/DBConnection.java`
2. Update the following constants with your MySQL connection details:
   - `URL`: The JDBC URL for your MySQL database (default: `jdbc:mysql://localhost:3306/recipe_db`)
   - `USER`: Your MySQL username (default: `root`)
   - `PASSWORD`: Your MySQL password

### Build and Run the Application

#### Using an IDE (Eclipse, IntelliJ IDEA, NetBeans)

1. Import the project into your IDE
2. Add MySQL Connector/J to your project's classpath
3. Build and run the `RecipeOrganizerApp.java` file

#### Using Command Line

1. Compile the Java files:
   ```
   javac -cp .:lib/mysql-connector-j-8.0.33.jar -d bin src/com/recipeorganizer/ui/RecipeOrganizerApp.java src/com/recipeorganizer/ui/RecipeDialog.java src/com/recipeorganizer/model/Recipe.java src/com/recipeorganizer/dao/RecipeDAO.java src/com/recipeorganizer/util/DBConnection.java
   ```
2. Run the application:
   ```
   java -cp bin:lib/mysql-connector-j-8.0.33.jar com.recipeorganizer.ui.RecipeOrganizerApp
   ```

## Project Structure

```
java project/
├── lib/                 # Libraries (MySQL Connector/J)
├── src/
│   └── com/
│       └── recipeorganizer/
│           ├── dao/      # Data Access Objects
│           │   └── RecipeDAO.java
│           ├── model/    # Data models
│           │   └── Recipe.java
│           ├── ui/       # User interface components
│           │   ├── RecipeDialog.java
│           │   └── RecipeOrganizerApp.java
│           └── util/     # Utility classes
│               └── DBConnection.java
└── db_setup.sql         # Database setup script
```

## Usage

1. Launch the application using one of the methods described above
2. The main window shows a list of recipes and their details
3. Use the search field to find recipes by name or category
4. Use the category dropdown to filter recipes by category
5. Select a recipe to view its details, including ingredients and instructions
6. Use the buttons at the bottom to add, edit, or delete recipes
7. Click "Toggle Favorite" to mark recipes as favorites

## Adding New Recipes

1. Click "Add Recipe" button
2. Fill in the recipe details in the dialog:
   - Name (required)
   - Category
   - Prep time and cook time
   - Number of servings
   - Difficulty level
   - Ingredients (one per line)
   - Instructions
   - Notes (optional)
   - Favorite status (checkbox)
3. Click "Save" to add the recipe to the database

## License

This project is open source, feel free to modify and distribute as needed.