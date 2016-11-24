package org.yourorghere;
 
import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
 
 
 
/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {
static BufferedImage image1 = null,image2 = null, image3 = null;
static Texture t1 = null, t2 = null , t3 = null;
static Koparka koparka;
private static float xrot = 0.0f, yrot = 0.0f;
static float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };//swiat?o otaczajšce
static float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };//?wiat?o rozproszone
static float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
static float lightPos[] = { 0.0f, 150.0f, 150.0f, 1.0f };//pozycja ?wiat?a
static float x = 0.f, z = 0.0f;
 
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();
 
        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
 
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
 
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        
        //Obs?uga klawiszy strza?ek
  frame.addKeyListener(new KeyListener()
  {
  public void keyPressed(KeyEvent e)
  {
  if(e.getKeyCode() == KeyEvent.VK_UP)
  xrot -= 1.0f;
  if(e.getKeyCode() == KeyEvent.VK_DOWN)
  xrot +=1.0f;
  if(e.getKeyCode() == KeyEvent.VK_RIGHT)
  yrot += 1.0f;
  if(e.getKeyCode() == KeyEvent.VK_LEFT)
  yrot -=1.0f;
  if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
      przesun(-1.0f);
  if(e.getKeyCode() == KeyEvent.VK_SPACE)
      przesun(1.0f);
  
  
  if (e.getKeyChar() == 'q')
  ambientLight = new float [] {ambientLight[0]-0.1f, ambientLight[0]-0.1f, ambientLight[0]-0.1f, ambientLight[0]-0.1f,1};
  if (e.getKeyChar() == 'w')
  ambientLight = new float [] {ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f, ambientLight[0]+0.1f,1};
  if (e.getKeyChar() == 'e')
  diffuseLight = new float [] {diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f, diffuseLight[0]-0.1f,1};
  if (e.getKeyChar() == 'r')
  diffuseLight = new float [] {diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f, diffuseLight[0]+0.1f,1};
  if (e.getKeyChar() == 'a')
  specular = new float [] {specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f, specular[0]-0.1f,1};
  if (e.getKeyChar() == 's')
  specular = new float [] {specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f, specular[0]+0.1f,1};
  if (e.getKeyChar() == 'd')
  lightPos = new float [] {lightPos[0]-0.1f, lightPos[0]-0.1f, lightPos[0]-0.1f, lightPos[0]-0.1f,1};
  if (e.getKeyChar() == 'f')
  lightPos = new float [] {lightPos[0]+0.1f, lightPos[0]+0.1f, lightPos[0]+0.1f, lightPos[0]+0.1f,1};
  
  if(e.getKeyChar() == '1') 
      koparka.kat1 -= 1.0f;
  if(e.getKeyChar() == '2') 
      koparka.kat1 += 1.0f;
  if(e.getKeyChar() == '3') 
      koparka.kat2 += 1.0f;
  if(e.getKeyChar() == '4') 
      koparka.kat2 -= 1.0f;
  if(e.getKeyChar() == '5') 
      koparka.kat3 -= 1.0f;
  if(e.getKeyChar() == '6') 
      koparka.kat3 += 1.0f;
  if(e.getKeyChar() == '7') 
      koparka.kat4 += 1.0f;
  if(e.getKeyChar() == '8') 
      koparka.kat4 -= 1.0f;
  
  }
  public void keyReleased(KeyEvent e){}
  public void keyTyped(KeyEvent e){}
  });
 
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }
 
    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));
 
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        
        koparka = new Koparka();
        // Enable VSync
        gl.setSwapInterval(1);
 
       gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametrów ?ród?a ?wiat?a nr. 0
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
       
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?ród?a ?wiat?a nr. 0
        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolorów
        //kolory b?dš ustalane za pomocš glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiektów
        float specref[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR,specref,0);
        
        gl.glMateriali(GL.GL_FRONT,GL.GL_SHININESS,128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        
        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glEnable(GL.GL_CULL_FACE);
    
         try
 {
 image1 = ImageIO.read(getClass().getResourceAsStream("/bok.jpg"));
 image2 = ImageIO.read(getClass().getResourceAsStream("/trawa.jpg"));
 image3 = ImageIO.read(getClass().getResourceAsStream("/niebo.jpg"));
 }
 catch(Exception exc)
 {
 JOptionPane.showMessageDialog(null, exc.toString());
 return;
 }
 
 t1 = TextureIO.newTexture(image1, false);
 t2 = TextureIO.newTexture(image2, false);
 t3 = TextureIO.newTexture(image3, false);
 
 gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_BLEND | GL.GL_MODULATE);
 gl.glEnable(GL.GL_TEXTURE_2D);
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
}
    
 
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
 
        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(90.0f, h, 1.0, 300.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
 
     void Rysuj(GL gl, Texture t1, Texture t2, Texture t3)
 {
//szescian
gl.glColor3f(1.0f,1.0f,1.0f);
//za³adowanie tekstury wczytanej wczeœniej z pliku krajobraz.bmp
gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());

gl.glBegin(GL.GL_QUADS);
//œciana przednia
gl.glNormal3f(0.0f,0.0f,-1.0f);
gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,100.0f);
gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
//œciana tylnia
gl.glNormal3f(0.0f,0.0f,1.0f);
gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
//œciana lewa
gl.glNormal3f(1.0f,0.0f,0.0f);
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
//œciana prawa
gl.glNormal3f(-1.0f,0.0f,0.0f);
gl.glTexCoord2f(0.0f, 0.7f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
gl.glTexCoord2f(0.7f, 0.7f);gl.glVertex3f(100.0f,-100.0f,100.0f);
gl.glTexCoord2f(0.7f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
gl.glEnd();

//œciana dolna
//za³adowanie tekstury wczytanej wczeœniej z pliku niebo.bmp
 gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
 //ustawienia aby tekstura siê powiela³a
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
 gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
gl.glBegin(GL.GL_QUADS);
gl.glNormal3f(0.0f,1.0f,0.0f);
 //koordynaty ustawienia 16 x 16 kwadratów powielonej tekstury na œcianie dolnej
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,-100.0f,100.0f);
gl.glTexCoord2f(0.0f, 16.0f);gl.glVertex3f(100.0f,-100.0f,-100.0f);
gl.glTexCoord2f(16.0f, 16.0f);gl.glVertex3f(-100.0f,-100.0f,-100.0f);
gl.glTexCoord2f(16.0f, 0.0f);gl.glVertex3f(-100.0f,-100.0f,100.0f);
gl.glEnd();

 //œciana gorna
//za³adowanie tekstury wczytanej wczeœniej z pliku trawa.bmp
gl.glBindTexture(GL.GL_TEXTURE_2D, t3.getTextureObject());
gl.glBegin(GL.GL_QUADS);
gl.glNormal3f(0.0f,-1.0f,0.0f);
gl.glTexCoord2f(0.0f, 1.0f);gl.glVertex3f(-100.0f,100.0f,100.0f);
gl.glTexCoord2f(1.0f, 1.0f);gl.glVertex3f(-100.0f,100.0f,-100.0f);
gl.glTexCoord2f(1.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,-100.0f);
gl.glTexCoord2f(0.0f, 0.0f);gl.glVertex3f(100.0f,100.0f,100.0f);
gl.glEnd();
 }
 
     public static void przesun(float d){
         if (x < 100 )
         {
          x-=d*Math.sin(yrot*(3.14f/180.0f));
          z+=d*Math.cos(yrot*(3.14f/180.0f));
         }
         
         
         }
     
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
 
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_AMBIENT,ambientLight,0); //swiat?o otaczajšce
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_DIFFUSE,diffuseLight,0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_SPECULAR,specular,0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0,GL.GL_POSITION,lightPos,0); //pozycja ?wiat?a
                
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();
 
       // gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó? osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó? osi Y
        
        gl.glTranslatef(x, 0.0f, z);
        
        gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
          // Move the "drawing cursor" around
 //gl.glTranslatef(-1.5f, 1.0f, -6.0f);
         //gl.glTranslatef(-1.5f, 1.0f, -6.0f);
        /*float x,y,kat;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f,0.0f,-6.0f); //?rodek
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = 2.5f*(float)Math.sin(kat); // zmiana rozmiaru kola
y = 2.5f*(float)Math.cos(kat); //
gl.glVertex3f(x, y, -6.0f); //kolejne punkty
}
gl.glEnd(); */
 gl.glTranslatef(0.0f, 97.0f, 0.0f);
 Rysuj(gl,t1,t2,t3);
 gl.glTranslatef(0.0f, -97.0f, 0.0f);
 gl.glScalef(2.0f,2.0f,2.0f);
 koparka.Rysuj(gl);
/* gl.glBegin(GL.GL_QUADS);
 //?ciana przednia
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
 //sciana tylnia
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 gl.glEnd();
 gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
 gl.glBegin(GL.GL_QUADS);
 //?ciana lewa
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
 //?ciana prawa
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
 //?ciana dolna
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,-1.0f,-1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,-1.0f,1.0f);
 //?ciana górna
 gl.glNormal3f(0.0f, 0.0f, 1.0f);
 gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f,1.0f,-1.0f);
 gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,1.0f,1.0f);
 gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glEnd(); 
 */
        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }
 
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
 