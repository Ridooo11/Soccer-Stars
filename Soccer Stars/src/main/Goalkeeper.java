package main;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;

public class Goalkeeper extends Player {
    
    public Goalkeeper(int x, int y, Color teamColor, ImageIcon playerImage, int DIAMETER) {
        super(x, y, teamColor, playerImage);  // Llamar al constructor de Player
        this.DIAMETER = DIAMETER;  // Establecer un di�metro mayor para el arquero
    }

    // M�todos espec�ficos del arquero pueden ir aqu� si es necesario.
    // Por ejemplo, agregar funcionalidades como moverse solo en la l�nea de gol, etc.

    // Sobrescribir el m�todo de dibujo si se necesita un estilo de dibujo espec�fico
    @Override
    public void draw(Graphics g) {
        // Llamar al m�todo de la clase base
        super.draw(g);
    }
}