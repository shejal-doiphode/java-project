package com.recipeorganizer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe model class representing a food recipe
 */
public class Recipe {
    private int id;
    private String name;
    private String category;
    private int prepTime; // in minutes
    private int cookTime; // in minutes
    private int servings;
    private String difficulty; // easy, medium, hard
    private String ingredients;
    private String instructions;
    private String notes;
    private boolean favorite;
    
    // Constructors
    public Recipe() {
    }
    
    public Recipe(String name, String category, int prepTime, int cookTime, 
                  int servings, String difficulty, String ingredients, 
                  String instructions) {
        this.name = name;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.favorite = false;
    }
    
    // Constructor with ID for database retrieval
    public Recipe(int id, String name, String category, int prepTime, int cookTime, 
                 int servings, String difficulty, String ingredients, 
                 String instructions, String notes, boolean favorite) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.notes = notes;
        this.favorite = favorite;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    
    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    
    public int getTotalTime() {
        return prepTime + cookTime;
    }
    
    @Override
    public String toString() {
        return name;
    }
}