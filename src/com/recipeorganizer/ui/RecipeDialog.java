package com.recipeorganizer.ui;

import com.recipeorganizer.dao.RecipeDAO;
import com.recipeorganizer.model.Recipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for adding or editing recipes
 */
public class RecipeDialog extends JDialog {
    private RecipeDAO recipeDAO;
    private Recipe recipe;
    private boolean saved = false;
    
    // Form fields
    private JTextField nameField;
    private JTextField categoryField;
    private JSpinner prepTimeSpinner;
    private JSpinner cookTimeSpinner;
    private JSpinner servingsSpinner;
    private JComboBox<String> difficultyComboBox;
    private JTextArea ingredientsArea;
    private JTextArea instructionsArea;
    private JTextArea notesArea;
    private JCheckBox favoriteCheckBox;
    
    // Constants
    private final String[] DIFFICULTY_LEVELS = {"Easy", "Medium", "Hard"};
    
    public RecipeDialog(JFrame parent, Recipe recipe) {
        super(parent, recipe == null ? "Add New Recipe" : "Edit Recipe", true);
        
        this.recipeDAO = new RecipeDAO();
        this.recipe = recipe;
        
        // Create and set up the form
        initComponents();
        
        // Load recipe data if editing
        if (recipe != null) {
            loadRecipeData();
        }
        
        // Configure dialog
        pack();
        setLocationRelativeTo(parent);
        setResizable(true);
        setMinimumSize(new Dimension(500, 600));
    }
    
    private void initComponents() {
        // Main panel with GridBagLayout for flexible form layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        nameField = new JTextField(30);
        mainPanel.add(nameField, gbc);
        
        // Category field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        categoryField = new JTextField(30);
        mainPanel.add(categoryField, gbc);
        
        // Prep time
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Prep Time (min):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        prepTimeSpinner = new JSpinner(new SpinnerNumberModel(15, 0, 999, 5));
        mainPanel.add(prepTimeSpinner, gbc);
        
        // Cook time
        gbc.gridx = 2;
        mainPanel.add(new JLabel("Cook Time (min):"), gbc);
        
        gbc.gridx = 3;
        cookTimeSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 999, 5));
        mainPanel.add(cookTimeSpinner, gbc);
        
        // Servings
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Servings:"), gbc);
        
        gbc.gridx = 1;
        servingsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        mainPanel.add(servingsSpinner, gbc);
        
        // Difficulty
        gbc.gridx = 2;
        mainPanel.add(new JLabel("Difficulty:"), gbc);
        
        gbc.gridx = 3;
        difficultyComboBox = new JComboBox<>(DIFFICULTY_LEVELS);
        mainPanel.add(difficultyComboBox, gbc);
        
        // Favorite checkbox
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Favorite:"), gbc);
        
        gbc.gridx = 1;
        favoriteCheckBox = new JCheckBox();
        mainPanel.add(favoriteCheckBox, gbc);
        
        // Ingredients
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Ingredients:"), gbc);
        
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        ingredientsArea = new JTextArea(10, 30);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsArea);
        mainPanel.add(ingredientsScrollPane, gbc);
        
        // Instructions
        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Instructions:"), gbc);
        
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        instructionsArea = new JTextArea(10, 30);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        mainPanel.add(instructionsScrollPane, gbc);
        
        // Notes
        gbc.gridy = 9;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("Notes:"), gbc);
        
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.5;
        notesArea = new JTextArea(5, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        mainPanel.add(notesScrollPane, gbc);
        
        // Buttons
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, gbc);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRecipe();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Add main panel to content pane
        getContentPane().add(mainPanel);
    }
    
    private void loadRecipeData() {
        nameField.setText(recipe.getName());
        categoryField.setText(recipe.getCategory());
        prepTimeSpinner.setValue(recipe.getPrepTime());
        cookTimeSpinner.setValue(recipe.getCookTime());
        servingsSpinner.setValue(recipe.getServings());
        
        // Set difficulty
        String difficulty = recipe.getDifficulty();
        if (difficulty != null && !difficulty.isEmpty()) {
            for (int i = 0; i < DIFFICULTY_LEVELS.length; i++) {
                if (DIFFICULTY_LEVELS[i].equalsIgnoreCase(difficulty)) {
                    difficultyComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        ingredientsArea.setText(recipe.getIngredients());
        instructionsArea.setText(recipe.getInstructions());
        notesArea.setText(recipe.getNotes());
        favoriteCheckBox.setSelected(recipe.isFavorite());
    }
    
    private void saveRecipe() {
        // Validate required fields
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a name for the recipe.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Get values from form fields
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        int prepTime = (Integer) prepTimeSpinner.getValue();
        int cookTime = (Integer) cookTimeSpinner.getValue();
        int servings = (Integer) servingsSpinner.getValue();
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        String ingredients = ingredientsArea.getText().trim();
        String instructions = instructionsArea.getText().trim();
        String notes = notesArea.getText().trim();
        boolean favorite = favoriteCheckBox.isSelected();
        
        boolean success = false;
        
        if (recipe == null) {
            // Create new recipe
            recipe = new Recipe(name, category, prepTime, cookTime, servings, difficulty, ingredients, instructions);
            recipe.setNotes(notes);
            recipe.setFavorite(favorite);
            
            // Save to database
            int newId = recipeDAO.addRecipe(recipe);
            success = (newId > 0);
            
        } else {
            // Update existing recipe
            recipe.setName(name);
            recipe.setCategory(category);
            recipe.setPrepTime(prepTime);
            recipe.setCookTime(cookTime);
            recipe.setServings(servings);
            recipe.setDifficulty(difficulty);
            recipe.setIngredients(ingredients);
            recipe.setInstructions(instructions);
            recipe.setNotes(notes);
            recipe.setFavorite(favorite);
            
            // Save to database
            success = recipeDAO.updateRecipe(recipe);
        }
        
        if (success) {
            saved = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save recipe. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}