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



/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {
private static float xrot = 0.0f, yrot = 0.0f;
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

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glEnable(GL.GL_CULL_FACE);
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
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok�? osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok�? osi Y
          // Move the "drawing cursor" around
 //gl.glTranslatef(-1.5f, 1.0f, -6.0f);
         //gl.glTranslatef(-1.5f, 1.0f, -6.0f);
        /*float x,y,kat;
gl.glBegin(GL.GL_TRIANGLE_FAN);
gl.glVertex3f(0.0f,0.0f,-6.0f); //�rodek
for(kat = 0.0f; kat < (2.0f*Math.PI);
kat+=(Math.PI/32.0f))
{
x = 2.5f*(float)Math.sin(kat); // zmiana rozmiaru kola
y = 2.5f*(float)Math.cos(kat); //
gl.glVertex3f(x, y, -6.0f); //kolejne punkty
}
gl.glEnd(); */

 gl.glBegin(GL.GL_QUADS);
 //?ciana przednia
 gl.glColor3f(1.0f,0.0f,0.0f);
 gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glVertex3f(1.0f,-1.0f,1.0f);
 gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glVertex3f(-1.0f,1.0f,1.0f);
 //sciana tylnia
 gl.glColor3f(0.0f,1.0f,0.0f);
 gl.glVertex3f(-1.0f,1.0f,-1.0f);
 gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glVertex3f(1.0f,-1.0f,-1.0f);
 gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 //?ciana lewa
 gl.glColor3f(0.0f,0.0f,1.0f);
 gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glVertex3f(-1.0f,1.0f,1.0f);
 gl.glVertex3f(-1.0f,1.0f,-1.0f);
 //?ciana prawa
 gl.glColor3f(1.0f,1.0f,0.0f);
 gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glVertex3f(1.0f,-1.0f,1.0f);
 gl.glVertex3f(1.0f,-1.0f,-1.0f);
 //?ciana dolna
 gl.glColor3f(1.0f,0.0f,1.0f);
 gl.glVertex3f(-1.0f,-1.0f,1.0f);
 gl.glVertex3f(-1.0f,-1.0f,-1.0f);
 gl.glVertex3f(1.0f,-1.0f,-1.0f);
 gl.glVertex3f(1.0f,-1.0f,1.0f);
 //?ciana g�rna
 gl.glColor3f(1.0f,2.0f,1.0f);
 gl.glVertex3f(1.0f,1.0f,-1.0f);
 gl.glVertex3f(-1.0f,1.0f,-1.0f);
 gl.glVertex3f(-1.0f,1.0f,1.0f);
 gl.glVertex3f(1.0f,1.0f,1.0f);
 gl.glEnd(); 

        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

