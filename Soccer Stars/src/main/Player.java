
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Player {
	private int x, y;
    private double velX, velY;
    int DIAMETER = 50;
    private final double FRICTION = 0.974;
    private final double FORCE_MULTIPLIER = 0.2;
    private final double COLLISION_ELASTICITY = 0.8;
    private Color teamColor; // Nuevo: color del equipo
    private ImageIcon playerImage;
    
    public Player(int x, int y, Color teamColor, ImageIcon playerImage) {
        this.x = x;
        this.y = y;
        this.teamColor = teamColor;
        this.playerImage = playerImage;
    }
    
    // Modificar el m�todo draw para usar el color del equipo
    public void draw(Graphics g) {
        // Dibujar la imagen del jugador
        if (playerImage != null) {
        	g.drawImage(playerImage.getImage(), x, y, DIAMETER, DIAMETER, null);  // Dibujar la imagen en las coordenadas (x, y)
        } else {
            g.setColor(Color.GRAY);  // Si no hay imagen, dibujamos un c�rculo gris
            g.fillOval(x, y, DIAMETER, DIAMETER);
            g.setColor(Color.WHITE);
            g.drawOval(x, y, DIAMETER, DIAMETER);
        }
    }
    
    public boolean collidesWithPlayer(Player other) {
        // Calcula la distancia entre los centros de los jugadores
        double dx = (x + DIAMETER / 2) - (other.getX() + other.getDiameter() / 2);
        double dy = (y + DIAMETER / 2) - (other.getY() + other.getDiameter() / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Si hay colisi�n
        if (distance < (DIAMETER / 2 + other.getDiameter() / 2)) {
            // Normalizar el vector de colisi�n
            double overlap = (DIAMETER / 2 + other.getDiameter() / 2) - distance; // Cu�nto se superponen
            double nx = dx / distance; // Direcci�n normalizada en X
            double ny = dy / distance; // Direcci�n normalizada en Y

            // Factor de correcci�n: controla cu�nto se mueven los jugadores
            double correctionFactor = 0.5; // Ajusta este valor para suavizar el movimiento (0.5 mueve ambos por igual)

            // Separar a los jugadores suavemente
            x -= nx * overlap * correctionFactor;
            y -= ny * overlap * correctionFactor;
            other.setPosition(
                other.getX() + nx * overlap * (1 - correctionFactor),
                other.getY() + ny * overlap * (1 - correctionFactor)
            );

            // Ajustar las velocidades para simular p�rdida de energ�a (opcional)
            velX *= 0.5;
            velY *= 0.5;
            other.setVelocity(other.getVelX() * 0.5, other.getVelY() * 0.5);

            return true;
        }

        return false;
    }



    
    // Getters y setters necesarios
    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }

    public void setVelocity(double velX, double velY) {
            this.velX = velX;
            this.velY = velY;
    }


    public void setPosition(double x, double y) {
        this.x = (int) x;  // Casting expl�cito de double a int
        this.y = (int) y;
    }	
    
 // M�todo para obtener la posici�n X
    public int getX() {
        return (int) x;
    }

    // M�todo para obtener la posici�n Y
    public int getY() {
        return (int) y;
    }

    // M�todo para obtener el di�metro del jugador
    public int getDiameter() {
        return DIAMETER;
    }

    public void update() {
            x += (int)velX;
            y += (int)velY;

            // Aplicar fricci�n normal
            velX *= FRICTION;
            velY *= FRICTION;

        // Detener el movimiento cuando la velocidad es muy baja
        if (Math.abs(velX) < 2) velX = 0;
        if (Math.abs(velY) < 2) velY = 0;
    }
    
    
    public void shoot(double dx, double dy) {
        // Calcular la magnitud del vector
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        
        // Normalizar el vector
        double dirX = dx / magnitude;
        double dirY = dy / magnitude;
        
        // Aplicar la fuerza con el multiplicador
        this.velX = -dirX * magnitude * FORCE_MULTIPLIER;
        this.velY = -dirY * magnitude * FORCE_MULTIPLIER;
    }
   
    public boolean contains(Point p) {
        return new Rectangle(x, y, DIAMETER, DIAMETER).contains(p);
    }

    // M�todo para verificar colisi�n con la pelota
    public boolean collidesWith(Ball ball) {
        double dx = (x + DIAMETER / 2) - (ball.getX() + ball.getDiameter() / 2);
        double dy = (y + DIAMETER / 2) - (ball.getY() + ball.getDiameter() / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (DIAMETER / 2 + ball.getDiameter() / 2);
    }

    public void handleCollision(Ball ball) {
        // Obtener los centros
        double playerCenterX = x + DIAMETER / 2.0;
        double playerCenterY = y + DIAMETER / 2.0;
        double ballCenterX = ball.getX() + ball.getDiameter() / 2.0;
        double ballCenterY = ball.getY() + ball.getDiameter() / 2.0;

        // Calcular vector de colisi�n
        double dx = ballCenterX - playerCenterX;
        double dy = ballCenterY - playerCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return; // Evitar divisi�n por cero

        // Normalizar el vector de colisi�n
        dx /= distance;
        dy /= distance;

        // Calcular velocidades relativas
        double relativeVelX = ball.getVelX() - velX;
        double relativeVelY = ball.getVelY() - velY;

        // Calcular producto punto de velocidad relativa y normal de colisi�n
        double dotProduct = relativeVelX * dx + relativeVelY * dy;

        // Calcular impulso
        double impulseMagnitude = -(1 + COLLISION_ELASTICITY) * dotProduct;
        
        // Calcular efecto basado en el punto de impacto y la velocidad relativa
        double spinFactor = (relativeVelX * dy - relativeVelY * dx) * 0.1;
        
        // Aplicar efecto a la pelota
        ball.addSpin(spinFactor);

        // Calcular nuevas velocidades
        double newBallVelX = ball.getVelX() + dx * impulseMagnitude;
        double newBallVelY = ball.getVelY() + dy * impulseMagnitude;

        // Aplicar las nuevas velocidades
        ball.setVelocity(newBallVelX, newBallVelY);

        // Separar los objetos para evitar que se peguen
        double overlap = (DIAMETER/2 + ball.getDiameter()/2) - distance;
        if (overlap > 0) {
            ball.setPosition(
                ball.getX() + dx * overlap,
                ball.getY() + dy * overlap
            );
        }
    }
    
 // M�todo adicional para agregar efecto intencionalmente
    public void addSpinToBall(Ball ball, double hitAngle) {
        // Calcular el efecto basado en el �ngulo de golpe
        double spinAmount = Math.sin(hitAngle) * 3.0; // 5.0 es la cantidad m�xima de efecto
        ball.addSpin(spinAmount);
    }

    public void checkWallCollision(int width, int height) {
        double reboundFactor = 0.5; // Reduce la velocidad en cada rebote (entre 0 y 1)

        if (x < 0) {
            x = 0;
            velX = -velX * reboundFactor;
        } else if (x + DIAMETER > width) {
            x = width - DIAMETER;
            velX = -velX * reboundFactor;
        }

        if (y < 0) {
            y = 0;
            velY = -velY * reboundFactor;
        } else if (y + DIAMETER > height) {
            y = height - DIAMETER;
            velY = -velY * reboundFactor;
        }
    }
    
    // M�todo para verificar si el jugador est� est�tico
    public boolean isStatic() {
        return Math.abs(velX) < 0.1 && Math.abs(velY) < 0.1;
    }
    
    // M�todo para resetear la posici�n
    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        this.velX = 0;
        this.velY = 0;
    }
    
    public void handlePostCollision() {
        // Factor de rebote
        double reboundFactor = 0.8; // Entre 0 y 1

        // Invertir la direcci�n de la velocidad y reducirla (aplicar rebote)
        this.velX = -this.velX * reboundFactor;
        this.velY = -this.velY * reboundFactor;

        // Ajustar la posici�n para evitar que el jugador se quede pegado al poste
        // Esto es opcional, dependiendo de c�mo quieres manejar las colisiones (eliminar la "pegajosidad")
        // Esta es una forma de separarlo despu�s de la colisi�n
        if (x < 0) {
            x = 0;
        } else if (x + DIAMETER > 800) { // Suponiendo que el ancho de la pantalla es 800
            x = 800 - DIAMETER;
        }

        if (y < 0) {
            y = 0;
        } else if (y + DIAMETER > 600) { // Suponiendo que la altura de la pantalla es 600
            y = 600 - DIAMETER;
        }
    }


    
    public void move() {
        // M�todo para actualizar posici�n en base a la velocidad (si es necesario)
    }
}