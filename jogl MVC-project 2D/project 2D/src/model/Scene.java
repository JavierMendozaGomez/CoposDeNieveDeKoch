//////////////////////////////////////////// 
// Project skeleton for Computer 2D Graphics
// MVC-based design
// Author: Chus Martï¿½n
// 2014
//////////////////////////////////////////// 

package model;

//JOGL imports
///Autor@: Juan Antonio de la Vega Gutierrez & Javier Mendoza Gomez

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import controller.Lista;

public class Scene {

	// Scene Visible Area (SVA)
	private double xLeft, xRight, yTop, yBottom; // SVA position

	// Scene variables
	private double xTriangle, yTriangle;
	private double /*triangleWidth,*/ triangleHeight, triangleSide;
	private boolean redColor;
	private double xCenter;
	private double yCenter ;
	private double escalaAncho, escalaAlto;

	private Lapiz lapiz;
	private Lista lista;
	private Copo copo;

	/////////////////////////////////
	public Scene(double xLeft1, double xRight1, double yTop1, double yBottom1){
		// SVA
		xLeft= xLeft1;
		xRight= xRight1;
		yTop= yTop1;
		yBottom= yBottom1;
		xCenter = (xRight - xLeft)/2;
		yCenter  = (yTop - yBottom)/2;

		// Triangle size
		//triangleWidth = 0.4 * (xRight-xLeft);
		triangleHeight = 0.8 * (yTop-yBottom);
		triangleSide = 2 * triangleHeight / Math.sqrt(3); // arista = 2*altura/ raiz(3)

		// Triangle initial location
		this.xTriangle = getCentroX() - (triangleSide / 2); //La x inicial del triangulo esta situada restando la mitad del tamaño de la arista partiendo desde el centro del plano
		this.yTriangle = yBottom + 0.1*(yTop-yBottom);	//La y esta 10% de la altura total del plano

		copo = new Copo();
		lista = copo.inicializarTriangulo(lapiz, xTriangle, yTriangle, triangleSide);

		//   redColor= true;

	}


	/////////////////////////////////
	public double getXLeft()   { return xLeft;}
	public double getXRight()  { return xRight;}
	public double getYTop()    { return yTop;}
	public double getYBottom() { return yBottom;}

	public double getWidth()   { return xRight-xLeft;}
	public double getHeight()  { return yTop-yBottom;}

	public double getCentroX() { return (xLeft+xRight)/2.0;}
	public double getCentroY() { return (yBottom+yTop)/2.0;}

	/////////////////////////////////
	public void resize(double viewPortRatio){		
		double sceneVisibleAreaRatio=(xRight-xLeft)/(yTop-yBottom);

		if (sceneVisibleAreaRatio>=viewPortRatio){
			// Increase SVA height
			double newHight= (xRight-xLeft)/viewPortRatio;
			double yCenter= (yBottom+yTop)/2.0;
			yTop= yCenter + newHight/2.0;
			yBottom= yCenter - newHight/2.0;
		}
		else{
			// Increase SVA width
			double newWidth= viewPortRatio*(yTop-yBottom);
			double xCenter= (xLeft+xRight)/2.0;
			xRight= xCenter + newWidth/2.0;
			xLeft= xCenter - newWidth/2.0;
		}
	}

	public void actualizar(double anchoNew, double altoNew){

		//double xCenter = getCentroX();
		//double yCenter = getCentroY();

		xRight = xCenter + anchoNew/2.0;
		xLeft =  xCenter - anchoNew/2.0;

		yTop = yCenter + altoNew/2.0;
		yBottom = yCenter - altoNew/2.0;

		System.out.println(xRight + "  " + xLeft + "  " + yTop + "  " + yBottom + "     " + anchoNew + " " + altoNew);
	}

	/////////////////////////////////
	public void draw(GLAutoDrawable drawable){

		GL2 gl = drawable.getGL().getGL2();

		gl.glColor3f(1.0f,0.0f,0.0f);

		int numSegmentos = lista.getSize();		
		PV2D pos;

		gl.glBegin(GL.GL_LINE_LOOP);

		for(int i = 0; i < numSegmentos; i++){
			pos = lista.getSegmento(i).getInitPoint();
			System.out.println("X: " + pos.getX() + "    Y:" + pos.getY() + " Tam: " + lista.getSegmento(i).getDist() + "   Angulo: "  + lista.getSegmento(i).getDir());
			gl.glVertex2d(pos.getX(), pos.getY());
		}

		gl.glEnd();


		gl.glColor3f(0.0f,1.0f,0.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(xCenter, yCenter);
		gl.glEnd();		

		gl.glFlush();
	}




	/////////////////////////////////
	public void moveTriangle(double xShift){
		xTriangle += xShift;
	}

	/////////////////////////////////
	public void changeColor(){
		redColor = !redColor;
	}

	public void setCenter(double xRaton, double yRaton, int width, int height) {

		//Calculamos la escala, es decir la diferencia entre el tamaño del canvas y el de nuestra area visible
		escalaAncho = width/(xRight - xLeft); 
		escalaAlto = height/(yTop - yBottom);

		//Como xRaton e yRaton obtendra la posicion del canvas (numeros enteros sin escala) debemos escalarlos para que su posicion sea la adecuada por si hay zooms
		xRaton = xRaton / escalaAncho;
		yRaton = yRaton / escalaAlto;		

		//Calculamos el incremento(positivo o negativo) que sufre x e y 
		double incrX = xRaton - ((xRight - xLeft)/2);
		double incrY = yTop - yRaton - yCenter;

		//Sumamos a las 4 esquinas(aristas, bordes) el incremento y calculamos los nuevos centros respecto a estos nuevos bordes
		xRight += incrX;
		xLeft  += incrX;
		xCenter = (xLeft + xRight)/2.0;
		yTop   += incrY;
		yBottom+= incrY;
		yCenter = (yBottom + yTop)/2.0;
	}

	public void anidar(){
		this.lista = this.copo.anidar(this.lista);
	}


	public void desaniar() {
		this.lista = this.copo.desanidar(this.lista);		
	}


	public void embaldosar(int nCol,GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		double SVAratio = getWidth()/getHeight();
		double w=(double)drawable.getWidth()/(double)nCol;
		double h = w/ SVAratio;
		for(int c=0; c<nCol;c++){
			double currentH = 0;
			while(((currentH + h )) <= drawable.getHeight()){	
				gl.glViewport((int)(c*w),(int)currentH,(int)w,(int)h);
				draw(drawable);
				currentH +=h;
			}//while
		}//for
	}		

}