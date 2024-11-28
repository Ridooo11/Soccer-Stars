package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Menu extends JPanel {
    private static final long serialVersionUID = 1L;
    private Dimension currentResolution;
    private JButton playButton, volumeButton, resolutionButton;
    private BufferedImage backgroundImage;

    // Mapa para almacenar informaci�n de los equipos
    private static final Map<String, TeamInfo> TEAM_INFO = new HashMap<>();
    
    // Clase interna para almacenar informaci�n de cada equipo
    private static class TeamInfo {
        String fullName;
        String flagPath;
        
        TeamInfo(String fullName, String flagPath) {
            this.fullName = fullName;
            this.flagPath = flagPath;
        }
    }
    
    // Inicializar datos de los equipos
    static {
        TEAM_INFO.put("Argentina", new TeamInfo("Argentina", "/resources/Flag_of_Argentina.png"));
        TEAM_INFO.put("Brasil", new TeamInfo("Brasil", "/resources/Flag_of_Brazil.svg.png"));
        TEAM_INFO.put("Nueva Zelanda", new TeamInfo("Nueva Zelanda", "/resources/Flag_of_New_Zealand.svg.png"));
        TEAM_INFO.put("Palestina", new TeamInfo("Palestina", "/resources/Flag_of_Palestine.svg.png"));
    }

    private String team1 = ""; // Equipo del Jugador 1
    private String team2 = ""; // Equipo del Jugador 2

    public Menu() {
    	setResolution(new Dimension(1280, 800)); // Resoluci�n inicial
    	setLayout(new GridBagLayout());
    	setBackground(new Color(34, 139, 34)); // Fondo verde estilo campo de f�tbol

    	try {
    	backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/fondoJuego.png"));
    	} catch (IOException e) {
    	e.printStackTrace();
    	backgroundImage = null;
    	}

    	// Resto del constructor original
    	setResolution(new Dimension(1280, 800)); // Resoluci�n inicial
    	setLayout(new GridBagLayout());

    	// Si no se puede cargar la imagen, mantener el color de fondo original
    	if (backgroundImage == null) {
    	setBackground(new Color(34, 139, 34)); // Fondo verde estilo campo de f�tbol
    	}

    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.insets = new Insets(20, 20, 20, 20);
    	gbc.gridx = 0;
    	gbc.weightx = 1.0;
    	gbc.weighty = 1.0;
    	gbc.fill = GridBagConstraints.BOTH;

    	// T�tulo del juego
    	JLabel title = new JLabel("Soccer Stars", SwingConstants.CENTER);
    	title.setFont(new Font("Briquette", Font.BOLD, 48));
    	title.setForeground(Color.WHITE);
    	gbc.gridy = 0;
    	add(title, gbc);

    	// Bot�n Jugar
    	gbc.gridy = 1;
    	playButton = createButton("JUGAR");
    	playButton.addActionListener(e -> showTeamSelectionWindow());
    	add(playButton, gbc);


    	// Bot�n Configuraci�n de Resoluci�n
    	gbc.gridy = 3;
    	resolutionButton = createButton("TUTORIAL");
    	resolutionButton.addActionListener(e -> showTutorialWindow());
    	add(resolutionButton, gbc);
    	}
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar imagen de fondo si est� disponible
        if (backgroundImage != null) {
            // Escalar la imagen para que cubra todo el panel
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Briquette", Font.BOLD, 24));
        button.setForeground(new Color(252, 252, 247));
        button.setBackground(new Color(0, 102, 0)); // Verde oscuro para botones
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JFrame createStyledFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(34, 139, 34)); // Fondo verde campo de f�tbol
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private void setResolution(Dimension resolution) {
        currentResolution = resolution;
        setPreferredSize(currentResolution);
    }


    private void showTutorialWindow() {
        JFrame tutorialFrame = createStyledFrame("Tutorial de Soccer Stars");
        tutorialFrame.setSize(700, 500);

        // Array de informaci�n de tutoriales
        TutorialPage[] tutorialPages = {
            new TutorialPage(
                "/resources/tutorial1.png", 
                "Selecciona tu equipo favorito para competir contra tu amigo"
            ),
            new TutorialPage(
                "/resources/tutorial2.png", 
                "Lanza al jugador para intentar hacer un gol"
            ),
            new TutorialPage(
                "/resources/tutorial3.png", 
                "Buena suerte y compite para ser el mejor"
            ),
        };

        // Panel principal del tutorial
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34));

        // Imagen del tutorial
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(600, 300));

        // T�tulo del tutorial
        JLabel titleLabel = new JLabel(tutorialPages[0].title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Botones de navegaci�n
        JButton prevButton = createButton("Anterior");
        JButton nextButton = createButton("Siguiente");

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(34, 139, 34));
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        // Configuraci�n inicial
        int[] currentPage = {0};
        updateTutorialPage(tutorialPages, currentPage, imageLabel, titleLabel);

        // Configuraci�n de los botones
        prevButton.addActionListener(e -> {
            if (currentPage[0] > 0) {
                currentPage[0]--;
                updateTutorialPage(tutorialPages, currentPage, imageLabel, titleLabel);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage[0] < tutorialPages.length - 1) {
                currentPage[0]++;
                updateTutorialPage(tutorialPages, currentPage, imageLabel, titleLabel);
            } else {
                tutorialFrame.dispose();
            }
        });

        // A�adir componentes al panel principal
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        tutorialFrame.add(mainPanel);
        tutorialFrame.setVisible(true);
    }

    // M�todo para actualizar la p�gina del tutorial
    private void updateTutorialPage(TutorialPage[] tutorialPages, int[] currentPage, JLabel imageLabel, JLabel titleLabel) {
        // Cargar la imagen
        ImageIcon icon = new ImageIcon(getClass().getResource(tutorialPages[currentPage[0]].imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        
        // Actualizar el t�tulo
        titleLabel.setText(tutorialPages[currentPage[0]].title);
    }

    // Clase para almacenar informaci�n de p�ginas de tutorial
    private static class TutorialPage {
        String imagePath;
        String title;

        TutorialPage(String imagePath, String title) {
            this.imagePath = imagePath;
            this.title = title;
        }
    }

    private void showTeamSelectionWindow() {
        JFrame teamFrame = createStyledFrame("Selecci�n de Equipos");

        String[] teams = {"Argentina", "Nueva Zelanda", "Brasil", "Palestina"};
        
        // Personalizar ComboBox para mostrar banderas y nombres completos
        class TeamRenderer extends DefaultListCellRenderer {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, 
                boolean isSelected, boolean cellHasFocus) {
                
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                panel.setBackground(isSelected ? 
                    new Color(100, 200, 100) : new Color(34, 139, 34));
                
                if (value instanceof String teamName) {
                    TeamInfo info = TEAM_INFO.get(teamName);
                    
                    // Cargar bandera
                    ImageIcon flagIcon = new ImageIcon(getClass().getResource(info.flagPath));
                    Image scaledFlag = flagIcon.getImage().getScaledInstance(35, 15, Image.SCALE_SMOOTH);
                    JLabel flagLabel = new JLabel(new ImageIcon(scaledFlag));
                    
                    // Etiqueta de nombre completo
                    JLabel nameLabel = new JLabel(info.fullName);
                    nameLabel.setForeground(Color.WHITE);
                    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    panel.add(flagLabel);
                    panel.add(Box.createHorizontalStrut(10)); // Espaciado entre bandera y texto
                    panel.add(nameLabel);
                }
                
                return panel;
            }
        }
        
        JComboBox<String> teamSelection1 = new JComboBox<>(teams);
        JComboBox<String> teamSelection2 = new JComboBox<>(teams);
        
        // Aplicar el renderer personalizado
        teamSelection1.setRenderer(new TeamRenderer());
        teamSelection2.setRenderer(new TeamRenderer());

        // Crear panel de selecci�n
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(34, 139, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y selecci�n del Jugador 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createStyledLabel("Jugador 1:"), gbc);
        
        gbc.gridx = 1;
        panel.add(teamSelection1, gbc);

        // Etiqueta y selecci�n del Jugador 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createStyledLabel("Jugador 2:"), gbc);
        
        gbc.gridx = 1;
        panel.add(teamSelection2, gbc);

        teamFrame.add(panel, BorderLayout.CENTER);

        // Bot�n CONTINUAR
        JButton continueButton = createButton("CONTINUAR");
        continueButton.setPreferredSize(new Dimension(0, 50)); // Altura fija para ocupar todo el ancho
        continueButton.addActionListener(e -> {
            team1 = (String) teamSelection1.getSelectedItem();
            team2 = (String) teamSelection2.getSelectedItem();

            if (team1.equals(team2)) {
                JOptionPane.showMessageDialog(this, 
                    "Selecciona equipos diferentes.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } else {
                teamFrame.dispose();
                startGame();
            }
        });
        teamFrame.add(continueButton, BorderLayout.SOUTH);

        teamFrame.setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void startGame() {
        JFrame gameFrame = new JFrame("Soccer Stars");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pasar equipos seleccionados al GamePanel
        GamePanel gamePanel = new GamePanel(currentResolution.width, currentResolution.height, team1, team2);
        gameFrame.add(gamePanel);

        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);

        gamePanel.startGame();

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            topFrame.dispose(); // Cerrar el men�
        }
    }
}