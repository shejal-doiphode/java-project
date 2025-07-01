package com.recipeorganizer.ui;

import com.recipeorganizer.dao.RecipeDAO;
import com.recipeorganizer.model.Recipe;
import com.recipeorganizer.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Main application class for the Recipe Organizer
 */
public class RecipeOrganizerApp extends JFrame {
    private RecipeDAO recipeDAO;
    private JTable recipeTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilterComboBox;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, favoriteButton;
    private JPanel recipeDetailsPanel;
    private JTextArea ingredientsTextArea, instructionsTextArea;
    private Recipe currentRecipe;
    
    // Table column names
    private final String[] columnNames = {"ID", "Name", "Category", "Total Time", "Difficulty", "Favorite"};
    
    public RecipeOrganizerApp() {
        recipeDAO = new RecipeDAO();
        
        // Set up the main frame
        setTitle("Food Recipe Organizer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add window closing listener to close database connection
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
                System.exit(0);
            }
        });
        
        // Create UI components
        initUI();
        
        // Load initial data
        loadRecipes();
        
        // Display the frame
        setVisible(true);
    }
    
    private void initUI() {
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel for search and filters
        JPanel topPanel = createTopPanel();
        
        // Center panel with recipe list and details
        JSplitPane centerSplitPane = createCenterPanel();
        
        // Bottom panel with buttons
        JPanel bottomPanel = createBottomPanel();
        
        // Add panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Set the main panel as the content pane
        setContentPane(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Search panel (left side)
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRecipes());
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Filter panel (right side)
        JPanel filterPanel = new JPanel(new BorderLayout());
        categoryFilterComboBox = new JComboBox<>();
        categoryFilterComboBox.addItem("All Categories");
        // Load categories from database
        List<String> categories = recipeDAO.getAllCategories();
        for (String category : categories) {
            categoryFilterComboBox.addItem(category);
        }
        categoryFilterComboBox.addActionListener(e -> filterRecipesByCategory());
        filterPanel.add(new JLabel("Category: "), BorderLayout.WEST);
        filterPanel.add(categoryFilterComboBox, BorderLayout.CENTER);
        
        // Add search and filter panels to top panel
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JSplitPane createCenterPanel() {
        // Create table model and table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class; // For favorite column
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        recipeTable = new JTable(tableModel);
        recipeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeTable.getTableHeader().setReorderingAllowed(false);
        recipeTable.getColumnModel().getColumn(0).setMaxWidth(50); // ID column
        recipeTable.getColumnModel().getColumn(3).setMaxWidth(100); // Time column
        recipeTable.getColumnModel().getColumn(4).setMaxWidth(100); // Difficulty column
        recipeTable.getColumnModel().getColumn(5).setMaxWidth(70); // Favorite column
        
        // Add selection listener
        recipeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = recipeTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        int recipeId = (Integer) tableModel.getValueAt(selectedRow, 0);
                        loadRecipeDetails(recipeId);
                    }
                }
            }
        });
        
        // Create recipe list scroll pane
        JScrollPane tableScrollPane = new JScrollPane(recipeTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 300));
        
        // Create recipe details panel
        recipeDetailsPanel = createRecipeDetailsPanel();
        JScrollPane detailsScrollPane = new JScrollPane(recipeDetailsPanel);
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, detailsScrollPane);
        splitPane.setResizeWeight(0.4);
        
        return splitPane;
    }
    
    private JPanel createRecipeDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create tabs for different sections
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        JPanel infoGridPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel nameLabel = new JLabel("Name:");
        JLabel nameValueLabel = new JLabel();
        
        JLabel categoryLabel = new JLabel("Category:");
        JLabel categoryValueLabel = new JLabel();
        
        JLabel timeLabel = new JLabel("Time:");
        JLabel timeValueLabel = new JLabel();
        
        JLabel difficultyLabel = new JLabel("Difficulty:");
        JLabel difficultyValueLabel = new JLabel();
        
        JLabel servingsLabel = new JLabel("Servings:");
        JLabel servingsValueLabel = new JLabel();
        
        JLabel notesLabel = new JLabel("Notes:");
        JTextArea notesTextArea = new JTextArea();
        notesTextArea.setEditable(false);
        notesTextArea.setLineWrap(true);
        notesTextArea.setWrapStyleWord(true);
        JScrollPane notesScrollPane = new JScrollPane(notesTextArea);
        
        infoGridPanel.add(nameLabel);
        infoGridPanel.add(nameValueLabel);
        infoGridPanel.add(categoryLabel);
        infoGridPanel.add(categoryValueLabel);
        infoGridPanel.add(timeLabel);
        infoGridPanel.add(timeValueLabel);
        infoGridPanel.add(difficultyLabel);
        infoGridPanel.add(difficultyValueLabel);
        infoGridPanel.add(servingsLabel);
        infoGridPanel.add(servingsValueLabel);
        
        infoPanel.add(infoGridPanel, BorderLayout.NORTH);
        infoPanel.add(new JLabel("Notes:"), BorderLayout.CENTER);
        infoPanel.add(notesScrollPane, BorderLayout.SOUTH);
        
        // Create ingredients panel
        JPanel ingredientsPanel = new JPanel(new BorderLayout());
        ingredientsTextArea = new JTextArea();
        ingredientsTextArea.setEditable(false);
        ingredientsTextArea.setLineWrap(true);
        ingredientsTextArea.setWrapStyleWord(true);
        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTextArea);
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);
        
        // Create instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsTextArea = new JTextArea();
        instructionsTextArea.setEditable(false);
        instructionsTextArea.setLineWrap(true);
        instructionsTextArea.setWrapStyleWord(true);
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsTextArea);
        instructionsPanel.add(instructionsScrollPane, BorderLayout.CENTER);
        
        // Add tabs
        tabbedPane.addTab("Info", infoPanel);
        tabbedPane.addTab("Ingredients", ingredientsPanel);
        tabbedPane.addTab("Instructions", instructionsPanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = new JButton("Add Recipe");
        editButton = new JButton("Edit Recipe");
        deleteButton = new JButton("Delete Recipe");
        favoriteButton = new JButton("Toggle Favorite");
        
        // Add button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRecipeDialog(null);
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = recipeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    openRecipeDialog(currentRecipe);
                } else {
                    JOptionPane.showMessageDialog(
                            RecipeOrganizerApp.this,
                            "Please select a recipe to edit.",
                            "No Recipe Selected",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRecipe();
            }
        });
        
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFavorite();
            }
        });
        
        // Initially disable edit, delete, and favorite buttons
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        favoriteButton.setEnabled(false);
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(favoriteButton);
        
        return panel;
    }
    
    private void loadRecipes() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get recipes from database
        List<Recipe> recipes = recipeDAO.getAllRecipes();
        
        // Add recipes to table model
        for (Recipe recipe : recipes) {
            addRecipeToTable(recipe);
        }
    }
    
    private void addRecipeToTable(Recipe recipe) {
        tableModel.addRow(new Object[]{
                recipe.getId(),
                recipe.getName(),
                recipe.getCategory(),
                recipe.getTotalTime() + " min",
                recipe.getDifficulty(),
                recipe.isFavorite()
        });
    }
    
    private void loadRecipeDetails(int recipeId) {
        currentRecipe = recipeDAO.getRecipeById(recipeId);
        
        if (currentRecipe != null) {
            // Enable buttons
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            favoriteButton.setEnabled(true);
            
            // Set recipe details
            ingredientsTextArea.setText(currentRecipe.getIngredients());
            instructionsTextArea.setText(currentRecipe.getInstructions());
        }
    }
    
    private void openRecipeDialog(Recipe recipe) {
        RecipeDialog dialog = new RecipeDialog(this, recipe);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            // Refresh the recipe list
            loadRecipes();
            
            // Update categories in filter
            updateCategoryFilter();
        }
    }
    
    private void deleteSelectedRecipe() {
        int selectedRow = recipeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int recipeId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this recipe?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            
            if (option == JOptionPane.YES_OPTION) {
                boolean deleted = recipeDAO.deleteRecipe(recipeId);
                if (deleted) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Recipe deleted successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Refresh the recipe list
                    loadRecipes();
                    
                    // Clear details panel
                    ingredientsTextArea.setText("");
                    instructionsTextArea.setText("");
                    currentRecipe = null;
                    
                    // Disable buttons
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    favoriteButton.setEnabled(false);
                    
                    // Update categories in filter
                    updateCategoryFilter();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete recipe.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
    
    private void toggleFavorite() {
        if (currentRecipe != null) {
            boolean newFavoriteStatus = !currentRecipe.isFavorite();
            boolean updated = recipeDAO.toggleFavorite(currentRecipe.getId(), newFavoriteStatus);
            
            if (updated) {
                currentRecipe.setFavorite(newFavoriteStatus);
                
                // Update table
                int selectedRow = recipeTable.getSelectedRow();
                tableModel.setValueAt(newFavoriteStatus, selectedRow, 5);
            }
        }
    }
    
    private void searchRecipes() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadRecipes();
        } else {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Search recipes
            List<Recipe> recipes = recipeDAO.searchRecipes(searchTerm);
            
            // Add matching recipes to table
            for (Recipe recipe : recipes) {
                addRecipeToTable(recipe);
            }
        }
    }
    
    private void filterRecipesByCategory() {
        String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
        
        if (selectedCategory == null || selectedCategory.equals("All Categories")) {
            loadRecipes();
        } else {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Filter recipes by category
            List<Recipe> recipes = recipeDAO.getRecipesByCategory(selectedCategory);
            
            // Add matching recipes to table
            for (Recipe recipe : recipes) {
                addRecipeToTable(recipe);
            }
        }
    }
    
    private void updateCategoryFilter() {
        String currentSelection = (String) categoryFilterComboBox.getSelectedItem();
        
        // Clear and reload categories
        categoryFilterComboBox.removeAllItems();
        categoryFilterComboBox.addItem("All Categories");
        
        // Add categories from database
        List<String> categories = recipeDAO.getAllCategories();
        for (String category : categories) {
            categoryFilterComboBox.addItem(category);
        }
        
        // Restore previous selection if it still exists
        if (currentSelection != null) {
            categoryFilterComboBox.setSelectedItem(currentSelection);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RecipeOrganizerApp();
            }
        });
    }
}