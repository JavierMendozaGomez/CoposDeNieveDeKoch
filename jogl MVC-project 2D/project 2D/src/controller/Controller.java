//////////////////////////////////////////// 
// Project skeleton for Computer 2D Graphics
// MVC-based design
// Author: Chus Martï¿½n
// 2014
//////////////////////////////////////////// 

package controller;

//JOGL imports
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

//AWT imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



//Specific imports
import model.Scene;

public class Controller implements GLEventListener, KeyListener, MouseListener{

	private Scene scene; // The model which is controlled  
	private GLAutoDrawable canvas; // The viewport that is used to render the model
	private final GLU glu = new GLU(); //This object is kept for invoking gluOrtho2D in update_PROJECTION_MATRIX	
	private boolean baldosas;
	private int numeroColumnas;


	public Controller(GLAutoDrawable canvas1){
		System.out.print("Into Controller's constructor\n\n");

		canvas= canvas1;	
		double xLeft, xRight, yTop, yBottom;
		xLeft=0; xRight= (double)canvas.getWidth();
		yBottom=0;  yTop= (double)canvas.getHeight();
		scene= new Scene(xLeft, xRight, yTop, yBottom); //Initialize the scene size with the viewport size 

		System.out.print("Scene bounds:\n");
		System.out.print("xLeft:  \t" + xLeft +   " xRight:\t" +  xRight + "\n");
		System.out.print("yBottom:\t" + yBottom + " yTop:  \t" +  yTop + "\n");
		System.out.print("\n");

		this.baldosas = false;

	}


	/////////////////////////////////
	// GLEventListener implementation
	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.print("Into init\n\n");

		GL2 gl = drawable.getGL().getGL2();

		gl.glClearColor(1, 1, 1, 1);
		gl.glColor3f(0.0f,0.0f,1.0f); 

		gl.glPointSize(4.0f);
		gl.glLineWidth(2.0f);

		update_PROJECTION_MATRIX(drawable);

		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		System.out.print("Into display\n");
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		if(baldosas)
			embaldosar(this.numeroColumnas);
		else{
			gl.glViewport(0, 0, canvas.getWidth(), canvas.getHeight()); //Para arreglar el punto de vista a la hora de hacer embaldosar 1
			scene.draw(drawable);
		}
	}

	private void embaldosar(int nCol) {

		scene.embaldosar(nCol,canvas);
	}


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		double viewPortRatio= (double)width/(double)height;		
		scene.resize(viewPortRatio);
		update_PROJECTION_MATRIX(drawable);
	}


	/////////////////////////////////
	// KeyListener implementation
	@Override
	public void keyPressed(KeyEvent e) {

		switch(e.getKeyCode()){
		case 109: //PAra que funcione con el "-" que esta cerca de Bloq num, el de la derecha del teclado
		case KeyEvent.VK_MINUS: zoom(0.9);
		//scene.zoom(0.9) update_projection_Matrix...
		GLContext context = canvas.getGL().getGL2().getContext();
		if(!(context.isCurrent())) context.makeCurrent();
		update_PROJECTION_MATRIX(canvas);
		context.release();
		break;
		case 521: //Para que funcione con el "+" que está a la izq de Intro. El del "*"
		case KeyEvent.VK_ADD: zoom(1.1);
		//scene.zoom(1.1); update_projection_Matrix = true;
		GLContext context1 = canvas.getGL().getGL2().getContext();
		if(!(context1.isCurrent())) context1.makeCurrent();
		update_PROJECTION_MATRIX(canvas);
		context1.release();
		break;
		}

		canvas.display();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	/////////////////////////////////
	// MouseListener implementation
	@Override
	public void mouseClicked(MouseEvent e) {
		double xRaton = e.getX();
		double yRaton = e.getY();

		scene.setCenter(xRaton, yRaton, canvas.getWidth(), canvas.getHeight());
		GLContext context = canvas.getGL().getGL2().getContext();
		if(!(context.isCurrent())) context.makeCurrent();
		update_PROJECTION_MATRIX(canvas);
		canvas.display();		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	//////////////////////////////////
	// Specific methods for this class
	public void move(double xShift){
		scene.moveTriangle(xShift);
		canvas.display();
	}

	private void update_PROJECTION_MATRIX(GLAutoDrawable drawable) {
		double xLeft= scene.getXLeft();
		double xRight= scene.getXRight();
		double yBottom= scene.getYBottom();
		double yTop= scene.getYTop();

		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(xLeft, xRight, yBottom, yTop);
	}


	public void zoom(double fAux){

		double anchoNew = scene.getWidth()/fAux;
		double altoNew = scene.getHeight()/fAux;
		scene.actualizar(anchoNew, altoNew);

	}


	public void anidar() {		
		scene.anidar();
		canvas.display();		
	}


	public void desanidar() {
		scene.desaniar();
		canvas.display();
	}

	public void setEmbaldosarTrue(int numeroColumnas){
		if(numeroColumnas == 1) setEmbaldosarFalse();
		else if(numeroColumnas > 1){					/// Lo protegemos de no haber columnas o negativas
			this.baldosas = true;
			this.numeroColumnas = numeroColumnas;
			canvas.display();
		}		
	}
	public void setEmbaldosarFalse(){
		this.baldosas = false;
		canvas.display();
	}
}