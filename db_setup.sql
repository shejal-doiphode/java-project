-- MySQL Schema for Recipe Organizer Application

-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS recipe_db;

-- Use the database
USE recipe_db;

-- Create recipes table
CREATE TABLE IF NOT EXISTS recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    prep_time INT DEFAULT 0,
    cook_time INT DEFAULT 0,
    servings INT DEFAULT 4,
    difficulty VARCHAR(50) DEFAULT 'Medium',
    ingredients TEXT,
    instructions TEXT,
    notes TEXT,
    favorite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample recipes
INSERT INTO recipes (name, category, prep_time, cook_time, servings, difficulty, ingredients, instructions, notes, favorite)
VALUES 
('Spaghetti Bolognese', 'Pasta', 15, 45, 4, 'Medium', 
'500g ground beef
1 large onion, diced
2 garlic cloves, minced
2 carrots, finely diced
2 celery stalks, finely diced
2 tbsp tomato paste
1 can (400g) crushed tomatoes
1 cup beef stock
1 tsp dried oregano
1 tsp dried basil
Salt and pepper to taste
400g spaghetti
Grated Parmesan cheese for serving',
'1. Heat oil in a large pot over medium heat.
2. Add onions, carrots, and celery, and cook until softened (about 5 minutes).
3. Add garlic and cook for another minute.
4. Increase heat to medium-high, add ground beef and cook until browned.
5. Add tomato paste and stir for 1 minute.
6. Pour in crushed tomatoes and beef stock.
7. Add herbs, salt, and pepper.
8. Reduce heat to low and simmer for 30-40 minutes.
9. Meanwhile, cook spaghetti according to package instructions.
10. Serve sauce over spaghetti with grated Parmesan cheese.',
'For a richer flavor, add a splash of red wine when adding the stock.',
TRUE),

('Classic Chocolate Chip Cookies', 'Dessert', 15, 10, 24, 'Easy', 
'2 1/4 cups all-purpose flour
1 tsp baking soda
1 tsp salt
1 cup (2 sticks) unsalted butter, room temperature
3/4 cup granulated sugar
3/4 cup packed brown sugar
2 large eggs
2 tsp vanilla extract
2 cups semi-sweet chocolate chips',
'1. Preheat oven to 375°F (190°C).
2. In a small bowl, whisk together the flour, baking soda, and salt.
3. In a large bowl, cream together the butter, granulated sugar, and brown sugar until light and fluffy.
4. Beat in eggs one at a time, then stir in vanilla.
5. Gradually blend in the dry ingredients.
6. Fold in the chocolate chips.
7. Drop rounded tablespoons of dough onto ungreased baking sheets.
8. Bake for 9 to 11 minutes or until golden brown.
9. Let stand on baking sheet for 2 minutes, then remove to cooling racks.',
'For softer cookies, slightly underbake them. For crispier cookies, add a minute or two to the baking time.',
TRUE),

('Vegetable Stir-Fry', 'Vegetarian', 20, 10, 2, 'Easy', 
'2 tbsp vegetable oil
2 cloves garlic, minced
1 tbsp ginger, minced
1 red bell pepper, sliced
1 yellow bell pepper, sliced
1 carrot, julienned
1 cup broccoli florets
1 cup snap peas
2 tbsp soy sauce
1 tbsp oyster sauce (or vegetarian alternative)
1 tsp sesame oil
1/2 tsp sugar
1/4 cup water
2 green onions, sliced
Sesame seeds for garnish',
'1. Heat vegetable oil in a large wok or skillet over high heat.
2. Add garlic and ginger, stir-fry for 30 seconds until fragrant.
3. Add vegetables and stir-fry for 4-5 minutes until crisp-tender.
4. In a small bowl, mix soy sauce, oyster sauce, sesame oil, sugar, and water.
5. Pour sauce over vegetables and cook for another 1-2 minutes.
6. Garnish with green onions and sesame seeds.
7. Serve hot with rice or noodles.',
'Customize with your favorite vegetables or add tofu, chicken, or beef for protein.',
FALSE);