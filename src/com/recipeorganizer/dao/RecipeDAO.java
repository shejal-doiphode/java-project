package com.recipeorganizer.dao;

import com.recipeorganizer.model.Recipe;
import com.recipeorganizer.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Recipe operations
 */
public class RecipeDAO {
    
    /**
     * Retrieves all recipes from the database
     * @return List of Recipe objects
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Recipe recipe = mapResultSetToRecipe(rs);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recipes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Retrieves a recipe by its ID
     * @param id Recipe ID
     * @return Recipe object or null if not found
     */
    public Recipe getRecipeById(int id) {
        String sql = "SELECT * FROM recipes WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecipe(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recipe by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Adds a new recipe to the database
     * @param recipe Recipe object to add
     * @return ID of the newly added recipe, or -1 if failed
     */
    public int addRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipes (name, category, prep_time, cook_time, servings, " +
                "difficulty, ingredients, instructions, notes, favorite) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set parameters
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getCategory());
            pstmt.setInt(3, recipe.getPrepTime());
            pstmt.setInt(4, recipe.getCookTime());
            pstmt.setInt(5, recipe.getServings());
            pstmt.setString(6, recipe.getDifficulty());
            pstmt.setString(7, recipe.getIngredients());
            pstmt.setString(8, recipe.getInstructions());
            pstmt.setString(9, recipe.getNotes());
            pstmt.setBoolean(10, recipe.isFavorite());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                return -1;
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding recipe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Updates an existing recipe in the database
     * @param recipe Recipe object with updated values
     * @return true if successful, false otherwise
     */
    public boolean updateRecipe(Recipe recipe) {
        String sql = "UPDATE recipes SET name = ?, category = ?, prep_time = ?, " +
                "cook_time = ?, servings = ?, difficulty = ?, ingredients = ?, " +
                "instructions = ?, notes = ?, favorite = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getCategory());
            pstmt.setInt(3, recipe.getPrepTime());
            pstmt.setInt(4, recipe.getCookTime());
            pstmt.setInt(5, recipe.getServings());
            pstmt.setString(6, recipe.getDifficulty());
            pstmt.setString(7, recipe.getIngredients());
            pstmt.setString(8, recipe.getInstructions());
            pstmt.setString(9, recipe.getNotes());
            pstmt.setBoolean(10, recipe.isFavorite());
            pstmt.setInt(11, recipe.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating recipe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Deletes a recipe from the database
     * @param id ID of the recipe to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRecipe(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting recipe: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Searches for recipes by name, category, or ingredients
     * @param searchTerm Term to search for
     * @return List of matching Recipe objects
     */
    public List<Recipe> searchRecipes(String searchTerm) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE name LIKE ? OR category LIKE ? OR ingredients LIKE ? ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String term = "%" + searchTerm + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapResultSetToRecipe(rs);
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching for recipes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Retrieves recipes by category
     * @param category Category to filter by
     * @return List of Recipe objects in the specified category
     */
    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE category = ? ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = mapResultSetToRecipe(rs);
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recipes by category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recipes;
    }
    
    /**
     * Retrieves a list of all unique categories
     * @return List of category names
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM recipes WHERE category IS NOT NULL AND category != '' ORDER BY category";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }
    
    /**
     * Toggles the favorite status of a recipe
     * @param id Recipe ID
     * @param favorite New favorite status
     * @return true if successful, false otherwise
     */
    public boolean toggleFavorite(int id, boolean favorite) {
        String sql = "UPDATE recipes SET favorite = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, favorite);
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error toggling favorite status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Maps a ResultSet row to a Recipe object
     * @param rs ResultSet containing recipe data
     * @return Recipe object
     * @throws SQLException if an error occurs
     */
    private Recipe mapResultSetToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("id"));
        recipe.setName(rs.getString("name"));
        recipe.setCategory(rs.getString("category"));
        recipe.setPrepTime(rs.getInt("prep_time"));
        recipe.setCookTime(rs.getInt("cook_time"));
        recipe.setServings(rs.getInt("servings"));
        recipe.setDifficulty(rs.getString("difficulty"));
        recipe.setIngredients(rs.getString("ingredients"));
        recipe.setInstructions(rs.getString("instructions"));
        recipe.setNotes(rs.getString("notes"));
        recipe.setFavorite(rs.getBoolean("favorite"));
        return recipe;
    }
}